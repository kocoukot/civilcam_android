package com.civilcam.ui.emergency.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.theme.CCTheme
import timber.log.Timber

@Composable
fun EmergencyToastContent(
	onCloseClicked: () -> Unit,
	progress: Int
) {
	Timber.i("toast progress: $progress")
	val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
	Box(
		modifier = Modifier.fillMaxSize()
	) {
		Card(
			modifier = Modifier.padding(
				top = systemBarsPadding.calculateTopPadding() + 12.dp,
				start = 16.dp,
				end = 16.dp
			), shape = RoundedCornerShape(4.dp)
		) {
			Column {
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 12.dp, start = 12.dp, end = 12.dp),
					verticalAlignment = Alignment.Top
				) {
					Text(
						text = stringResource(id = R.string.emergency_toast_title),
						style = CCTheme.typography.common_medium_text_bold,
						color = CCTheme.colors.black
					)
					Spacer(modifier = Modifier.weight(1f))
					Icon(
						painter = painterResource(id = R.drawable.ic_notification_close),
						contentDescription = null,
						tint = CCTheme.colors.primaryRed,
						modifier = Modifier.clickable { onCloseClicked.invoke() }
					)
				}
				Text(
					text = stringResource(id = R.string.emergency_toast_description),
					style = CCTheme.typography.common_text_regular,
					color = CCTheme.colors.grayOne,
					modifier = Modifier
						.padding(top = 8.dp, start = 12.dp, end = 12.dp)
				)
				Spacer(modifier = Modifier.height(16.dp))
				LinearProgressIndicator(
					modifier = Modifier
						.fillMaxWidth()
						.height(6.dp)
						.background(color = Color.Magenta),
					backgroundColor = CCTheme.colors.primaryRed40,
					color = CCTheme.colors.primaryRed,
					progress = (progress / 3000f)
				)
			}
		}
	}
}