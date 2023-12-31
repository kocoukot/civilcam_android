package com.civilcam.ext_features.compose.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun DialogLoadingContent() {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        AppCircularProgress(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun AppCircularProgress(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator(
            color = CCTheme.colors.primaryRed,
        )
    }
}