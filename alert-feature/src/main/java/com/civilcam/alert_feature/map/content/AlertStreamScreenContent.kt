package com.civilcam.alert_feature.map.content

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.model.LiveMapActions
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.compose.elements.LiveAnimation
import com.civilcam.ext_features.theme.CCTheme
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@Composable
fun AlertStreamScreenContent(
    modifier: Modifier = Modifier,
    alertScreenState: EmergencyScreen,
    cameraState: Int,
    onActionClick: (LiveMapActions) -> Unit
) {

    val bottomBarPadding by animateDpAsState(
        targetValue = if (alertScreenState == EmergencyScreen.COUPLED) 14.dp else 80.dp,
        animationSpec = tween(delayMillis = 250)
    )


    Box(modifier = modifier.background(CCTheme.colors.cianColor)) {
        EmergencyCameraPreview(
            cameraState = cameraState,
            modifier = Modifier.fillMaxSize()
        )
        LiveVideoBottomBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomBarPadding)
                .align(Alignment.BottomCenter),
            alertScreenState = alertScreenState,
            onActionClick = onActionClick
        )

    }
}

@Composable
private fun LiveVideoBottomBar(
    modifier: Modifier = Modifier,
    alertScreenState: EmergencyScreen,
    onActionClick: (LiveMapActions) -> Unit
) {

    var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            Timber.i("LaunchedEffect $currentLongTime")
            delay(1.seconds)
            currentLongTime += 1000
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LiveAnimation()

        Text(
            text = DateUtils.getFullDateAndTimeString(currentLongTime),
            style = CCTheme.typography.common_medium_text_regular,
            color = CCTheme.colors.white,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 16.dp, end = 2.dp)
        )

        Crossfade(targetState = alertScreenState) { targetState ->
            IconActionButton(
                buttonIcon = if (targetState == EmergencyScreen.COUPLED) R.drawable.ic_live_extend else R.drawable.ic_live_minimize,
                buttonClick = {
                    onActionClick.invoke(
                        if (targetState == EmergencyScreen.COUPLED)
                            LiveMapActions.ClickScreenChange(EmergencyScreen.LIVE_EXTENDED)
                        else
                            LiveMapActions.ClickScreenChange(EmergencyScreen.COUPLED)
                    )
                },
                tint = CCTheme.colors.white,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
            )
        }

    }
}


@Composable
fun EmergencyCameraPreview(
    modifier: Modifier = Modifier,
    cameraState: Int
) {
    Crossfade(targetState = cameraState, modifier = modifier) { state ->
        CreateCameraPreview(cameraState = state)
    }
}

@Composable
fun CreateCameraPreview(
    cameraState: Int
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraState)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize(),
    )
}