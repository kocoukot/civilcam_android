package com.civilcam.service.voice

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import timber.log.Timber


class SpeechRecognitionListener(
    val onVoiceRecognized: (String) -> Unit,
    val onErrorRecognizing: (String, Boolean) -> Unit,
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {
        Timber.tag("recognise").d("speech ready ")
    }

    override fun onBeginningOfSpeech() {
        Timber.tag("recognise").d("speech begin ")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Timber.tag("recognise").d("speech onRmsChanged")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Timber.tag("recognise").d("speech onBufferReceived ")
    }

    override fun onEndOfSpeech() {
        Timber.tag("recognise").d("speech onEndOfSpeech ")
    }

    override fun onError(error: Int) {
        var needContinue = false
        val message = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_SERVER -> "error from server"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_CLIENT -> {
                needContinue = true
                "Audio recording stopped"
            }
            SpeechRecognizer.ERROR_NO_MATCH -> {
                needContinue = true
                "No match"
            }
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                needContinue = true
                "RecognitionService busy"
            }
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                needContinue = true
                "No speech input"
            }
            else -> {
                needContinue = true
                "Didn't understand, please try again."
            }
        }
        Timber.tag("recognise").d("speech $message")
        onErrorRecognizing(message, needContinue)
    }

    override fun onResults(results: Bundle?) {
        val result =
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.first().orEmpty()
        onVoiceRecognized(result)
        Timber.tag("recognise").d("speech onResults $result ")
    }


    override fun onPartialResults(partialResults: Bundle?) {
        val result =
            partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.first()
                .orEmpty()
//        onVoiceRecognized(result)
//        Timber.tag("recognise").d("speech onPartialResults $result ")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Timber.tag("recognise").d("speech onEvent $eventType ")
    }
}