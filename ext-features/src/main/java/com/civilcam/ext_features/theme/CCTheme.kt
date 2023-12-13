package com.civilcam.ext_features.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

    object CCTheme {

        val colors: CCColors
            @Composable get() = LocalCCColor.current

        val typography: CCTypography
            @Composable get() = LocalCCTypography.current

    }

internal val LocalCCColor = compositionLocalOf { CCColors() }
internal val LocalCCTypography = compositionLocalOf { CCTypography() }