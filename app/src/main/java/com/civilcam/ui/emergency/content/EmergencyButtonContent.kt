package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.emergency.model.EmergencyButton
import kotlinx.coroutines.delay
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun EmergencyButtonContent(
    emergencyButton: EmergencyButton,
    modifier: Modifier,
    oneClick: () -> Unit,
    doubleClick: () -> Unit,
) {

    val buttonColorAnimated by animateColorAsState(targetValue = if (emergencyButton is EmergencyButton.InDangerButton) CCTheme.colors.primaryRed else CCTheme.colors.white)

    with(emergencyButton) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (emergencyButton is EmergencyButton.InDangerButton) {
                WavesAnimation(modifier)
            }

            Box(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .background(buttonColorAnimated, CircleShape)
                    .clip(CircleShape)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = CCTheme.colors.black),
                        onDoubleClick = {
                            Timber.i("detectTapGestures onDoubleTap")
                            doubleClick.invoke()
                        },
                        onClick = {
                            Timber.i("detectTapGestures onTap")
                            oneClick.invoke()
                        }),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = emergencyButton,
                    modifier = Modifier
                ) {
                    Text(
                        stringResource(buttonText),
                        color = buttonTextColor,
                        style = CCTheme.typography.emergency_button,
                        modifier = Modifier,
                        textAlign = TextAlign.Center,
                    )
                }
            }
//        }
        }
    }
}

@Composable
fun WavesAnimation(modifier: Modifier) {

    val waves = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) },
    )

    val animationSpec = infiniteRepeatable<Float>(
        animation = tween(2000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart,
    )

    waves.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 500L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = animationSpec
            )
        }
    }

    val dys = waves.map { it.value }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Waves
        dys.forEach { dy ->
            Box(
                Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        scaleX = dy * 4 + 1
                        scaleY = dy * 4 + 1
                        alpha = 1 - dy
                    },
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = CCTheme.colors.primaryRed, shape = CircleShape)
                )
            }
        }
    }
}