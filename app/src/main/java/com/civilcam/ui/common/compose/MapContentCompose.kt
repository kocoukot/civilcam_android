package com.civilcam.ui.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun LocationData(locationData: String) {
    Row(
        modifier = Modifier
            .background(CCTheme.colors.white, CircleShape)
            .fillMaxWidth(0.65f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_map_pin),
            contentDescription = null,
            modifier = Modifier.padding(start = 4.dp)
        )

        Text(
            text = locationData,
            style = CCTheme.typography.common_text_small_regular,
            color = CCTheme.colors.black,
            modifier = Modifier
                .padding(start = 4.dp, end = 15.dp)
                .padding(vertical = 6.dp),
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            maxLines = 1
        )
    }
}


@Composable
fun LocationDetectButton(
    isAllowed: Boolean = false,
    onDetectLocation: () -> Unit
) {
    IconActionButton(
        buttonIcon = R.drawable.ic_location_pin,
        buttonClick = onDetectLocation,
        tint = if (isAllowed) CCTheme.colors.primaryRed else CCTheme.colors.primaryRed40,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = CCTheme.colors.white)
            .size(28.dp)
    )
}