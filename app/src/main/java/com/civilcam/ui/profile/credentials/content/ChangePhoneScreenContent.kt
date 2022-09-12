package com.civilcam.ui.profile.credentials.content

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.civilcam.ext_features.compose.elements.PhoneInputField
import kotlinx.coroutines.launch

@Composable
fun ChangePhoneScreenContent(
	onValueChanged: (String) -> Unit,
	isPhoneTaken: Boolean,
	errorMessage: String
) {
	val coroutineScope = rememberCoroutineScope()
	val listState = rememberScrollState()
	PhoneInputField(
		onValueChanged = {
			onValueChanged.invoke(it)
		},
		isReversed = true,
		isInFocus = {
			coroutineScope.launch {
				listState.scrollTo(1000)
			}
		},
		hasError = isPhoneTaken,
		errorMessage = errorMessage
	)
}