package com.civilcam.ext_features.compose.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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

