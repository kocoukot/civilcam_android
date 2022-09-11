package com.civilcam.ext_features.compose.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun DividerLightGray() {
    Divider(
        color = CCTheme.colors.lightGray,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
}

@Composable
fun RowDivider() {
    Divider(
        color = CCTheme.colors.grayThree,
        modifier = Modifier
    )
}

@Composable
fun RowDividerGrayThree(padding: Int = 16) {
    Divider(
        color = CCTheme.colors.grayThree,
        modifier = Modifier.padding(start = padding.dp)
    )
}