package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
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

@Composable
fun EmergencyLiveContent(
	screen: EmergencyScreen,
	cameraState: Int,
	onClick: (EmergencyActions) -> Unit
) {
	
	BoxWithConstraints() {
		Box {
			EmergencyCameraPreview(
				cameraState = cameraState
			)
		}
		
		Box {
			Column {
				Spacer(modifier = Modifier.weight(1f))
				LiveBottomBar(
					data = "02.02.2022 3:29:56 AM",
					screen = screen,
					onClick = { action -> onClick.invoke(action) }
				)
			}
		}
	}
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun LiveBottomBar(
	data: String,
	screen: EmergencyScreen,
	onClick: (EmergencyActions) -> Unit
) {
	Column(
		Modifier
			.fillMaxWidth()
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
			
			Text(
				text = data,
				style = CCTheme.typography.common_medium_text_regular,
				color = CCTheme.colors.white,
				fontSize = 13.sp,
				modifier = Modifier.padding(start = 16.dp, end = 2.dp)
			)
			
			Spacer(modifier = Modifier.weight(1f))
			
			AnimatedVisibility(visible = screen == EmergencyScreen.COUPLED) {
				IconActionButton(
					buttonIcon = R.drawable.ic_live_extend,
					buttonClick = { onClick.invoke(EmergencyActions.MaximizeLive) },
					tint = CCTheme.colors.white,
					modifier = Modifier
						.size(28.dp)
				)
			}
			
			AnimatedVisibility(visible = screen == EmergencyScreen.LIVE_EXTENDED) {
				IconActionButton(
					buttonIcon = R.drawable.ic_live_minimize,
					buttonClick = { onClick.invoke(EmergencyActions.MinimizeLive) },
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
					
					Image(
						painter = painterResource(id = R.drawable.ic_flash_light_off),
						contentDescription = null,
						modifier = Modifier
							.layoutId("flash_button")
							.clickable { onClick.invoke(EmergencyActions.ControlFlash) }
					)
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
	Surface(
		color = CCTheme.colors.primaryRed,
		shape = CircleShape,
		modifier = Modifier
			.size(6.dp)
			.scale(scale),
		content = {}
	)
}

