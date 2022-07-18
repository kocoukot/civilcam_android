@file:OptIn(ExperimentalLayoutApi::class)

package com.civilcam.ui.settings.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.ext.isEmail
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.Constant.ISSUE_DESCRIPTION_LIMIT
import com.civilcam.ui.common.Constant.ISSUE_LIMIT
import com.civilcam.ui.common.compose.PlaceholderText
import com.civilcam.ui.common.compose.inputs.EmailInputField

@Composable
fun ContactSupportContent(
    supportInformation: (issue: String, description: String, email: String) -> Unit
) {

    var issue by remember { mutableStateOf("") }
    var issueDescription by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEmailError by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))


        IssueInputField(
            title = stringResource(id = R.string.settings_contact_issue_title),
            placeHolder = stringResource(id = R.string.settings_contact_issue_enter),
        ) {
            issue = it
            supportInformation.invoke(
                issue,
                issueDescription,
                email,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        DescriptionInputField {
            issueDescription = it
            supportInformation.invoke(
                issue,
                issueDescription,
                email,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        EmailInputField(
            isReversed = true,
            title = stringResource(id = R.string.settings_contact_reply_title),
            placeHolder = stringResource(id = R.string.settings_contact_reply_placeholder),
            errorMessage = stringResource(id = R.string.settings_contact_error_text),
            hasError = isEmailError,
            onValueChanged = {
                isEmailError = if (it.isEmpty()) false else !it.isEmail()
                email = it
                supportInformation.invoke(
                    issue,
                    issueDescription,
                    email,
                )
            },
            onFocusChanged = {

            }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IssueInputField(
    title: String,
    placeHolder: String,
    text: String = "",
    isError: Boolean = false,
    errorText: String = "",
    onValueChanged: (String) -> Unit,
) {
    var inputText by remember { mutableStateOf(text) }
    if (text.isNotEmpty()) inputText = text
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.lightGray)
            .imePadding()
            .imeNestedScroll()
    ) {
        val textColor by
        animateColorAsState(targetValue = if (isError) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
        Text(
            title,
            color = textColor,
            style = CCTheme.typography.common_text_small_regular,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )

        BasicTextField(
            textStyle = CCTheme.typography.common_text_regular,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            singleLine = true,
            value = inputText,
            onValueChange = {
                if (it.length <= ISSUE_LIMIT) {
                    inputText = it
                }
                onValueChanged.invoke(inputText.trim())
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            decorationBox = { innerTextField ->
                val borderColor by
                animateColorAsState(targetValue = if (isError) CCTheme.colors.primaryRed else CCTheme.colors.white)
                Row(
                    modifier = Modifier
                        .background(CCTheme.colors.white, RoundedCornerShape(4.dp))
                        .border(2.dp, borderColor, RoundedCornerShape(4.dp))
                        .padding(vertical = 14.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.weight(1f)
                    ) {
                        if (inputText.isEmpty()) PlaceholderText(placeHolder)
                        innerTextField()
                    }
                    if (inputText.isNotEmpty()) LimitLabelContent(
                        inputText.length,
                        ISSUE_LIMIT
                    )
                }
            }
        )

        AnimatedVisibility(visible = isError) {
            Text(
                text = errorText,
                modifier = Modifier,
                style = CCTheme.typography.common_error_text_regular
            )
        }
    }
}


@Composable
private fun DescriptionInputField(
    text: String = "",
    onValueChanged: (String) -> Unit,
) {
    var inputText by remember { mutableStateOf(text) }
    if (text.isNotEmpty()) inputText = text
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.lightGray)
            .imePadding()
            .imeNestedScroll()
    ) {

        BasicTextField(
            textStyle = CCTheme.typography.common_text_regular,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(4.dp)),
            singleLine = false,
            value = inputText,
            onValueChange = {

                if (it.length <= ISSUE_DESCRIPTION_LIMIT) inputText = it

                onValueChanged.invoke(inputText.trim())
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(CCTheme.colors.white, RoundedCornerShape(4.dp))
                        .padding(vertical = 14.dp)
                        .padding(start = 12.dp, end = 12.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        if (inputText.isEmpty()) PlaceholderText(stringResource(id = R.string.settings_contact_issue_description))
                        innerTextField()
                    }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        if (inputText.isNotEmpty()) LimitLabelContent(
                            inputText.length,
                            ISSUE_DESCRIPTION_LIMIT
                        )
                    }
                }
            }
        )
    }
}


@Composable
private fun LimitLabelContent(
    value: Int,
    limit: Int
) {
    Text(
        text = "$value/$limit",
        style = CCTheme.typography.common_text_regular,
        color = CCTheme.colors.grayOne,
        modifier = Modifier.padding(start = 8.dp)
    )
}

