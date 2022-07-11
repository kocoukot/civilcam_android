package com.civilcam.ui.emergency.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.civilcam.R

sealed class EmergencyButton(
    @StringRes val buttonText: Int,
    val buttonTextColor: Color,
    val buttonColor: Color
) {
    object InSafeButton : EmergencyButton(
        R.string.emergency_screen_sos,
        Color(0xFFCF3919),
        Color(0xFFFFFFFF)
    )

    object InDangerButton : EmergencyButton(
        R.string.emergency_screen_safe,
        Color(0xFFFFFFFF),
        Color(0xFFCF3919)
    )
}
