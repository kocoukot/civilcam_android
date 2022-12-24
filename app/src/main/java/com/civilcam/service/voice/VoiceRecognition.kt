package com.civilcam.service.voice

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import timber.log.Timber

interface VoiceRecognition {

    fun initRecorder()

    fun startRecord()

    fun stopRecord()

    fun destroy()

    class Base(private val context: Context, onActivate: () -> Unit) : VoiceRecognition {
        var recognizerState = RecognizerState.DESTROYED
        private var listener = SpeechRecognitionListener(
            onVoiceRecognized = { result ->
                recognizerState = RecognizerState.STOPED

                Timber.tag("recognise").i("recognition result $result")
                if (result.replace(" ", "").lowercase() in (KEY_VOICE_PHRASE)) {
                    stopRecord()
                    onActivate.invoke()
                } else {
                    startRecord()
                }
            },
            onErrorRecognizing = { errorText, needContinue ->
                recognizerState = RecognizerState.STOPED

                Timber.tag("recognise").i(errorText)
                if (needContinue) startRecord()
            }
        )
        private val speechRecognizer: SpeechRecognizer by lazy {
            SpeechRecognizer.createSpeechRecognizer(context)
        }

        override fun initRecorder() {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                Timber.tag("recognise").d("SpeechRecognizer inited")
                speechRecognizer.setRecognitionListener(listener)
            } else {
                listener.onErrorRecognizing.invoke("SpeechRecognizer is not available", false)
            }
        }

        override fun startRecord() {
            when (recognizerState) {
                RecognizerState.DESTROYED -> {
                    initRecorder()
                    launch()
                }
                RecognizerState.INITTED, RecognizerState.STOPED -> {
                    launch()
                }
                else -> {}

            }
        }

        private fun launch() {
            val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }
            speechRecognizer.startListening(recognizerIntent)
            recognizerState = RecognizerState.LISTENING
        }

        override fun stopRecord() {
            recognizerState = RecognizerState.DESTROYED
            speechRecognizer.destroy()
        }

        override fun destroy() {
            recognizerState = RecognizerState.DESTROYED
            speechRecognizer.destroy()
        }

        companion object {
            private val KEY_VOICE_PHRASE = listOf(
                "activatecivilcam",
                "activatecivilcamm",
                "activatecivilcom",
                "activatecivilcomm"
            )
        }

        enum class RecognizerState {
            DESTROYED,
            INITTED,
            LISTENING,
            STOPED
        }
    }
}