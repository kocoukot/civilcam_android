package com.civilcam.ui.common.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputField(
    title: String,
    placeHolder: String,
    text: String = "",
    isEnable: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    isReversed: Boolean = false,
    onTextClicked: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()

    Timber.d("getDateFromCalendar $text")
    var inputText by remember { mutableStateOf(text) }
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
            color = CCTheme.colors.grayText,
            style = CCTheme.typography.common_text_small_regular,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )

        BasicTextField(
            enabled = isEnable,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
                .clickable {
                    if (!isEnable) onTextClicked?.invoke()
                },
            singleLine = true,
            value = if (isEnable) inputText else text,
            onValueChange = {
                inputText = it
                onValueChanged.invoke(it.trim())
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .background(
                            if (isReversed) CCTheme.colors.white else CCTheme.colors.lightGray,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 12.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .weight(1f)
                    ) {
                        if (inputText.isEmpty()) Text(
                            placeHolder,
                            style = CCTheme.typography.common_text_regular,
                            color = CCTheme.colors.grayText
                        )
                        innerTextField()
                    }
                    if (trailingIcon != null) trailingIcon()
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview
@Composable
fun InputFieldPreview() {
    InputField("First name", "Enter First Name",
        onValueChanged = {})
}