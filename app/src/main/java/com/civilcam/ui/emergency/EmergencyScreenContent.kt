package com.civilcam.ui.emergency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.DividerLightGray
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.emergency.content.EmergencyButtonContent
import com.civilcam.ui.emergency.content.EmergencyLiveContent
import com.civilcam.ui.emergency.content.EmergencyTopBarContent
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmergencyScreenContent(viewModel: EmergencyViewModel) {

    val state = viewModel.state.collectAsState()
    val singapore = LatLng(1.35, 103.87)

    val liveWeight by animateFloatAsState(
        targetValue = if (state.value.emergencyScreen == EmergencyScreen.LIVE_EXTENDED || state.value.emergencyScreen == EmergencyScreen.COUPLED) 1f else 0.1f,
        animationSpec = tween(animation_duration)
    )
    val userMapWeight by animateFloatAsState(
        targetValue = if (state.value.emergencyScreen == EmergencyScreen.NORMAL || state.value.emergencyScreen == EmergencyScreen.COUPLED || state.value.emergencyScreen == EmergencyScreen.MAP_EXTENDED) 1f else 0.1f,
        animationSpec = tween(animation_duration)
    )

//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }

    Scaffold(
        backgroundColor = CCTheme.colors.grayThree,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedVisibility(
                visible = state.value.emergencyScreen == EmergencyScreen.MAP_EXTENDED ||
                        state.value.emergencyScreen == EmergencyScreen.LIVE_EXTENDED,
//                enter = slideInVertically(animationSpec = tween(animation_duration)) + fadeIn(),
//                exit = slideOutVertically(animationSpec = tween(animation_duration)) + fadeOut()
            ) {
                Column {
                    TopAppBarContent(
                        title = stringResource(id = state.value.emergencyScreen.title),
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
                    visible = state.value.emergencyScreen == EmergencyScreen.COUPLED ||
                            state.value.emergencyScreen == EmergencyScreen.LIVE_EXTENDED,
                    modifier = Modifier.weight(liveWeight),
                    //   enter = slideInVertically(animationSpec = tween(animation_duration)) + fadeIn(),
                    //  exit = slideOutVertically(animationSpec = tween(animation_duration)) + fadeOut()
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(color = Color.Black)
                    ) {

                        EmergencyLiveContent(
                            screen = state.value.emergencyScreen,
                            cameraState = state.value.cameraState,
                            onClick = { action ->
                                when (action) {
                                    EmergencyActions.MinimizeLive -> {
                                        viewModel.setInputActions(
                                            EmergencyActions.ChangeMode(
                                                EmergencyScreen.COUPLED
                                            )
                                        )
                                    }
                                    EmergencyActions.MaximizeLive -> {
                                        viewModel.setInputActions(
                                            EmergencyActions.ChangeMode(
                                                EmergencyScreen.LIVE_EXTENDED
                                            )
                                        )
                                    }
                                    else -> viewModel.setInputActions(action)
                                }
                            }
                        )
                    }
                }

                AnimatedVisibility(
                    visible = state.value.emergencyScreen == EmergencyScreen.NORMAL ||
                            state.value.emergencyScreen == EmergencyScreen.COUPLED ||
                            state.value.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
//                    enter = slideInVertically(animationSpec = tween(animation_duration)) + fadeIn(),
//                    exit = slideOutVertically(animationSpec = tween(animation_duration)) + fadeOut(),
                    modifier = Modifier.weight(userMapWeight)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                    ) {

                        AnimatedVisibility(visible = state.value.emergencyScreen == EmergencyScreen.NORMAL) {
                            Spacer(modifier = Modifier.height(40.dp))
                        }

                        EmergencyTopBarContent(
                            locationData = state.value.location,
                            screen = state.value.emergencyScreen,
                            onClick = { action ->
                                when (action) {
                                    EmergencyActions.MaximizeMap -> {
                                        viewModel.setInputActions(
                                            EmergencyActions.ChangeMode(
                                                EmergencyScreen.MAP_EXTENDED
                                            )
                                        )
                                    }
                                    EmergencyActions.MinimizeMap -> {
                                        viewModel.setInputActions(
                                            EmergencyActions.ChangeMode(
                                                EmergencyScreen.COUPLED
                                            )
                                        )
                                    }
                                    else -> viewModel.setInputActions(action)
                                }

                            }
                        )

                        AnimatedVisibility(
                            visible = state.value.emergencyScreen == EmergencyScreen.NORMAL,
                            modifier = Modifier.padding(start = 16.dp, top = 88.dp),
                            enter = fadeIn(animationSpec = tween(animation_duration)),
                            exit = fadeOut(animationSpec = tween(animation_duration))
                        ) {
                            Spacer(modifier = Modifier.height(156.dp))
                            IconActionButton(
                                buttonIcon = R.drawable.ic_location_pin,
                                buttonClick = { viewModel.setInputActions(EmergencyActions.DetectLocation) },
                                tint = CCTheme.colors.primaryRed.copy(alpha = if (state.value.isLocationAllowed) 1f else 0.4f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color = CCTheme.colors.white)
                                    .size(28.dp)
                            )
                        }
                        /*GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState
                        ) {
                            Marker(
                                state = MarkerState(position = singapore),
                                title = "Singapore",
                                snippet = "Marker in Singapore"
                            )
                        }*/
                    }
                }
            }

            AnimatedVisibility(
                visible = state.value.emergencyScreen == EmergencyScreen.NORMAL ||
                        state.value.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
                enter = fadeIn(animationSpec = tween(animation_duration)),
                exit = fadeOut(animationSpec = tween(animation_duration))
            ) {
                EmergencyButtonContent(
                    emergencyButton = state.value.emergencyButton,
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

private const val animation_duration = 700
 