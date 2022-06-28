package com.civilcam.ui.common.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme

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