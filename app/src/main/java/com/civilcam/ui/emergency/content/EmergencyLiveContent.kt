package com.civilcam.ui.emergency.content

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.emergency.model.EmergencyScreen

@Composable
fun EmergencyLiveContent(
	modifier: Modifier,
	screen: EmergencyScreen
) {
	Column(
		Modifier.fillMaxSize()
	) {
		LiveBottomBar(
			data = "02.02.2022 3:29:56 AM",
			screen = screen
		)
	}
}

@Composable
fun LiveBottomBar(
	data: String,
	screen: EmergencyScreen
) {
	Column(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp)) {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.Bottom
		) {
			
			Row {
				Text(
					text = stringResource(id = R.string.emergency_live_title),
					style = CCTheme.typography.common_medium_text_regular,
					color = CCTheme.colors.white,
					modifier = Modifier.padding(start = 16.dp, end = 2.dp)
				)
				
				LiveAnimation()
			}
			
			Text(
				text = data,
				style = CCTheme.typography.common_medium_text_regular,
				color = CCTheme.colors.white,
				modifier = Modifier.padding(start = 16.dp, end = 2.dp)
			)
			
			Image(
				painter = if (screen == EmergencyScreen.COUPLED) painterResource(id = R.drawable.ic_live_extend) else painterResource(
					id = R.drawable.ic_live_minimize
				),
				contentDescription = null
			)
		}
		Row {
			Image(
				painter = painterResource(id = R.drawable.ic_live_camera_change),
				contentDescription = null
			)
			
			Image(
				painter = painterResource(id = R.drawable.ic_flash_light_off),
				contentDescription = null
			)
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
			animation = tween(1000),
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
