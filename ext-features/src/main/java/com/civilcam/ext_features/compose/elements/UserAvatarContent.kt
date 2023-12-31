package com.civilcam.ext_features.compose.elements

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.civilcam.ext_features.theme.CCTheme


@Composable
fun CircleUserAvatar(avatar: Int, avatarSize: Int) {
    Box(
        modifier = Modifier
            .size(avatarSize.dp)
            .border(1.dp, CCTheme.colors.white, CircleShape),
    ) {
        Image(
            painter = painterResource(id = avatar), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
        )
    }

}

@Composable
fun CircleUserAvatar(avatar: Uri, avatarSize: Int) {
    Box(
        modifier = Modifier
            .size(avatarSize.dp)
            .border(1.dp, CCTheme.colors.white, CircleShape),
    ) {
        Image(
            painter = rememberImagePainter(data = avatar), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
        )
    }
}


@Composable
fun CircleUserAvatar(avatar: String, avatarSize: Int) {
    Box(
        modifier = Modifier
            .size(avatarSize.dp)
            .border(1.dp, CCTheme.colors.white, CircleShape),
    ) {
        Image(
            painter = rememberImagePainter(data = avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
        )
    }
}