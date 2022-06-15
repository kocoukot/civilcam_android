package com.civilcam.ui.common.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme

@Composable
fun ComposeButton(
    title: String,
    modifier: Modifier = Modifier,
    buttonClick: () -> Unit
) {

    Button(
        onClick = { buttonClick.invoke() },
        modifier = modifier,
        elevation = ButtonDefaults.elevation(0.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = CCTheme.colors.primaryRed),
    ) {

        Text(
            title, style = CCTheme.typography.button_text, color = CCTheme.colors.white,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

}