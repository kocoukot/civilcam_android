package com.civilcam.ui.emergency.model

import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.TorchState
import com.civilcam.common.ext.compose.ComposeFragmentState

data class EmergencyState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val location: String = "",
    val cameraFlash: Boolean = false,
    val isLocationAllowed: Boolean = false,
    val cameraState: Int = CameraSelector.LENS_FACING_BACK,
    val emergencyScreen: EmergencyScreen = EmergencyScreen.NORMAL,
    val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton,
    val lens: Int? = CameraSelector.LENS_FACING_BACK,
    val lensInfo: MutableMap<Int, CameraInfo> = mutableMapOf(),
    @TorchState.State val torchState: Int = TorchState.OFF,
    val emergencyUserModel: EmergencyUserModel? = null
) : ComposeFragmentState