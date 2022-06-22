package com.civilcam.ui.common.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme


@Composable
fun ComposeButton(
    title: String,
    modifier: Modifier = Modifier,
    textFontWeight: FontWeight = FontWeight.W600,
    isActivated: Boolean = true,
    buttonClick: () -> Unit
) {
    val backgroundColor =
        animateColorAsState(targetValue = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.lightGray)
    val borderColor = if (isActivated) CCTheme.colors.primaryRed else CCTheme.colors.grayOne
    val textColor = if (isActivated) CCTheme.colors.white else CCTheme.colors.grayOne

    MaterialTheme {
        CompositionLocalProvider(
            LocalRippleTheme provides SecondaryRippleTheme
        ) {
            Button(
                enabled = isActivated,
                border = BorderStroke(1.dp, borderColor),
                onClick = { buttonClick.invoke() },
                modifier = modifier.fillMaxWidth(),
                elevation = ButtonDefaults.elevation(0.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor.value,
                    disabledBackgroundColor = Color.Transparent
                ),
            )
            {
                Text(
                    title,
                    style = CCTheme.typography.button_text,
                    fontWeight = textFontWeight,
                    color = textColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TextActionButton(
    actionTitle: String,
    actionAction: () -> Unit
) {
    TextButton(onClick = actionAction, modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = actionTitle,
            color = CCTheme.colors.primaryRed,
            style = CCTheme.typography.common_text_medium
        )
    }
}

@Preview
@Composable
fun ComposeButtonPreview() {
    ComposeButton(
        title = "Title",
        modifier = Modifier,
        isActivated = false,
        buttonClick = {},
    )
}

@Immutable
private object SecondaryRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = Color.White,
        lightTheme = MaterialTheme.colors.isLight
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = Color.White,
        lightTheme = MaterialTheme.colors.isLight
    )
}