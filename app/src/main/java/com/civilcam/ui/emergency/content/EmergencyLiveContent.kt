package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.DateUtils.getFullDateAndTimeString
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.compose.elements.LiveAnimation
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.emergency.EmergencyViewModel
import com.civilcam.ui.emergency.model.EmergencyActions
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun EmergencyLiveContent(
	screen: EmergencyScreen,
	viewModel: EmergencyViewModel,
	flashState: Boolean,
	onClick: (EmergencyActions) -> Unit
) {
	
	var currentLongTime by remember { mutableStateOf(System.currentTimeMillis()) }
	
	LaunchedEffect(Unit) {
		while (true) {
			delay(1.seconds)
			currentLongTime += 1000
		}
	}
	
	Box {
		
		EmergencyLivePreview(recordingViewModel = viewModel, onActionClick = {})
		
		LiveBottomBar(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.fillMaxWidth(),
			CurrentTimeComposable = {
				Text(
					text = getFullDateAndTimeString(currentLongTime),
					style = CCTheme.typography.common_medium_text_regular,
					color = CCTheme.colors.white,
					fontSize = 13.sp,
					modifier = Modifier.padding(start = 16.dp, end = 2.dp)
				)
			},
			screen = screen,
			onClick = onClick,
			flashState = flashState
		)
	}
}


@Composable
fun LiveBottomBar(
	modifier: Modifier = Modifier,
	CurrentTimeComposable: @Composable (() -> Unit),
	screen: EmergencyScreen,
	onClick: (EmergencyActions) -> Unit,
	flashState: Boolean
) {
	Column(
		modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp),
		verticalArrangement = Arrangement.Bottom,
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			LiveAnimation()
			CurrentTimeComposable()
			Crossfade(targetState = screen) { targetState ->
				IconActionButton(
					buttonIcon = if (targetState == EmergencyScreen.COUPLED) R.drawable.ic_live_extend else R.drawable.ic_live_minimize,
					buttonClick = {
						onClick.invoke(
							if (targetState == EmergencyScreen.COUPLED) EmergencyActions.ClickChangeScreen(
								EmergencyScreen.LIVE_EXTENDED
							) else EmergencyActions.ClickChangeScreen(EmergencyScreen.COUPLED)
						)
					},
					tint = CCTheme.colors.white,
					modifier = Modifier
						.clip(CircleShape)
						.size(28.dp)
						.clip(CircleShape)
				)
			}
		}
		
		AnimatedVisibility(
			visible = screen == EmergencyScreen.LIVE_EXTENDED,
			modifier = Modifier.padding(top = 12.dp)
		) {
			Box(
				modifier = Modifier.fillMaxWidth()
			) {
				
				IconActionButton(modifier = Modifier
					.align(Alignment.Center)
					.size(64.dp)
					.background(CCTheme.colors.black_70, CircleShape),
					tint = CCTheme.colors.white,
					buttonIcon = R.drawable.ic_turn_camera,
					buttonClick = { onClick.invoke(EmergencyActions.ChangeCamera) })
				
				IconActionButton(modifier = Modifier
					.align(Alignment.CenterEnd)
					.size(44.dp)
					.background(
						if (flashState) CCTheme.colors.white else CCTheme.colors.black_70,
						CircleShape
					),
					tint = if (flashState) CCTheme.colors.black else CCTheme.colors.white,
					buttonIcon = R.drawable.ic_flashlight,
					buttonClick = { onClick.invoke(EmergencyActions.ControlFlash) })
			}
		}
	}
}

