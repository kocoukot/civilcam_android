package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.emergency.model.EmergencyScreen

@Composable
fun EmergencyLiveContent(
	screen: EmergencyScreen,
	onExtendClicked: () -> Unit,
	onMinimizeClicked: () -> Unit
) {
	Column(
		Modifier.fillMaxSize()
	) {
		//need to add camera screen
		
//		EmergencyCameraPreview(
//			screen = screen,
//			onExtendClicked = onExtendClicked,
//			onMinimizeClicked = onMinimizeClicked
//		)
		
		Spacer(modifier = Modifier.weight(1f))
		
		LiveBottomBar(
			data = "02.02.2022 3:29:56 AM",
			screen = screen,
			onExtendClicked = { onExtendClicked.invoke() },
			onMinimizeClicked = { onMinimizeClicked.invoke() }
		)
	}
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LiveBottomBar(
	data: String,
	screen: EmergencyScreen,
	onExtendClicked: () -> Unit,
	onMinimizeClicked: () -> Unit
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
				LiveButton(
					painter = painterResource(id = R.drawable.ic_live_extend),
					onCardClicked = { onExtendClicked.invoke() }
				)
			}
			
			AnimatedVisibility(visible = screen == EmergencyScreen.LIVE_EXTENDED) {
				LiveButton(
					painter = painterResource(id = R.drawable.ic_live_minimize),
					onCardClicked = { onMinimizeClicked.invoke() }
				)
			}
			
		}
		
		AnimatedVisibility(visible = screen == EmergencyScreen.LIVE_EXTENDED) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier
					.fillMaxWidth()
			) {
				
				Image(
					painter = painterResource(id = R.drawable.ic_live_camera_change),
					contentDescription = null,
				)

//			Image(
//				painter = painterResource(id = R.drawable.ic_flash_light_off),
//				contentDescription = null
//			)
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
	
	Box(modifier = Modifier.scale(scale)) {
		Surface(
			color = CCTheme.colors.primaryRed,
			shape = CircleShape,
			modifier = Modifier.size(6.dp),
			content = {}
		)
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveButton(
	onCardClicked: () -> Unit,
	painter: Painter
) {
	IconButton(
		onClick = { onCardClicked.invoke() },
		modifier = Modifier
			.size(24.dp)
	) {
		Icon(
			painter = painter,
			contentDescription = null,
			tint = CCTheme.colors.white
		)
	}
}
