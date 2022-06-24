package com.civilcam.ui.common.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme

@Composable
fun TopAppBarContent(
    title: String,
    titleSize: Int = 20,
    color: Color = CCTheme.colors.white,
    actionTitle: String = "",
    isActionEnabled: Boolean = true,
    navigationAction: () -> Unit,
    actionAction: (() -> Unit)? = null
) {

    TopAppBar(
        backgroundColor = color,
        elevation = 0.dp,
        title = {
            Text(
                color = CCTheme.colors.black,
                textAlign = TextAlign.Start,
                text = title,
                style = CCTheme.typography.common_text_regular,
                fontWeight = FontWeight.W600,
                fontSize = titleSize.sp
            )
        },
        navigationIcon = {
            BackButton(navigationAction::invoke)
        },
        actions = {
            TextActionButton(actionTitle, isEnabled = isActionEnabled) { actionAction?.invoke() }
        }
    )
}

@Preview
@Composable
private fun TopAppBarContentPreview() {
    TopAppBarContent(
        title = "Terms & Conditions",
        actionTitle = "Contacts",
        navigationAction = {},
        actionAction = {},
    )
}

@Composable
fun BackButton(navigationAction: () -> Unit) {
    IconButton(
        modifier = Modifier,
        onClick = navigationAction
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.ic_back_navigation),
                contentDescription = null,
                tint = CCTheme.colors.black
            )

        }
    }
}



