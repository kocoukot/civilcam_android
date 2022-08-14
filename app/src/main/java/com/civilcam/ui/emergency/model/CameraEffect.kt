package com.civilcam.ui.emergency.model

sealed class CameraEffect {
	data class RecordVideo(val filePath: String) : CameraEffect()
	object StopRecording : CameraEffect()
}