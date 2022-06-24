package com.civilcam.ui.common.compose

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter


//todo fix after api
@Composable
fun CircleUserAvatar(avatar: Int, avatarSize: Int) {
    Image(
        painter = painterResource(id = avatar), contentDescription = null,
        modifier = Modifier
            .size(avatarSize.dp)
            .clip(CircleShape)
    )
}

@Composable
fun CircleUserAvatar(avatar: Uri, avatarSize: Int) {
    Image(
        painter = rememberImagePainter(data = avatar), contentDescription = null,
        modifier = Modifier
            .size(avatarSize.dp)
            .clip(CircleShape)
    )
}