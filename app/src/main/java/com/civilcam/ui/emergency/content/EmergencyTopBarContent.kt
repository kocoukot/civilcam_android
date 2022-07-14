package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.AvatarButton
import com.civilcam.ui.emergency.model.EmergencyScreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmergencyTopBarContent(
	onAvatarClicked: () -> Unit,
	onSettingsClicked: () -> Unit,
	onLocationClicked: () -> Unit,
	onMapExtendClicked: () -> Unit,
	onMapMinimizeClicked: () -> Unit,
	locationData: String,
	screen: EmergencyScreen
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		
		AnimatedVisibility(visible = screen == EmergencyScreen.NORMAL) {
			AvatarButton {
				onAvatarClicked.invoke()
			}
		}
		
		AnimatedVisibility(
			visible = screen == EmergencyScreen.COUPLED ||
					screen == EmergencyScreen.MAP_EXTENDED
		) {
			BoxButton(
				painter = painterResource(id = R.drawable.ic_location_pin),
				onCardClicked = { onLocationClicked.invoke() }
			)
		}
		
		Spacer(modifier = Modifier.weight(1f))
		
		Card(
			onClick = {},
			shape = RoundedCornerShape(
				corner = CornerSize(50.dp)
			),
			backgroundColor = CCTheme.colors.white,
			modifier = Modifier
				.wrapContentSize()
				.padding(all = 0.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Image(
					painter = painterResource(id = R.drawable.ic_map_pin),
					contentDescription = null,
					modifier = Modifier.padding(start = 4.dp)
				)
				
				Text(
					text = locationData,
					style = CCTheme.typography.common_text_small_regular,
					color = CCTheme.colors.black,
					modifier = Modifier.padding(start = 4.dp, end = 15.dp)
				)
			}
		}
		
		Spacer(modifier = Modifier.weight(1f))
		
		AnimatedVisibility(visible = screen == EmergencyScreen.NORMAL) {
			BoxButton(
				painter = painterResource(id = R.drawable.ic_settings),
				onCardClicked = { onSettingsClicked.invoke() }
			)
		}
		
		AnimatedVisibility(visible = screen == EmergencyScreen.COUPLED) {
			BoxButton(
				painter = painterResource(id = R.drawable.ic_map_extended),
				onCardClicked = { onMapExtendClicked.invoke() }
			)
		}
		
		AnimatedVisibility(visible = screen == EmergencyScreen.MAP_EXTENDED) {
			BoxButton(
				painter = painterResource(id = R.drawable.ic_map_minimize),
				onCardClicked = { onMapMinimizeClicked.invoke() }
			)
		}
		
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxButton(
	onCardClicked: () -> Unit,
	painter: Painter
) {
	IconButton(
		onClick = { onCardClicked.invoke() },
		modifier = Modifier
			.clip(RoundedCornerShape(4.dp))
			.background(color = CCTheme.colors.white)
			.size(24.dp)
	) {
		Icon(
			painter = painter,
			contentDescription = null,
			tint = CCTheme.colors.primaryRed,
			modifier = Modifier.padding(all = 4.dp)
		)
	}
}