package com.civilcam.service.voice

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import timber.log.Timber

interface VoiceRecognition {

    fun initRecorder()

    fun startRecord(context: Context)

    fun stopRecord(context: Context)

    fun destroy()

    class Base(private val context: Context, onActivate: () -> Unit) : VoiceRecognition {
        var recognizerState = RecognizerState.DESTROYED
        private var listener = SpeechRecognitionListener(
            onVoiceRecognized = { result ->
                recognizerState = RecognizerState.STOPED

                Timber.tag("recognise").i("recognition result $result")
                if (result.replace(" ", "").lowercase() in (KEY_VOICE_PHRASE)) {
                    stopRecord(context)
                    onActivate.invoke()
                } else {
                    startRecord(context)
                }
            },
            onErrorRecognizing = { errorText, needContinue ->
                recognizerState = RecognizerState.STOPED

                Timber.tag("recognise").i(errorText)
                if (needContinue) startRecord(context)
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

        override fun startRecord(context: Context) {
            when (recognizerState) {
                RecognizerState.DESTROYED -> {
                    initRecorder()
                    launch(context)
                }
                RecognizerState.INITTED, RecognizerState.STOPED -> {
                    launch(context)
                }
                else -> {}

            }
        }

        private fun launch(context: Context) {
            val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).adjustStreamVolume(
                AudioManager.STREAM_NOTIFICATION,
                AudioManager.ADJUST_MUTE,
                0
            )
            speechRecognizer.startListening(recognizerIntent)
            recognizerState = RecognizerState.LISTENING
        }

        override fun stopRecord(context: Context) {
            (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager).adjustStreamVolume(
                AudioManager.STREAM_NOTIFICATION,
                AudioManager.ADJUST_UNMUTE,
                0
            )
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