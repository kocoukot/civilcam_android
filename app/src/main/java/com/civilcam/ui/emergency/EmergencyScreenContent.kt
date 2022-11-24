package com.civilcam.ui.emergency

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.Constant.ANIMATION_DURATION
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.alert.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.emergency.content.LiveButtonContent
import com.civilcam.ui.emergency.content.LiveMapContent
import com.civilcam.ui.emergency.model.EmergencyActions
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LiveScreenContent(viewModel: EmergencyViewModel) {
	
	val state by viewModel.composeState.collectAsState()
	val screenModifier = Modifier.fillMaxSize()
	
	if (state.isLoading) DialogLoadingContent()
	
	if (state.errorText.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.errorText,
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { viewModel.setInputActions(EmergencyActions.ClickCloseAlert) }
		)
	}
	
	var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }
	
	LaunchedEffect(Unit) {
		while (true) {
			delay(1.seconds)
			currentLongTime += 1000
			viewModel.setInputActions(
				EmergencyActions.LiveCurrentTime(
					DateUtils.getFullDateAndTimeString(
						currentLongTime
					)
				)
			)
		}
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
			modifier = Modifier.fillMaxSize()
		) {
			
			Column {
				LiveMapContent(
					modifier = Modifier
						.fillMaxSize(),
					avatarUrl = state.userAvatar?.imageUrl,
					isLocationAllowed = state.isLocationAllowed,
					screenState = state.emergencyScreen,
					userLocationData = state.emergencyUserModel,
					onActionClicked = viewModel::setInputActions,
				)
			}
			
			AnimatedVisibility(
				visible = state.emergencyScreen == EmergencyScreen.NORMAL ||
						state.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
				enter = fadeIn(animationSpec = tween(ANIMATION_DURATION)),
				exit = fadeOut(animationSpec = tween(0))
			) {
				LiveButtonContent(
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

