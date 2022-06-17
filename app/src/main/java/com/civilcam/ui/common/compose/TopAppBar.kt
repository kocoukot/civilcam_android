package com.civilcam.ui.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme

@Composable
fun TopAppBarContent(
    title: String,
    navigationTitle: String,
    actionTitle: String = "",
    navigationAction: () -> Unit,
    actionAction: (() -> Unit)? = null
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                navigationAction.invoke()
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_navigation),
                        contentDescription = null,
                        tint = CCTheme.colors.black
                    )
                    Text(
                        navigationTitle,
                    )
                }
            }

        }

        Text(
            color = CCTheme.colors.black,
            textAlign = TextAlign.Center,
            text = title,
            style = CCTheme.typography.common_text_regular,
            fontWeight = FontWeight.W600
        )

        if (actionTitle.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                TextButton(onClick = {
                    actionAction?.invoke()
                }) {
                    Text(
                        text = actionTitle,
                        color = CCTheme.colors.primaryRed,
                        style = CCTheme.typography.common_text_medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TopAppBarContentPreview() {
    TopAppBarContent(
        title = "Terms & Conditions and\nPrivacy Policy",
        navigationTitle = "Back",
        actionTitle = "Contacts",
        navigationAction = {},
        actionAction = {},
    )
}