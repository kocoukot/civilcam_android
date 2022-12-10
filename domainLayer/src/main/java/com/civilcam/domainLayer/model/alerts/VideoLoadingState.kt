package com.civilcam.domainLayer.model.alerts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.R


interface VideoLoadingState {

    @Composable
    fun StateIcon(modifier: Modifier, onIconClick: () -> Unit)

    object Loading : VideoLoadingState {
        @Composable
        override fun StateIcon(modifier: Modifier, onIconClick: () -> Unit) =
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                modifier = modifier,
                color = Color(0xFFCF3919),
            )
    }

    object DownLoaded : VideoLoadingState {
        @Composable
        override fun StateIcon(modifier: Modifier, onIconClick: () -> Unit) =
            Icon(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_download_done),
                contentDescription = "video downloaded",
                tint = Color(0xFF00C26F)

            )
    }

    object ReadyToLoad : VideoLoadingState {
        @Composable
        override fun StateIcon(modifier: Modifier, onIconClick: () -> Unit) =
            Icon(
                modifier = modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = Color(0xFF000000)),
                ) {
                    onIconClick.invoke()
                },
                painter = painterResource(id = R.drawable.ic_download_video),
                contentDescription = "video ready for downloaded",
                tint = Color(0xFFCF3919)
            )
    }
}
