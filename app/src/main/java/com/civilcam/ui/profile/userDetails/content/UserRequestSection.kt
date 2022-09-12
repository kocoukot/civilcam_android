package com.civilcam.ui.profile.userDetails.content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun UserRequestSection(
    userName: String,
    requestText: String,
    requestAnswer: (ButtonAnswer) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white),
        horizontalAlignment = Alignment.Start
    ) {
        RowDivider()

        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.primaryRed,
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp
                    ),
                ) {
                    append(userName)
                }
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.grayOne,
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp
                    ),
                ) {
                    append(" $requestText")
                }
            },
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(top = 12.dp)
                .padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnswerButton(
                text = stringResource(id = R.string.decline_text),
                buttonType = ButtonAnswer.DECLINE,
                modifier = Modifier.weight(1f),
                onButtonClick = requestAnswer::invoke
            )
            AnswerButton(
                text = stringResource(id = R.string.accept_text),
                modifier = Modifier.weight(1f),
                buttonType = ButtonAnswer.ACCEPT,
                onButtonClick = requestAnswer::invoke
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        RowDivider()

    }
}


@Composable
private fun AnswerButton(
    text: String,
    buttonType: ButtonAnswer,
    modifier: Modifier = Modifier,
    onButtonClick: (ButtonAnswer) -> Unit
) {
    Button(
        onClick = { onButtonClick.invoke(buttonType) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = CCTheme.colors.white),
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(0.dp),
        border = BorderStroke(1.dp, CCTheme.colors.grayOne)
    ) {
        Text(
            text = text,
            style = CCTheme.typography.common_text_small_medium,
            color = if (buttonType.answer) CCTheme.colors.primaryRed else CCTheme.colors.black,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}