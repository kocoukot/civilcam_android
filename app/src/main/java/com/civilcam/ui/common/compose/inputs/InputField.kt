package com.civilcam.ui.common.compose.inputs

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.digits
import com.civilcam.common.ext.letters
import com.civilcam.common.theme.CCTheme
import com.civilcam.common.theme.MaterialSelectionColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
	inputCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
	trailingIcon: @Composable (() -> Unit)? = null,
	isReversed: Boolean = false,
	onTextClicked: (() -> Unit)? = null,
	onValueChanged: (String) -> Unit,
) {

	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	var hasFocus by remember { mutableStateOf(false) }
	var inputText by remember { mutableStateOf(text) }
	if (text.isNotEmpty()) inputText = text
	
	val titleColorState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else if (hasFocus) CCTheme.colors.black else CCTheme.colors.grayOne)
	val errorBorderState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.onFocusChanged { focusState ->
				when {
					focusState.hasFocus -> hasFocus = true
					!focusState.isFocused -> hasFocus = false
				}
			}
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
			style = CCTheme.typography.common_text_small_regular,
			color = titleColorState,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		MaterialTheme(
			colors = MaterialSelectionColor
		) {
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
					capitalization = inputCapitalization,
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
							if (inputText.isEmpty()) PlaceholderText(placeHolder)
							innerTextField()
						}
						if (trailingIcon != null) trailingIcon()
					}
				}
			)
		}
		
		AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
			ErrorText(errorMessage)
		}
	}
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    var hasFocus by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(text) }
    if (text.isNotEmpty()) inputText = text

    val titleColorState by
    animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else if (hasFocus) CCTheme.colors.black else CCTheme.colors.grayOne)
    val errorBorderState by
    animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                when {
                    focusState.hasFocus -> hasFocus = true
                    !focusState.isFocused -> hasFocus = false
                }
            }
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
            style = CCTheme.typography.common_text_small_regular,
            color = titleColorState,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )

        MaterialTheme(
            colors = MaterialSelectionColor
        ) {
            BasicTextField(
                cursorBrush = Brush.sweepGradient(colors = listOf(CCTheme.colors.white)),
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
                    capitalization = inputCapitalization,
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
                            if (inputText.isEmpty()) PlaceholderText(placeHolder)
                            innerTextField()
                        }
                        if (trailingIcon != null) trailingIcon()
                    }
                }
            )
        }

        AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
            ErrorText(errorMessage)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmailInputField(
	title: String,
	placeHolder: String,
	text: String = "",
	hasError: Boolean = false,
	errorMessage: String = "",
	isEnable: Boolean = true,
	isReversed: Boolean = false,
	onTextClicked: (() -> Unit)? = null,
	onValueChanged: (String) -> Unit,
	onFocusChanged: (Boolean) -> Unit
) {

  val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	var hasFocus by remember { mutableStateOf(false) }
	var inputText by remember { mutableStateOf(text) }
	if (text.isNotEmpty()) inputText = text
	
	val titleColorState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else if (hasFocus) CCTheme.colors.black else CCTheme.colors.grayOne)
	val errorBorderState by
	animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.onFocusChanged { focusState ->
				when {
					focusState.hasFocus -> hasFocus = true
					!focusState.isFocused -> hasFocus = false
				}
				onFocusChanged.invoke(focusState.hasFocus)
			}
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
			style = CCTheme.typography.common_text_small_regular,
			color = titleColorState,
			modifier = Modifier
				.padding(bottom = 8.dp)
				.fillMaxWidth()
		)
		
		MaterialTheme(
			colors = MaterialSelectionColor
		) {
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
					inputText = it
					onValueChanged.invoke(inputText.trim())
					
				},
				keyboardOptions = KeyboardOptions(
					capitalization = KeyboardCapitalization.None,
					keyboardType = KeyboardType.Email
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
							if (inputText.isEmpty()) PlaceholderText(placeHolder)
							innerTextField()
						}
					}
				}
			)
		}
		
		AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
			ErrorText(errorMessage)
		}
	}

    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    var hasFocus by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(text) }
    if (text.isNotEmpty()) inputText = text

    val titleColorState by
    animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else if (hasFocus) CCTheme.colors.black else CCTheme.colors.grayOne)
    val errorBorderState by
    animateColorAsState(targetValue = if (hasError && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                when {
                    focusState.hasFocus -> hasFocus = true
                    !focusState.isFocused -> hasFocus = false
                }
                onFocusChanged.invoke(focusState.hasFocus)
            }
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
            style = CCTheme.typography.common_text_small_regular,
            color = titleColorState,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )

        MaterialTheme(
            colors = MaterialSelectionColor
        ) {
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
                    inputText = it
                    onValueChanged.invoke(inputText.trim())

                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email
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
                            if (inputText.isEmpty()) PlaceholderText(placeHolder)
                            innerTextField()
                        }
                    }
                }
            )
        }

        AnimatedVisibility(visible = hasError && inputText.isNotEmpty()) {
            ErrorText(errorMessage)
        }
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
	animateColorAsState(
		targetValue =
		if (hasError) {
			CCTheme.colors.primaryRed
		} else if (inputText.isNotEmpty() && !hasError) {
			CCTheme.colors.black
		} else {
			CCTheme.colors.grayOne
		}
	)
	
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
		MaterialTheme(
			colors = MaterialSelectionColor
		) {
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
		}
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
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    var inputText by remember { mutableStateOf(text) }
    val otpColorState by
    animateColorAsState(
        targetValue =
        if (hasError) {
            CCTheme.colors.primaryRed
        } else if (inputText.isNotEmpty() && !hasError) {
            CCTheme.colors.black
        } else {
            CCTheme.colors.grayOne
        }
    )

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
        MaterialTheme(
            colors = MaterialSelectionColor
        ) {
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
        }
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PasswordField(
	name: String,
	placeholder: String,
	hasError: Boolean = false,
	noMatch: Boolean = false,
	isReEnter: Boolean = false,
	text: String = "",
	isReversed: Boolean = false,
	onValueChanged: (String) -> Unit
) {
	val coroutineScope = rememberCoroutineScope()
	val viewRequester = BringIntoViewRequester()
	val visibility: MutableState<Boolean> = remember { mutableStateOf(false) }
	var inputText by remember { mutableStateOf(text) }
	var hasFocus by remember { mutableStateOf(false) }
	
	
	val errorColorState by
	animateColorAsState(
		targetValue =
		if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty())
			CCTheme.colors.primaryRed
		else if (hasFocus)
			CCTheme.colors.black
		else CCTheme.colors.grayOne
	)
	
	val errorBorderState by
	animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
	
	if (text.isNotEmpty()) inputText = text
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
			.bringIntoViewRequester(viewRequester)
			.onFocusEvent {
				hasFocus = it.isFocused
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
		MaterialTheme(
			colors = MaterialSelectionColor
		) {
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
					keyboardType = KeyboardType.Password,
					capitalization = KeyboardCapitalization.None
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
							AnimatedContent(targetState = visibility.value) { state ->
								Text(
									stringResource(id = if (state) R.string.hide else R.string.show_title),
									modifier = Modifier.clickable {
										visibility.value = !visibility.value
									},
									style = CCTheme.typography.common_text_regular,
									color = if (state) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
								)
							}
						}
					}
				}
			)
		}
		AnimatedVisibility(visible = isReEnter && inputText.isNotEmpty()) {
			ErrorText(stringResource(id = R.string.password_no_match))
		}
	}

    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    val visibility: MutableState<Boolean> = remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(text) }
    var hasFocus by remember { mutableStateOf(false) }


    val errorColorState by
    animateColorAsState(
        targetValue =
        if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty())
            CCTheme.colors.primaryRed
        else if (hasFocus)
            CCTheme.colors.black
        else CCTheme.colors.grayOne
    )

    val errorBorderState by
    animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)

    if (text.isNotEmpty()) inputText = text
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
            .bringIntoViewRequester(viewRequester)
            .onFocusEvent {
                hasFocus = it.isFocused
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
        MaterialTheme(
            colors = MaterialSelectionColor
        ) {
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
                    keyboardType = KeyboardType.Password,
                    capitalization = KeyboardCapitalization.None
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
                            AnimatedContent(targetState = visibility.value) { state ->
                                Text(
                                    stringResource(id = if (state) R.string.hide else R.string.show_title),
                                    modifier = Modifier.clickable {
                                        visibility.value = !visibility.value
                                    },
                                    style = CCTheme.typography.common_text_regular,
                                    color = if (state) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
                                )
                            }
                        }
                    }
                }
            )
        }
        AnimatedVisibility(visible = isReEnter && inputText.isNotEmpty()) {
            ErrorText(stringResource(id = R.string.password_no_match))
        }
    }
}

@Preview
@Composable
private fun OtpCodeFieldPreview() {
	OtpCodeInputField(onValueChanged = {}, hasError = true)
}

@Preview
@Composable
private fun PasswordInputFieldPreview() {
	PasswordField(
		onValueChanged = {},
		name = stringResource(id = R.string.password),
		placeholder = stringResource(id = R.string.create_password),
		hasError = true
	)
}

@Preview
@Composable
private fun InputFieldPreview() {
	InputField(
		"First name",
		"Enter First Name",
		onValueChanged = {},
		hasError = true,
		errorMessage = stringResource(id = R.string.invalid_email),
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