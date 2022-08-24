package com.civilcam.ui.profile.credentials.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ui.common.compose.inputs.EmailInputField
import timber.log.Timber

@Composable
fun ChangeEmailScreenContent(
	onValueChanged: (String) -> Unit,
	value: String,
	currentEmail: String,
	hasError: Boolean,
	errorMessage: String
) {
	Column {
		
		EmailInputField(
			title = stringResource(id = R.string.change_credentials_current_email_title),
			text = currentEmail,
			placeHolder = stringResource(id = R.string.create_account_email_placeholder),
			errorMessage = "",
			hasError = false,
			isEnable = false,
			onValueChanged = {},
			isReversed = true,
			onFocusChanged = {
				Timber.i("Focus state: $it")
			}
		)
		
		Spacer(modifier = Modifier.height(16.dp))
		
		EmailInputField(
			title = stringResource(id = R.string.change_credentials_new_email_title),
			text = value,
			placeHolder = stringResource(id = R.string.change_credentials_new_email_placeholder),
			errorMessage = errorMessage,
			hasError = hasError,
			onValueChanged = {
				onValueChanged.invoke(it)
			},
			isReversed = true,
			onFocusChanged = {
				Timber.i("Focus state: $it")
			}
		)
		
	}
}