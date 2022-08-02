package com.civilcam.ui.common.compose.inputs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.civilcam.common.ext.digits
import com.civilcam.common.theme.CCTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PinCodeInputField(
	pinCodeValue: (String) -> Unit,
	matchState: Boolean
) {
	
	var inputSize by remember { mutableStateOf(0) }
	var pinValue by remember { mutableStateOf("") }
	val pinColorState by
	animateColorAsState(
		targetValue =
		if (!matchState) {
			if (inputSize == PIN_SIZE) {
				CCTheme.colors.primaryRed
			} else {
				CCTheme.colors.black
			}
		} else {
			CCTheme.colors.black
		}
	)
	
	if (inputSize == PIN_SIZE) {
		LaunchedEffect(key1 = Unit) {
			delay(1000)
			inputSize = 0
			pinValue = ""
		}
	}
	
	
	Column(
		modifier = Modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	
	) {
		
		PinInputField(
			onValueChanged = {
				if (it.length < PIN_SIZE + 1) {
					pinValue = it
					pinCodeValue.invoke(pinValue)
					inputSize = it.takeIf { it.isNotEmpty() }?.length ?: 0
				}
			},
		)
		
		Row(
			modifier = Modifier.offset(y = (-52).dp),
			horizontalArrangement = Arrangement.Center
		) {
			(0 until PIN_SIZE).forEach {
				Icon(
					imageVector = if (inputSize > it) Icons.Default.Circle else Icons.Outlined.Circle,
					contentDescription = null,
					modifier = Modifier
                        .padding(12.dp)
                        .size(8.dp),
					tint = pinColorState
				)
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinInputField(
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
                .focusRequester(focusRequester),
			singleLine = true,
			value = inputText,
			onValueChange = { value ->
				if (value.length < PIN_SIZE + 1) {
					inputText = value.digits()
					onValueChanged.invoke(inputText.trim())
					if (value.length == PIN_SIZE) {
						inputText = ""
					}
				}
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

private const val PIN_SIZE = 4