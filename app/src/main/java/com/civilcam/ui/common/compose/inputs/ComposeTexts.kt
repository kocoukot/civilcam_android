package com.civilcam.ui.common.compose.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme

@Composable
fun EmptyListText(text: String) {
    Text(
        text,
        textAlign = TextAlign.Center,
        style = CCTheme.typography.common_text_regular,
        modifier = Modifier
            .background(CCTheme.colors.white, RoundedCornerShape(50))
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

