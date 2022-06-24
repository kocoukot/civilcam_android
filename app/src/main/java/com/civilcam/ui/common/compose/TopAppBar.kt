package com.civilcam.ui.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    actionTitle: String = "",
    navigationAction: () -> Unit,
    actionAction: (() -> Unit)? = null
) {

    TopAppBar(
        backgroundColor = CCTheme.colors.white,
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
            TextActionButton(actionTitle) { actionAction?.invoke() }
        }
    )
}

@Preview
@Composable
fun TopAppBarContentPreview() {
    TopAppBarContent(
        title = "Terms & Conditions",
        actionTitle = "Contacts",
        navigationAction = {},
        actionAction = {},
    )
}


@Preview
@Composable
fun SlicedTopAppBarContentPreview() {
    SlicedTopAppBarContent(
//        actionTitle = "Contacts",
        navigationAction = {},
        actionAction = {},
    )
}

@Composable
fun SlicedTopAppBarContent(
    actionTitle: String = "",
    actionAction: (() -> Unit)? = null,
    navigationAction: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackButton(navigationAction::invoke)
        if (actionTitle.isNotEmpty())
            TextActionButton(actionTitle) { actionAction?.invoke() }
    }
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



