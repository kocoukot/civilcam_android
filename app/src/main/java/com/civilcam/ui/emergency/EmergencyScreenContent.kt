package com.civilcam.ui.emergency

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.Constant.ANIMATION_DURATION
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.DividerLightGray
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.emergency.content.EmergencyButtonContent
import com.civilcam.ui.emergency.content.EmergencyLiveContent
import com.civilcam.ui.emergency.content.EmergencyMapContent
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen
import com.google.android.gms.maps.model.LatLng

@Composable
fun EmergencyScreenContent(viewModel: EmergencyViewModel) {

    val state by viewModel.state.collectAsState()
    val singapore = LatLng(1.35, 103.87)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val liveHeight by animateDpAsState(
        targetValue = if (state.emergencyScreen == EmergencyScreen.LIVE_EXTENDED) screenHeight
        else if (state.emergencyScreen == EmergencyScreen.COUPLED) screenHeight / 2
        else 0.dp,
        animationSpec = tween(ANIMATION_DURATION)
    )

    val mapHeight by animateDpAsState(
        targetValue = if (state.emergencyScreen == EmergencyScreen.MAP_EXTENDED || state.emergencyScreen == EmergencyScreen.NORMAL) screenHeight
        else if (state.emergencyScreen == EmergencyScreen.COUPLED) screenHeight / 2
        else 0.dp,
        animationSpec = tween(ANIMATION_DURATION)
    )

    var screenModifier = Modifier.fillMaxSize()

    if (state.emergencyScreen == EmergencyScreen.LIVE_EXTENDED || state.emergencyScreen == EmergencyScreen.MAP_EXTENDED) {
        screenModifier = screenModifier.statusBarsPadding()
    }
    Scaffold(
        backgroundColor = CCTheme.colors.grayThree,
        modifier = screenModifier,
        topBar = {
            AnimatedVisibility(
                visible = state.emergencyScreen == EmergencyScreen.MAP_EXTENDED ||
                        state.emergencyScreen == EmergencyScreen.LIVE_EXTENDED,
            ) {
                Column {
                    TopAppBarContent(
                        title = stringResource(id = state.emergencyScreen.title),
                        navigationItem = {
                            BackButton {
                                viewModel.setInputActions(EmergencyActions.GoBack)
                            }
                        },
                    )
                    DividerLightGray()
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column {
                AnimatedVisibility(
                    visible = state.emergencyScreen != EmergencyScreen.NORMAL,
                    modifier = Modifier.height(liveHeight),
                    enter = slideInVertically(
                        animationSpec = tween(
                            ANIMATION_DURATION,
                        )
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        animationSpec = tween(
                            ANIMATION_DURATION,
                        )
                    ) + fadeOut()
                ) {
                    EmergencyLiveContent(
                        screen = state.emergencyScreen,
                        cameraState = state.cameraState,
                        onClick = viewModel::setInputActions
                    )
                }


                EmergencyMapContent(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth(),
                    screenState = state.emergencyScreen,
                    locationData = state.location,
                    onActionClicked = viewModel::setInputActions,
                )
            }

            AnimatedVisibility(
                visible = state.emergencyScreen == EmergencyScreen.NORMAL ||
                        state.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
                enter = fadeIn(animationSpec = tween(ANIMATION_DURATION)),
                exit = fadeOut(animationSpec = tween(ANIMATION_DURATION))
            ) {
                EmergencyButtonContent(
                    emergencyButton = state.emergencyButton,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .size(150.dp)
                        .offset(y = (-36).dp),
                    onButtonClick = viewModel::setInputActions,
                )
            }
        }
    }
}

