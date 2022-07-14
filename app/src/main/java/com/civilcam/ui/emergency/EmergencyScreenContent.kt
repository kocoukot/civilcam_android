package com.civilcam.ui.emergency

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.DividerLightGray
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.emergency.content.EmergencyButtonContent
import com.civilcam.ui.emergency.content.EmergencyCameraPreview
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
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//    }
	
	Scaffold(
		backgroundColor = CCTheme.colors.grayThree,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Crossfade(targetState = state.value.emergencyScreen) { state ->
				AnimatedVisibility(
					visible = state == EmergencyScreen.MAP_EXTENDED ||
							state == EmergencyScreen.LIVE_EXTENDED,
					enter = fadeIn(animationSpec = tween(1000)),
					exit = fadeOut(animationSpec = tween(1000))
				) {
					Column(
						Modifier.padding(top = 24.dp)
					) {
						TopAppBarContent(
							title = if (state == EmergencyScreen.MAP_EXTENDED) stringResource(id = R.string.emergency_map_title)
							else stringResource(id = R.string.emergency_live_stream_title),
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
					modifier = Modifier.weight(1f),
					enter = fadeIn(animationSpec = tween(1000)),
					exit = fadeOut(animationSpec = tween(1000))
				) {
					Column(
						Modifier
							.fillMaxSize()
							.background(color = Color.Black)
					) {
						
						EmergencyLiveContent(
							screen = state.value.emergencyScreen,
							cameraState = state.value.cameraState,
							onExtendClicked = {
								viewModel.setInputActions(
									EmergencyActions.ChangeMode(
										EmergencyScreen.LIVE_EXTENDED
									)
								)
							},
							onMinimizeClicked = {
								viewModel.setInputActions(
									EmergencyActions.ChangeMode(
										EmergencyScreen.COUPLED
									)
								)
							},
							onCameraChangeClicked = {
								viewModel.setInputActions(EmergencyActions.ChangeCamera)
							},
							onFlashClicked = {
								viewModel.setInputActions(EmergencyActions.ControlFlash)
							}
						)
					}
				}
				
				AnimatedVisibility(
					visible = state.value.emergencyScreen == EmergencyScreen.NORMAL ||
							state.value.emergencyScreen == EmergencyScreen.COUPLED ||
							state.value.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
					enter = fadeIn(animationSpec = tween(1000)),
					exit = fadeOut(animationSpec = tween(1000)),
					modifier = Modifier.weight(1f)
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
							onAvatarClicked = { viewModel.setInputActions(EmergencyActions.GoUserProfile) },
							onSettingsClicked = { viewModel.setInputActions(EmergencyActions.GoSettings) },
							onLocationClicked = { viewModel.setInputActions(EmergencyActions.DetectLocation) },
							onMapExtendClicked = {
								viewModel.setInputActions(
									EmergencyActions.ChangeMode(
										EmergencyScreen.MAP_EXTENDED
									)
								)
							},
							onMapMinimizeClicked = {
								viewModel.setInputActions(
									EmergencyActions.ChangeMode(
										EmergencyScreen.COUPLED
									)
								)
							}
						)
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
				enter = fadeIn(animationSpec = tween(1000)),
				exit = fadeOut(animationSpec = tween(1000))
			) {
				EmergencyButtonContent(
					emergencyButton = state.value.emergencyButton,
					modifier = Modifier
						.align(Alignment.BottomCenter)
						.size(150.dp)
						.offset(y = (-32).dp),
					oneClick = {
						viewModel.setInputActions(EmergencyActions.OneClickSafe)
					},
					doubleClick = {
						viewModel.setInputActions(EmergencyActions.DoubleClickSos)
					},
				)
			}
		}
	}
}
 