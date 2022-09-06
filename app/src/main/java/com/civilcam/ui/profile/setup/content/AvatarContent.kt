package com.civilcam.ui.profile.setup.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.PictureModel


@Composable
fun AvatarContent(avatar: PictureModel? = null, onChangeAvatar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val avatarText =
            stringResource(
                id = if (avatar == null) R.string.profile_setup_upload_photo else
                    R.string.profile_setup_change_photo
            )
        val stroke = Stroke(
            width = 3f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    onChangeAvatar.invoke()
                },
            contentAlignment = Alignment.Center,
        ) {

            if (avatar == null) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(style = stroke, color = Color(0xFF9C9C9C))
                }
            }

            Image(
                contentScale = if (avatar == null) ContentScale.Fit else ContentScale.Crop,
                painter =
                if (avatar == null) {
                    painterResource(id = R.drawable.ic_avatar_placeholder)
                } else {
                    rememberImagePainter(data = avatar.uri)
                },
                contentDescription = null,
                modifier = Modifier.clickable {
                    onChangeAvatar.invoke()
                }
            )
        }
        Text(
            avatarText,
            modifier = Modifier
                .padding(top = 12.dp)
                .clickable {
                    onChangeAvatar.invoke()
                },
            style = CCTheme.typography.common_text_small_medium,
            color = CCTheme.colors.primaryRed

        )
    }
}


@Composable
fun CalendarIcon(tintColor: Color) =

    Icon(
        painter = painterResource(id = R.drawable.ic_calendar),
        contentDescription = null,
        tint = tintColor,
    )


@Preview
@Composable
fun AvatarContentPreview() {
    AvatarContent {

    }
}