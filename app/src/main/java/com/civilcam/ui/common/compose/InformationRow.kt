package com.civilcam.ui.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme


@Composable
fun InformationRow(
    title: String,
    titleFont: FontFamily = FontFamily(Font(R.font.roboto_medium)),
    text: String = "",
    titleColor: Color = CCTheme.colors.black,
    needDivider: Boolean = true,
    isClickable: Boolean = true,
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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CCTheme.colors.black),
                    enabled = isClickable
                ) { rowClick.invoke() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f),

                ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Column {

                        Text(
                            text = title,
                            style = CCTheme.typography.common_text_medium,
                            fontFamily = titleFont,
                            color = titleColor,
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
    }
}

@Preview
@Composable
private fun InformationRowPreview() {
    InformationRow(
        title = "Alleria Windrunner",
        text = "hasdbjs",
        trailingIcon = { TextActionButton("Resolve") {} },
        rowClick = {},
    )
}

@Composable
fun HeaderTitleText(text: String) {
    Text(
        text,
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.lightGray)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        style = CCTheme.typography.common_text_small_medium,
        color = CCTheme.colors.black
    )
}