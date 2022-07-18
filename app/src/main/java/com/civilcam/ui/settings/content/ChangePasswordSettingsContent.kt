package com.civilcam.ui.settings.content

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.PlaceholderText
import com.civilcam.ui.settings.model.ChangePasswordSectionData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordSettingsContent(
    passwordData: ChangePasswordSectionData,
    currentPassword: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(CCTheme.colors.white)
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))

        PasswordField(
            text = passwordData.currentPassword,
            hasError = !passwordData.error.isNullOrEmpty(),
            onValueChanged = {
                currentPassword.invoke(it)
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
private fun PasswordField(
    hasError: Boolean = false,
    noMatch: Boolean = false,
    text: String = "",
    onValueChanged: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    val visibility: MutableState<Boolean> = remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(text) }
    val errorColorState by
    animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
    val errorBorderState by
    animateColorAsState(targetValue = if ((hasError && inputText.isNotEmpty()) || noMatch && inputText.isNotEmpty()) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)

    if (text.isNotEmpty()) inputText = text
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.lightGray)
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
            stringResource(id = R.string.settings_password_label),
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
                        .background(CCTheme.colors.white, RoundedCornerShape(4.dp))
                        .padding(vertical = 14.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.weight(1f)
                    ) {
                        if (inputText.isEmpty()) PlaceholderText(text = stringResource(id = R.string.settings_password_placeholder))
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
        if (hasError) {
            Text(
                stringResource(id = R.string.settings_password_error_text),
                color = CCTheme.colors.primaryRed,
                style = CCTheme.typography.common_text_small_regular,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
