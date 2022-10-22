package com.civilcam.ext_features.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.ext_features.R
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun EmptyListText(text: String) {
    Text(
        text,
        textAlign = TextAlign.Center,
        style = CCTheme.typography.common_text_regular,
        modifier = Modifier
            .background(CCTheme.colors.white, CircleShape)
            .padding(vertical = 8.dp, horizontal = 16.dp),
    )
}


@Composable
fun ErrorText(text: String) {
    Text(
        text,
        color = CCTheme.colors.primaryRed,
        style = CCTheme.typography.common_text_small_regular,
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun PlaceholderText(text: String) {
    Text(
        text,
        modifier = Modifier,
        style = CCTheme.typography.common_text_regular,
        color = CCTheme.colors.grayOne
    )
}


@Composable
fun InformationBoxContent(
    text: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    onButtonClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .background(CCTheme.colors.white, CircleShape)
            .border(1.dp, CCTheme.colors.grayOne, CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = CCTheme.colors.black),
                enabled = !text.contains("+")
            ) {
                if (!text.contains("+")) onButtonClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CCTheme.typography.common_text_small_medium,
            modifier = textModifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun ProfileRow(
    title: String,
    value: String = "",
    titleColor: Color = CCTheme.colors.black,
    valueColor: Color = CCTheme.colors.grayOne,
    needDivider: Boolean = true,
    needRow: Boolean = true,
    rowClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(CCTheme.colors.white)
            .clickable { rowClick.invoke() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = title,
                color = titleColor,
                style = CCTheme.typography.common_medium_text_regular
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value,
                color = valueColor,
                style = CCTheme.typography.common_medium_text_regular,
                fontWeight = FontWeight.W400
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (needRow) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_row_arrow),
                    contentDescription = null,
                    tint = CCTheme.colors.grayOne
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        if (needDivider) RowDividerGrayThree()
    }
}