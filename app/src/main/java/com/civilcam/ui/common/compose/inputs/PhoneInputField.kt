package com.civilcam.ui.common.compose.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.clearPhone
import com.civilcam.common.ext.digits
import com.civilcam.common.theme.CCTheme
import com.civilcam.common.theme.MaterialSelectionColor
import com.civilcam.ui.common.compose.ErrorText
import com.civilcam.ui.common.compose.PlaceholderText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun PhoneInputField(
	text: String = "",
	hasError: Boolean = false,
	errorMessage: String = "",
	isInFocus: () -> Unit,
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit,
) {
	val coroutineScope = rememberCoroutineScope()
	var hasFocus by remember { mutableStateOf(false) }
	var inputText by remember { mutableStateOf(text) }
	if (text.isNotEmpty()) inputText = text
	else {
		LaunchedEffect(key1 = Unit) {
			inputText = ""
		}
	}
	
	
	val titleColorState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else if (hasFocus) CCTheme.colors.black else CCTheme.colors.grayOne)
	val errorBorderState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
 
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.onFocusEvent {
				hasFocus = it.isFocused
				if (it.isFocused) {
					coroutineScope.launch {
						delay(400)
						isInFocus.invoke()
					}
				}
			}
	) {
		
		Text(
			stringResource(id = R.string.profile_setup_phone_number_label),
			color = titleColorState,
			style = CCTheme.typography.common_text_small_regular,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		MaterialTheme(
			colors = MaterialSelectionColor
		) {
			
			BasicTextField(
				visualTransformation = PhoneNumberTransformation(),
				textStyle = if (hasError && inputText.isNotEmpty()) CCTheme.typography.common_text_regular_error else CCTheme.typography.common_text_regular,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 5.dp)
					.border(
						1.dp,
						errorBorderState,
						RoundedCornerShape(4.dp)
					)
					.clip(RoundedCornerShape(4.dp)),
				singleLine = true,
				value = inputText,
				onValueChange = { value ->
					if (value.length < 11) inputText = value.digits()
					onValueChanged.invoke(inputText.trim())
				},
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Phone
				),
				decorationBox = { innerTextField ->
					Row(
						modifier = Modifier
                            .background(
                                if (isReversed) CCTheme.colors.white else CCTheme.colors.lightGray,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(vertical = 14.dp)
                            .padding(start = 12.dp, end = 12.dp),
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							"+1 ",
							modifier = Modifier,
							style = CCTheme.typography.common_text_regular,
							color = CCTheme.colors.grayOne
						)
						Box(
							Modifier
								.weight(1f)
						) {
							if (inputText.isEmpty()) PlaceholderText(text = (stringResource(id = R.string.profile_setup_phone_number_placeholder)))
							innerTextField()
						}
					}
				}
			)
		}
		AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
			ErrorText(errorMessage)
		}
		if (inputText.clearPhone().length in 1..9 && !hasFocus) {
			ErrorText(stringResource(id = R.string.profile_setup_phone_error))
		}
		Spacer(modifier = Modifier.height(16.dp))
	}
}

@Preview
@Composable
fun PhoneInputFieldPreview() {
	PhoneInputField(onValueChanged = {}, isInFocus = {})
}

private class PhoneNumberTransformation : VisualTransformation {
	override fun filter(text: AnnotatedString): TransformedText {
		return phoneNumFilter(text)
	}
}

fun phoneNumFilter(text: AnnotatedString): TransformedText {
	
	// (XXX) XXX XXXX
	val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text
	var out = ""
	for (i in trimmed.indices) {
		if (i == 0) out += "("
		out += trimmed[i]
		if (i == 2) out += ") "
		if (i == 5) out += " "
	}
	
	val phoneNumberOffsetTranslator = object : OffsetMapping {
		override fun originalToTransformed(offset: Int): Int {
			if (offset <= 0) return offset
			if (offset < 3) return offset + 1
			if (offset == 3) return offset + 2
			if (offset <= 6) return offset + 3
			if (offset < 11) return offset + 4
			return 14
			
		}
		
		override fun transformedToOriginal(offset: Int): Int {
			if (offset <= 0) return offset
			if (offset <= 2) return offset - 1
			if (offset <= 3) return offset - 2
			if (offset <= 6) return offset - 3
			if (offset <= 11) return offset - 4
			return 14
		}
	}
	
	return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
}