package com.civilcam.ui.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme


@Composable
fun InformationRow(
    title: String,
    text: String = "",
    needDivider: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    rowClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(CCTheme.colors.white),
        verticalArrangement = Arrangement.Center,


        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clickable { rowClick.invoke() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column {

                Text(
                    text = title,
                    style = CCTheme.typography.common_text_medium
                )

                if (text.isNotEmpty())
                    Text(
                        text = text,
                        style = CCTheme.typography.common_text_small_regular,
                        color = CCTheme.colors.grayOne
                    )

            }

            Spacer(modifier = Modifier.weight(1f))
            if (trailingIcon != null) trailingIcon()
        }
        if (needDivider) RowDivider()
    }
}

@Composable
fun RowDivider() {
    Divider(color = CCTheme.colors.grayThree, modifier = Modifier.padding(start = 64.dp))

}

@Preview
@Composable
fun InformationRowPreview() {
    InformationRow(
        title = "Alleria Windrunner",
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.img_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape),
            )
        },
        trailingIcon = { TextActionButton("Resolve") {} },
        rowClick = {},
    )
}