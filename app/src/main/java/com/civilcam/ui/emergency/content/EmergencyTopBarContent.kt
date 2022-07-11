package com.civilcam.ui.emergency.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.AvatarButton


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmergencyTopBarContent(
	onAvatarClicked: () -> Unit,
	onSettingsClicked: () -> Unit,
    locationData: String
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 40.dp, start = 16.dp, end = 16.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		AvatarButton {
			onAvatarClicked.invoke()
		}
		
		Card(
			onClick = {},
			shape = RoundedCornerShape(
				corner = CornerSize(50.dp)
			),
			backgroundColor = CCTheme.colors.white,
			modifier = Modifier
				.wrapContentWidth()
				.navigationBarsPadding()
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
		
		Card(
			onClick = { onSettingsClicked.invoke() },
			shape = RoundedCornerShape(
				corner = CornerSize(4.dp)
			),
			backgroundColor = CCTheme.colors.white,
			modifier = Modifier
				.wrapContentWidth()
				.navigationBarsPadding()
		) {
			Image(
				painter = painterResource(id = R.drawable.ic_settings),
				contentDescription = null,
				modifier = Modifier.padding(all = 4.dp)
			)
		}
	}
}