package com.civilcam.ui.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
    titleSize: Int = 17,
    actionTitle: String = "",
    navigationAction: () -> Unit,
    actionAction: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(CCTheme.colors.white),
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .background(CCTheme.colors.white),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(navigationAction::invoke)
            }

            Text(
                color = CCTheme.colors.black,
                textAlign = TextAlign.Center,
                text = title,
                style = CCTheme.typography.common_text_regular,
                fontWeight = FontWeight.W600,
                fontSize = titleSize.sp
            )

            if (actionTitle.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    TextActionButton(actionTitle) { actionAction?.invoke() }
                }
            }
        }
    }
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



