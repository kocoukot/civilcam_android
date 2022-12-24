package com.civilcam.ui.emergency

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.Constant.ANIMATION_DURATION
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.emergency.content.EmergencyToastContent
import com.civilcam.ui.emergency.content.LiveButtonContent
import com.civilcam.ui.emergency.content.LiveMapContent
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyButton
import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LiveScreenContent(viewModel: EmergencyViewModel) {
	
	val state by viewModel.composeState.collectAsState()
	if (state.isLoading) DialogLoadingContent()
	
	var toastState by remember { mutableStateOf(false) }
	
	val animatedProgress by animateFloatAsState(
		targetValue = if (toastState) 1f else 0.0f,
		animationSpec = tween(durationMillis = 5000, easing = LinearEasing, delayMillis = 500),
		finishedListener = { toastState = false }// ProgressIndicatorDefaults.ProgressAnimationSpec
	)
	
	var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }
	LaunchedEffect(Unit) {
		while (true) {
			Timber.i("LaunchedEffect $currentLongTime")
			delay(1.seconds)
			currentLongTime += 1000
			viewModel.setInputActions(EmergencyActions.ChangeCurrentTime(currentLongTime))
		}
	}
	
	if (state.errorText.isNotEmpty()) {
		AlertDialogComp(dialogText = state.errorText,
			alertType = AlertDialogButtons.OK,
			onOptionSelected = { viewModel.setInputActions(EmergencyActions.ClickCloseAlert) })
	}
	
	
	
	Scaffold(backgroundColor = CCTheme.colors.grayThree,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			AnimatedVisibility(
				visible = state.emergencyScreen == EmergencyScreen.MAP_EXTENDED || state.emergencyScreen == EmergencyScreen.LIVE_EXTENDED,
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
		}) {
		Box(
			modifier = Modifier.fillMaxSize()
		) {
			val defaultButtonModifier = Modifier
				.align(Alignment.BottomCenter)
				.size(150.dp)
				.offset(y = (-36).dp)

			val buttonBox = if (state.emergencyScreen == EmergencyScreen.MAP_EXTENDED)
				Modifier
					.fillMaxSize()
					.navigationBarsPadding()
			else
				Modifier.fillMaxSize()

			Column {
				LiveMapContent(
					modifier = Modifier.fillMaxSize(),
					avatarUrl = state.userAvatar?.imageUrl,
					isLocationAllowed = state.isLocationAllowed,
					screenState = state.emergencyScreen,
					userLocationData = state.emergencyUserModel,
					onActionClicked = viewModel::setInputActions,
				)
			}

			AnimatedVisibility(
				visible = state.emergencyScreen == EmergencyScreen.NORMAL || state.emergencyScreen == EmergencyScreen.MAP_EXTENDED,
				enter = fadeIn(animationSpec = tween(ANIMATION_DURATION)),
				exit = fadeOut(animationSpec = tween(0))
			) {
				LiveButtonContent(
					emergencyButton = state.emergencyButton,
					buttonBoxM = buttonBox,
					modifier = defaultButtonModifier,
					onButtonClick = { action ->
						when (action) {
							is EmergencyActions.DoubleClickSos -> {
								toastState = false
								viewModel.setInputActions(action)
							}
							is EmergencyActions.OneClickSafe -> {
								if (state.emergencyButton == EmergencyButton.InSafeButton) {
									toastState = true
								} else {
									viewModel.setInputActions(action)
								}
							}
							else -> {}
						}
					},
				)
			}

			AnimatedVisibility(
				visible = toastState,
				enter = slideInVertically(animationSpec = tween(300)),
				exit = slideOutVertically(animationSpec = tween(300))
			) {
				EmergencyToastContent(progress = animatedProgress, onCloseClicked = {
					toastState = false
				})
			}
		}
	}
}

