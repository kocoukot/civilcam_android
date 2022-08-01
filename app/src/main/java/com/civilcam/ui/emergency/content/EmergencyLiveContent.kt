package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen
import com.civilcam.utils.DateUtils.getFullDateAndTimeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun EmergencyLiveContent(
    screen: EmergencyScreen,
    cameraState: Int,
    onClick: (EmergencyActions) -> Unit
) {
    val scope = CoroutineScope(Dispatchers.Main)

    var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }

    scope.launch {
        currentLongTime = System.currentTimeMillis()
        while (screen == EmergencyScreen.LIVE_EXTENDED || screen == EmergencyScreen.COUPLED) {
            delay(1000)
            currentLongTime = System.currentTimeMillis()
            Timber.i("flowCurrentTime ${System.currentTimeMillis()} currentLongTime $currentLongTime")
        }
    }

    Box {
        EmergencyCameraPreview(
            cameraState = cameraState,
            modifier = Modifier.fillMaxSize()
        )


        LiveBottomBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            dataComposable = {
                Text(
                    text = getFullDateAndTimeString(currentLongTime),
                    style = CCTheme.typography.common_medium_text_regular,
                    color = CCTheme.colors.white,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 2.dp)
                )
            },
            screen = screen,
            onClick = { action -> onClick.invoke(action) }
        )
    }
}


@Composable
fun LiveBottomBar(
    modifier: Modifier = Modifier,
    dataComposable: @Composable (() -> Unit),
    screen: EmergencyScreen,
    onClick: (EmergencyActions) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.emergency_live_title),
                    style = CCTheme.typography.common_medium_text_regular,
                    color = CCTheme.colors.white,
                    modifier = Modifier.padding(end = 4.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W500
                )

                LiveAnimation()
            }

            Spacer(modifier = Modifier.weight(1f))

            dataComposable()


            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = screen == EmergencyScreen.COUPLED || screen == EmergencyScreen.LIVE_EXTENDED) {
                IconActionButton(
                    buttonIcon = if (screen == EmergencyScreen.COUPLED) R.drawable.ic_live_extend else R.drawable.ic_live_minimize,
                    buttonClick = {
                        onClick.invoke(
                            if (screen == EmergencyScreen.COUPLED) EmergencyActions.MaximizeLive else EmergencyActions.MinimizeLive
                        )
                    },
                    tint = CCTheme.colors.white,
                    modifier = Modifier
                        .size(28.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = screen == EmergencyScreen.LIVE_EXTENDED,
            modifier = Modifier
                .padding(top = 13.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val constraints = ConstraintSet {
                    val changeButton = createRefFor("change_button")
                    val flashButton = createRefFor("flash_button")

                    constrain(changeButton) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                        centerHorizontallyTo(parent)
                    }
                    constrain(flashButton) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
                }
                ConstraintLayout(
                    constraints,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_live_camera_change),
                        contentDescription = null,
                        modifier = Modifier
                            .layoutId("change_button")
                            .clickable { onClick.invoke(EmergencyActions.ChangeCamera) }
                    )

                    Box(
                        modifier = Modifier
                            .background(CCTheme.colors.black, CircleShape)
                            .layoutId("flash_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_flashlight),
                            tint = CCTheme.colors.white,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .layoutId("flash_button")
                                .clickable { onClick.invoke(EmergencyActions.ControlFlash) }

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = Modifier
            .size(6.dp)
            .scale(scale)
            .background(CCTheme.colors.primaryRed, CircleShape),
    )
}

