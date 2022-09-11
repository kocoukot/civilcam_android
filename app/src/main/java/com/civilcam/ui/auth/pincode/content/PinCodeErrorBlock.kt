package com.civilcam.ui.auth.pincode.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.auth.pincode.model.PinCodeFlow

@Composable
fun PinCodeErrorBlock(
	screenState: PinCodeFlow
) {
	Column(
		Modifier
			.fillMaxWidth()
			.background(color = CCTheme.colors.primaryRed),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = when (screenState) {
				PinCodeFlow.CURRENT_PIN_CODE, PinCodeFlow.SOS_PIN_CODE -> {
					stringResource(id = R.string.pin_code_is_incorrect_title)
				}
				else -> stringResource(id = R.string.pin_code_new_no_match_title)
			},
			modifier = Modifier.padding(vertical = 4.dp),
			textAlign = TextAlign.Center,
			fontSize = 13.sp,
			color = CCTheme.colors.white,
			style = CCTheme.typography.common_text_regular
		)
	}
}