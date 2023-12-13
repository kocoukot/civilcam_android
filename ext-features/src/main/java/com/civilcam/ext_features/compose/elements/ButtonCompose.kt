package com.civilcam.ext_features.compose.elements

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.civilcam.ext_features.R
import com.civilcam.ext_features.theme.CCTheme


@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun ComposeButton(
    title: String,
    modifier: Modifier = Modifier,
    textFontWeight: FontWeight = FontWeight.W600,
    textStyle: TextStyle = CCTheme.typography.button_text,
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

            Box(
               modifier = Modifier
            )
            ButtonDefaults.buttonColors()
            Button(
                enabled = isActivated,
                border = BorderStroke(1.dp, borderColor),
                onClick = { buttonClick.invoke() },
                modifier = modifier.fillMaxWidth(),
                elevation = ButtonDefaults.elevation(0.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = backgroundColor.value,
                    disabledBackgroundColor = Color.White
                ),
            )
            {
                Crossfade(targetState = title) {
                    Text(
                        title,
                        style = textStyle,
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
    modifier: Modifier = Modifier,
    textColor: Color = CCTheme.colors.primaryRed,
    textFont: FontFamily = FontFamily(Font(R.font.roboto_medium)),
    actionAction: () -> Unit,
) {
    TextButton(
        enabled = isEnabled,
        onClick = actionAction,
        modifier = modifier
    ) {
        Text(
            text = actionTitle,
            color = if (isEnabled) textColor else CCTheme.colors.grayOne,
            style = CCTheme.typography.common_text_medium,
            fontFamily = textFont
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
fun BackButton(
    modifier: Modifier = Modifier,
    tint: Color = CCTheme.colors.black,
    navigationAction: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = navigationAction
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back_navigation),
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun AvatarButton(buttonClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.img_avatar),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .clickable {
                buttonClick.invoke()
            }
    )
}

@Composable
fun AvatarButton(url: String?, buttonClick: () -> Unit) {
    url?.let {
        Image(
            painter = rememberImagePainter(data = url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .clickable {
                    buttonClick.invoke()
                }
        )
    }
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

@Preview
@Composable
private fun TextActionButtonPreview() {
    TextActionButton(
        actionTitle = "Title",
    ) {}
}

@Preview
@Composable
private fun IconActionButtonPreview() {
    IconActionButton(
        buttonIcon = R.drawable.img_avatar,
    ) {}
}

@Preview
@Composable
private fun BackButtonPreview() {
    BackButton {}
}

@Preview
@Composable
private fun AvatarButtonPreview() {
    AvatarButton {}
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