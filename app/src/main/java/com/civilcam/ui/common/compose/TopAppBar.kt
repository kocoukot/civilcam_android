package com.civilcam.ui.common.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.common.theme.CCTheme

@Composable
fun TopAppBarContent(
    title: String,
    titleSize: Int = 20,
    backgroundColor: Color = CCTheme.colors.white,
    actionItem: @Composable (() -> Unit)? = null,
    navigationItem: @Composable (() -> Unit)
) {

    TopAppBar(
        backgroundColor = backgroundColor,
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
            Row {
                Spacer(modifier = Modifier.width(8.dp))
                navigationItem()
            }
        },
        actions = {
            if (actionItem != null) {
                Row {
                    actionItem()
                 //   Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
    
    Divider(color = CCTheme.colors.grayThree, thickness = 1.dp)
}

@Preview
@Composable
private fun TopAppBarContentPreview() {
    TopAppBarContent(
        title = "Terms & Conditions",
        navigationItem = {
            AvatarButton {

            }
        },
        actionItem = {
            TextActionButton(actionTitle = "Contacts") {

            }
        },
    )
}