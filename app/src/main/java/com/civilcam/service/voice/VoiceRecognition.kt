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
        var isListening = false
        private var listener = SpeechRecognitionListener(
            onVoiceRecognized = { result ->
                isListening = false

                Timber.tag("recognise").i("recognition result $result")
                if (result.replace(" ", "").lowercase() in (KEY_VOICE_PHRASE)) {
                    stopRecord()
                    onActivate.invoke()
                } else {
                    startRecord()
                }
            },
            onErrorRecognizing = { errorText, needContinue ->
                isListening = false
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
            if (!isListening) {
                val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                    putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
                }
                speechRecognizer.startListening(recognizerIntent)
                isListening = true
            }
        }

        override fun stopRecord() {
            isListening = false
            speechRecognizer.cancel()
        }

        override fun destroy() {
            isListening = false
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
    }
}