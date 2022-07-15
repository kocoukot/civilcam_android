package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.AvatarButton
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmergencyTopBarContent(
	onClick: (EmergencyActions) -> Unit,
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
		AnimatedVisibility(
			visible = screen == EmergencyScreen.NORMAL
		) {
			AvatarButton {
				onClick.invoke(EmergencyActions.GoUserProfile)
			}
		}
		
		AnimatedVisibility(
			visible = screen == EmergencyScreen.COUPLED ||
					screen == EmergencyScreen.MAP_EXTENDED
		) {
			IconActionButton(
				buttonIcon = R.drawable.ic_location_pin,
				buttonClick = { onClick.invoke(EmergencyActions.DetectLocation) },
				tint = CCTheme.colors.primaryRed,
				modifier = Modifier
					.clip(RoundedCornerShape(4.dp))
					.background(color = CCTheme.colors.white)
					.size(28.dp)
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
					modifier = Modifier.padding(start = 4.dp, end = 15.dp, top = 7.dp, bottom = 7.dp)
				)
			}
		}
		
		Spacer(modifier = Modifier.weight(1f))
		
		AnimatedVisibility(visible = screen == EmergencyScreen.NORMAL) {
			IconActionButton(
				buttonIcon = R.drawable.ic_settings,
				buttonClick = { onClick.invoke(EmergencyActions.GoSettings) },
				tint = CCTheme.colors.primaryRed,
				modifier = Modifier
					.clip(RoundedCornerShape(4.dp))
					.background(color = CCTheme.colors.white)
					.size(28.dp)
			)
		}
		
		AnimatedVisibility(visible = screen == EmergencyScreen.COUPLED) {
			IconActionButton(
				buttonIcon = R.drawable.ic_map_extended,
				buttonClick = { onClick.invoke(EmergencyActions.MaximizeMap) },
				tint = CCTheme.colors.primaryRed,
				modifier = Modifier
					.clip(RoundedCornerShape(4.dp))
					.background(color = CCTheme.colors.white)
					.size(28.dp)
			)
		}
		
		AnimatedVisibility(visible = screen == EmergencyScreen.MAP_EXTENDED) {
			IconActionButton(
				buttonIcon = R.drawable.ic_map_minimize,
				buttonClick = { onClick.invoke(EmergencyActions.MinimizeMap) },
				tint = CCTheme.colors.primaryRed,
				modifier = Modifier
					.clip(RoundedCornerShape(4.dp))
					.background(color = CCTheme.colors.white)
					.size(28.dp)
			)
		}
		
	}
}