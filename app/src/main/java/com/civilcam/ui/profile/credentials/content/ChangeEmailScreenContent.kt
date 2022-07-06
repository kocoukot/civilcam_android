package com.civilcam.ui.profile.credentials.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.inputs.EmailInputField
import timber.log.Timber

@Composable
fun ChangeEmailScreenContent(
	onValueChanged: (String) -> Unit,
	value: String,
	isEmail: Boolean,
	errorMessage: String
) {
	Column {
		
		EmailInputField(
			title = stringResource(id = R.string.create_account_email_label),
			text = value,
			placeHolder = stringResource(id = R.string.create_account_email_placeholder),
			errorMessage = errorMessage,
			hasError = !isEmail,
			onValueChanged = {
				onValueChanged.invoke(it)
			},
			isReversed = true,
			onFocusChanged = {
				Timber.i("Focus state: $it")
			}
		)
		
		Spacer(modifier = Modifier.height(8.dp))
		
		AnimatedVisibility(visible = isEmail || value.isEmpty()) {
			Text(
				text = stringResource(id = R.string.digit_verification_code),
				modifier = Modifier.fillMaxWidth(),
				color = CCTheme.colors.grayOne,
				fontSize = 13.sp
			)
		}
	}
}