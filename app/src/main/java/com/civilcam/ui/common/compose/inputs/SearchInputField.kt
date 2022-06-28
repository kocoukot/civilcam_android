package com.civilcam.ui.common.compose.inputs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.letters
import com.civilcam.common.theme.CCTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchInputField(
    text: String = "",
    isEnable: Boolean = true,
    isReversed: Boolean = false,
    onTextClicked: (() -> Unit)? = null,
    onValueChanged: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    Timber.d("getDateFromCalendar $text")
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
        BasicTextField(
            enabled = isEnable,
            textStyle = CCTheme.typography.common_text_regular,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    if (!isEnable) onTextClicked?.invoke()
                },
            singleLine = true,
            value = if (isEnable) inputText else text,
            onValueChange = {
                inputText = it.letters()
                onValueChanged.invoke(inputText.trim())

            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .background(
                            if (isReversed) CCTheme.colors.white else CCTheme.colors.lightGray,
                            RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 8.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputText.isEmpty())
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_search),
                                    contentDescription = null,
                                )
                                Text(
                                    stringResource(id = R.string.search_text),
                                    modifier = Modifier,
                                    style = CCTheme.typography.common_text_regular,
                                    color = CCTheme.colors.grayOne
                                )
                            }

                        innerTextField()
                    }

                }
            }
        )
    }
}


@Preview
@Composable
private fun SearchInputFieldPreview() {
    SearchInputField {

    }
}