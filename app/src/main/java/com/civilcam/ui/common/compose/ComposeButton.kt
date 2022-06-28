package com.civilcam.ui.common.compose

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.civilcam.R
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
                    disabledBackgroundColor = Color.White
                ),
            )
            {
                Crossfade(targetState = title) {
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
}

@Composable
fun TextActionButton(
    actionTitle: String,
    isEnabled: Boolean = true,
    textColor: Color = CCTheme.colors.primaryRed,
    actionAction: () -> Unit
) {
    TextButton(
        enabled = isEnabled,
        onClick = actionAction,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = actionTitle,
            color = if (isEnabled) textColor else CCTheme.colors.grayOne,
            style = CCTheme.typography.common_text_medium
        )
    }
}

@Composable
fun IconActionButton(
    buttonIcon: Int,
    modifier: Modifier = Modifier,
    tint: Color = CCTheme.colors.primaryRed,
    buttonClick: () -> Unit
) {
    IconButton(onClick = buttonClick, modifier = modifier) {
        Icon(
            painter = painterResource(id = buttonIcon),
            contentDescription = null,
            tint = tint
        )
    }
}


@Composable
fun BackButton(navigationAction: () -> Unit) {
    IconButton(
        modifier = Modifier,
        onClick = navigationAction
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_navigation),
            contentDescription = null,
            tint = CCTheme.colors.black
        )
    }
}

@Composable
fun AvatarButton(buttonClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.img_avatar),
        contentDescription = null,
        modifier = Modifier
            .offset(x = 8.dp)
            .size(28.dp)
            .clip(CircleShape)
            .clickable {
                buttonClick.invoke()
            }
    )
}

@Preview
@Composable
private fun ComposeButtonPreview() {
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