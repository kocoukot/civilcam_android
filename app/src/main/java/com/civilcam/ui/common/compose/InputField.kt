package com.civilcam.ui.common.compose

import android.text.method.PasswordTransformationMethod
import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.digits
import com.civilcam.common.ext.letters
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.LengthCheckStrategy
import com.civilcam.ui.auth.LowerCaseCheckStrategy
import com.civilcam.ui.auth.OneDigitCheckStrategy
import com.civilcam.ui.auth.UpperCaseCheckStrategy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputField(
	title: String,
	placeHolder: String,
	text: String = "",
	hasError: Boolean = false,
	errorMessage: String = "",
	isEnable: Boolean = true,
	inputType: KeyboardType = KeyboardType.Text,
	trailingIcon: @Composable (() -> Unit)? = null,
	isReversed: Boolean = false,
	onTextClicked: (() -> Unit)? = null,
	onValueChanged: (String) -> Unit,
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	Timber.d("getDateFromCalendar $text")
	var inputText by remember { mutableStateOf(text) }
	val errorColorState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	val errorBorderState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
	
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.bringIntoViewRequester(viewRequester)
			.onFocusEvent {
				if (it.isFocused) {
					coroutineScope.launch {
						delay(400)
						viewRequester.bringIntoView()
					}
				}
			}
	) {
		
		Text(
			title,
			color = errorColorState,
			style = CCTheme.typography.common_text_small_regular,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		BasicTextField(
			enabled = isEnable,
			textStyle = if (hasError && inputText.isNotEmpty()) CCTheme.typography.common_text_regular_error else CCTheme.typography.common_text_regular,
			modifier = Modifier
				.fillMaxWidth()
				.clip(RoundedCornerShape(4.dp))
				.border(
					1.dp,
					errorBorderState,
					RoundedCornerShape(4.dp)
				)
				.clickable {
					if (!isEnable) onTextClicked?.invoke()
				},
			singleLine = true,
			value = if (isEnable) inputText else text,
			onValueChange = {
				inputText = if (isEnable && inputType == KeyboardType.Text) {
					it.letters()
				} else {
					it
				}
				onValueChanged.invoke(inputText.trim())
				
			},
			keyboardOptions = KeyboardOptions(
				capitalization = KeyboardCapitalization.Sentences,
				keyboardType = inputType
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
					Box(
						Modifier
							.weight(1f)
					) {
						if (inputText.isEmpty()) Text(
							placeHolder,
							modifier = Modifier,
							style = CCTheme.typography.common_text_regular,
							color = CCTheme.colors.grayOne
						)
						innerTextField()
					}
					if (trailingIcon != null) trailingIcon()
				}
			}
		)
		AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
			Text(
				errorMessage,
				color = CCTheme.colors.primaryRed,
				style = CCTheme.typography.common_text_small_regular,
				modifier = Modifier
					.padding(top = 8.dp)
					.fillMaxWidth()
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhoneInputField(
	text: String = "",
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit,
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	
	var inputText by remember { mutableStateOf(text) }
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.bringIntoViewRequester(viewRequester)
			.onFocusEvent {
				if (it.isFocused) {
					coroutineScope.launch {
						delay(400)
						viewRequester.bringIntoView()
					}
				}
			}
	) {
		
		Text(
			stringResource(id = R.string.profile_setup_phone_number_label),
			color = CCTheme.colors.grayOne,
			style = CCTheme.typography.common_text_small_regular,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		BasicTextField(
			visualTransformation = PhoneNumberTransformation(),
			textStyle = CCTheme.typography.common_text_regular,
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 5.dp)
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
						if (inputText.isEmpty()) Text(
							stringResource(id = R.string.profile_setup_phone_number_placeholder),
							modifier = Modifier,
							style = CCTheme.typography.common_text_regular,
							color = CCTheme.colors.grayOne
						)
						innerTextField()
					}
					
					
				}
			}
		)
		Spacer(modifier = Modifier.height(16.dp))
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OtpCodeInputField(
	text: String = "",
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit,
	hasError: Boolean
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	var inputText by remember { mutableStateOf(text) }
	val otpColorState by
	animateColorAsState(targetValue = if (hasError) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.bringIntoViewRequester(viewRequester)
			.onFocusEvent {
				if (it.isFocused) {
					coroutineScope.launch {
						delay(400)
						viewRequester.bringIntoView()
					}
				}
			}
	) {
		
		Text(
			stringResource(id = R.string.verification_code),
			color = otpColorState,
			style = CCTheme.typography.common_text_small_regular,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		val focusRequester = remember { FocusRequester() }
		BasicTextField(
			visualTransformation = OTPCodeTransformation(),
			textStyle = if (hasError) CCTheme.typography.common_text_regular_error else CCTheme.typography.common_text_regular,
			modifier = Modifier
				.fillMaxWidth()
				.border(
					1.dp,
					if (hasError) CCTheme.colors.primaryRed else CCTheme.colors.lightGray,
					RoundedCornerShape(4.dp)
				)
				.clip(RoundedCornerShape(4.dp))
				.focusRequester(focusRequester),
			singleLine = true,
			value = inputText,
			onValueChange = { value ->
				if (value.length < 7) inputText = value.digits()
				onValueChanged.invoke(inputText.trim())
			},
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Number
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
					Box(
						Modifier
							.weight(1f)
					) {
						if (inputText.isEmpty()) Text(
							stringResource(id = R.string.verification_code_placeholder),
							modifier = Modifier,
							style = CCTheme.typography.common_text_regular,
							color = otpColorState
						)
						innerTextField()
					}
				}
			}
		)
		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
		
		AnimatedVisibility(visible = hasError) {
			Text(
				stringResource(id = R.string.otp_error_code),
				color = CCTheme.colors.primaryRed,
				style = CCTheme.typography.common_text_small_regular,
				modifier = Modifier
					.padding(top = 8.dp)
					.fillMaxWidth()
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalUnitApi::class)
@Composable
fun PasswordField(
	name: String,
	placeholder: String,
	hasError: Boolean = false,
	noMatch: Boolean = false,
	isReEnter: Boolean = false,
	text: String = "",
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit,
	visibility: MutableState<Boolean> = remember { mutableStateOf(false) }
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	
	var inputText by remember { mutableStateOf(text) }
	val errorColorState by
	animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	val errorBorderState by
	animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
	
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.bringIntoViewRequester(viewRequester)
			.onFocusEvent {
				if (it.isFocused) {
					coroutineScope.launch {
						delay(400)
						viewRequester.bringIntoView()
					}
				}
			}
	) {
		Text(
			name,
			color = errorColorState,
			style = CCTheme.typography.common_text_small_regular,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		BasicTextField(
			visualTransformation = if (visibility.value) VisualTransformation.None else PasswordVisualTransformation(),
			textStyle = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.typography.common_text_regular_error else CCTheme.typography.common_text_regular,
			modifier = Modifier
				.fillMaxWidth()
				.clip(RoundedCornerShape(4.dp))
				.border(
					1.dp,
					errorBorderState,
					RoundedCornerShape(4.dp)
				),
			singleLine = true,
			value = inputText,
			onValueChange = {
				inputText = it
				onValueChanged.invoke(inputText.trim())
			},
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Password
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
					Box(
						Modifier.weight(1f)
					) {
						if (inputText.isEmpty())
							Text(
								placeholder,
								modifier = Modifier,
								style = CCTheme.typography.common_text_regular,
								color = CCTheme.colors.grayOne
							)
						
						innerTextField()
					}
					if (inputText.isNotEmpty()) {
						Text(
							stringResource(id = if (visibility.value) R.string.hide else R.string.show_title),
							modifier = Modifier.clickable {
							    visibility.value = !visibility.value
							},
							style = CCTheme.typography.common_text_regular,
							color = if (visibility.value) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
						)
					}
				}
			}
		)
		if (isReEnter && inputText.isNotEmpty()) {
			Text(
				stringResource(id = R.string.password_no_match),
				color = CCTheme.colors.primaryRed,
				style = CCTheme.typography.common_text_small_regular,
				modifier = Modifier
					.padding(top = 8.dp)
					.fillMaxWidth()
			)
		}
	}
}

@Preview
@Composable
fun PhoneInputFieldPreview() {
	PhoneInputField(onValueChanged = {})
}

@Preview
@Composable
fun OtpCodeFieldPreview() {
	OtpCodeInputField(onValueChanged = {}, hasError = true)
}

@Preview
@Composable
fun PasswordInputFieldPreview() {
	PasswordField(
		onValueChanged = {},
		name = stringResource(id = R.string.password),
		placeholder = stringResource(id = R.string.create_password),
		hasError = true
	)
}

@Preview
@Composable
fun InputFieldPreview() {
	InputField(
		"First name", "Enter First Name",
		onValueChanged = {}, hasError = true, errorMessage = stringResource(id = R.string.invalid_email)
	)
}

private class OTPCodeTransformation : VisualTransformation {
	override fun filter(text: AnnotatedString): TransformedText {
		return otpCodeFilter(text)
	}
}

fun otpCodeFilter(text: AnnotatedString): TransformedText {
	
	val trimmed = if (text.text.length >= 6) text.text.substring(0..5) else text.text
	var out = ""
	for (i in trimmed.indices) {
		if (i == 3) out += " "
		out += trimmed[i]
	}
	
	val phoneNumberOffsetTranslator = object : OffsetMapping {
		override fun originalToTransformed(offset: Int): Int {
			if (offset == 4) return offset + 1
			if (offset == 5) return offset + 1
			if (offset == 6) return offset + 1
			return offset
			
		}
		
		override fun transformedToOriginal(offset: Int): Int {
			if (offset == 4) return offset + 1
			if (offset == 5) return offset + 1
			if (offset == 6) return offset + 1
			return offset
		}
	}
	
	return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
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