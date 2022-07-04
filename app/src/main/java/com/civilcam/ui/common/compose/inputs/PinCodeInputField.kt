package com.civilcam.ui.common.compose.inputs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.civilcam.common.ext.digits
import com.civilcam.common.theme.CCTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinCodeInputField(
	pinCodeValue: (String) -> Unit
) {
	
	val inputPin = remember { mutableStateListOf<Int>() }
	val pinValue = remember { mutableStateOf("") }
	val pinSize = 4
	
	if (inputPin.size == 4) {
		LaunchedEffect(true) {
			delay(300)
			pinCodeValue.invoke(pinValue.value)
			inputPin.clear()
			pinValue.value = ""
		}
	}
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(CCTheme.colors.white),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		
		Box(
			modifier = Modifier.fillMaxWidth()
		) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				
				PinInputField(
					onValueChanged = {
						if (it.length > pinValue.value.length) {
							inputPin.add(it.toInt())
						}
						pinValue.value = it
					},
					onBackSpace = {
						if ((pinValue.value.isNotEmpty() || pinValue.value == "")
							&& inputPin.size > 0
						) {
							inputPin.removeLast()
						}
					}
				)
				
				Row(
					modifier = Modifier.offset(y = (-52).dp)
				) {
					(0 until pinSize).forEach {
						Icon(
							imageVector = if (inputPin.size > it) Icons.Default.Circle else Icons.Outlined.Circle,
							contentDescription = null,
							modifier = Modifier
								.padding(12.dp)
								.size(8.dp),
							tint = CCTheme.colors.black
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun PinInputField(
	text: String = "",
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit,
	onBackSpace: () -> Unit
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	var inputText by remember { mutableStateOf(text) }
	
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.offset(y = (-12).dp)
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
		
		val focusRequester = remember { FocusRequester() }
		BasicTextField(
			visualTransformation = PinCodeTransformation(),
			textStyle = CCTheme.typography.common_text_regular,
			modifier = Modifier
				.fillMaxWidth()
				.alpha(0f)
				.clip(RoundedCornerShape(4.dp))
				.focusRequester(focusRequester)
				.onKeyEvent { keyEvent: KeyEvent ->
					if (keyEvent.key == Key.Backspace) {
						onBackSpace.invoke()
					}
					false
				},
			singleLine = true,
			value = inputText,
			onValueChange = { value ->
				if (value.length < 5) inputText = value.digits()
				onValueChanged.invoke(inputText.trim())
				if (value.length == 4) inputText = ""
			},
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Number,
				imeAction = ImeAction.Done
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
							"",
							modifier = Modifier,
							style = CCTheme.typography.common_text_regular,
							color = CCTheme.colors.black
						)
						innerTextField()
					}
				}
			},
			cursorBrush = Brush.verticalGradient(
				0.00f to Color.Transparent,
				0.00f to Color.Transparent,
				0.00f to Color.Transparent,
				0.00f to Color.Transparent,
				0.00f to Color.Transparent,
				0.00f to Color.Transparent
			)
		)
		LaunchedEffect(Unit) {
			focusRequester.requestFocus()
		}
	}
}

private class PinCodeTransformation : VisualTransformation {
	override fun filter(text: AnnotatedString): TransformedText {
		return pinCodeFilter(text)
	}
}

fun pinCodeFilter(text: AnnotatedString): TransformedText {
	
	val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
	var out = ""
	for (i in trimmed.indices) {
		out += trimmed[i]
	}
	
	val phoneNumberOffsetTranslator = object : OffsetMapping {
		override fun originalToTransformed(offset: Int): Int {
			return offset
			
		}
		
		override fun transformedToOriginal(offset: Int): Int {
			return offset
		}
	}
	
	return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
}
