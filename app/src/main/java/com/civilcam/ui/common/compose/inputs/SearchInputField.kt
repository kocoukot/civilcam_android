package com.civilcam.ui.common.compose.inputs

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.letters
import com.civilcam.common.theme.CCTheme
import com.civilcam.common.theme.MaterialSelectionColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun SearchInputField(
    text: String = "",
    isLetters: Boolean = true,
    looseFocus: Boolean = false,
    isReversed: Boolean = false,
    onValueChanged: (String) -> Unit,
    isFocused: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val viewRequester = BringIntoViewRequester()
    var inputText by remember { mutableStateOf(text) }
    var hasFocus by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    if (text.isNotEmpty()) inputText = text

    if (looseFocus) {
        focusManager.clearFocus()
        inputText = ""
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isReversed) CCTheme.colors.lightGray else CCTheme.colors.white)
            .bringIntoViewRequester(viewRequester)
            .focusRequester(focusRequester)
            .onFocusEvent {
                hasFocus = it.hasFocus
                if (it.isFocused) {
                    isFocused.invoke()
                    coroutineScope.launch {
                        delay(400)
                        viewRequester.bringIntoView()
                    }
                }
            }
    ) {

        MaterialTheme(
            colors = MaterialSelectionColor
        ) {


            BasicTextField(
//            enabled = isEnable,
                textStyle = CCTheme.typography.common_text_regular,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
                    .clickable {

                    },
                singleLine = true,
                value = inputText,
                onValueChange = {
                    inputText = if (isLetters) it.letters() else it
                    onValueChanged.invoke(inputText.trim())
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .background(
                                if (isReversed) CCTheme.colors.white else CCTheme.colors.lightGray,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(vertical = 8.dp)
                            .padding(start = 12.dp, end = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
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
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        stringResource(id = R.string.search_text),
                                        modifier = Modifier,
                                        style = CCTheme.typography.common_text_medium,
                                        color = CCTheme.colors.grayOne
                                    )
                                }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(
                                visible = hasFocus && inputText.isNotEmpty(),
                                enter = slideInHorizontally(),
                                exit = slideOutHorizontally()
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.padding(end = 8.dp))
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 16.dp)
                            ) {
                                innerTextField()
                            }
                            AnimatedVisibility(
                                visible = inputText.isNotEmpty(),
                                enter = scaleIn(),
                                exit = scaleOut()
                            ) {
                                ClearButton {
                                    inputText = ""
                                    onValueChanged.invoke(inputText.trim())
                                }
                            }
                        }

                    }
                }
            )
        }
    }
}

@Composable
private fun ClearButton(onClearText: () -> Unit) {
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClearText.invoke() },
        painter = painterResource(id = R.drawable.ic_clear),
        contentDescription = null,
        tint = CCTheme.colors.grayOne
    )
}


@Preview
@Composable
private fun SearchInputFieldPreview() {

}