package com.civilcam.ui.alerts.map.content

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.map.model.LiveMapActions
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.emergency.model.EmergencyScreen

@Composable
fun AlertMapScreenContent(
    modifier: Modifier = Modifier,
    alertScreenState: EmergencyScreen,
    onActionClick: (LiveMapActions) -> Unit
) {

    val topBarPadding by animateDpAsState(
        targetValue = if (alertScreenState == EmergencyScreen.COUPLED) 16.dp else 68.dp,
        animationSpec = tween(delayMillis = 250)
    )
    Box(modifier = modifier.background(CCTheme.colors.grayOne)) {

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = topBarPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconActionButton(
                buttonIcon = R.drawable.ic_location_pin,
                buttonClick = { onActionClick.invoke(LiveMapActions.ClickDetectLocation) },
                tint = CCTheme.colors.primaryRed,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = CCTheme.colors.white)
                    .size(28.dp)
            )


            Row(
                modifier = Modifier
                    .background(CCTheme.colors.white, CircleShape)
                    .width(160.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_map_pin),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Text(
                    text = "locationData locationData",
                    style = CCTheme.typography.common_text_small_regular,
                    color = CCTheme.colors.black,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(start = 6.dp, end = 15.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }


            Crossfade(targetState = alertScreenState) { state ->
                IconActionButton(
                    buttonIcon = if (state == EmergencyScreen.COUPLED)
                        R.drawable.ic_map_extended
                    else
                        R.drawable.ic_map_minimize,
                    buttonClick = {
                        onActionClick.invoke(
                            if (state == EmergencyScreen.COUPLED)
                                LiveMapActions.ClickScreenChange(EmergencyScreen.MAP_EXTENDED)
                            else
                                LiveMapActions.ClickScreenChange(EmergencyScreen.COUPLED)
                        )
                    },
                    tint = CCTheme.colors.primaryRed,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = CCTheme.colors.white)
                        .size(28.dp)
                )

            }
        }
    }
}