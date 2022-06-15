package com.civilcam.ui.common.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.common.theme.CCTheme

@Composable
fun DialogLoadingContent() {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = CCTheme.colors.primaryRed,
            )
        }
    }
}
