package com.civilcam.ext_features.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


//data class SMTypography internal constructor
data class CCColors internal constructor(
    val white: Color = Color(0xFFFFFFFF),
    val white30: Color = Color(0x4DFFFFFF),

    val black: Color = Color(0xFF111111),
    val black_70: Color = Color(0xB3111111),

    val primaryGreen: Color = Color(0xFF00C26F),
    val primaryRed: Color = Color(0xFFCF3919),
    val primaryRed40: Color = Color(0xFFECB0A3),

    val grayOne: Color = Color(0xFF9C9C9C),
    val grayOneDark: Color = Color(0xFF333333),

    val grayTwo: Color = Color(0xFFB4BAC3),
    val grayThree: Color = Color(0xFFE4E4E4),
    val lightGray: Color = Color(0xFFF5F6F6),

    val cianColor: Color = Color(0xFF4B9595),
)

val MaterialSelectionColor = lightColors(
    primary = Color(0xFF000000),
)