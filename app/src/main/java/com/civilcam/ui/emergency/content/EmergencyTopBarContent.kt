package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.AvatarButton
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyScreen


@Composable
fun EmergencyTopBarContent(
    modifier: Modifier = Modifier,
    onClick: (EmergencyActions) -> Unit,
    locationData: String,
    screen: EmergencyScreen
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(88.dp)
        ) {
            AnimatedVisibility(visible = screen == EmergencyScreen.NORMAL) {
                AvatarButton {
                    onClick.invoke(EmergencyActions.GoUserProfile)
                }
            }
            IconActionButton(
                buttonIcon = R.drawable.ic_location_pin,
                buttonClick = { onClick.invoke(EmergencyActions.DetectLocation) },
                tint = CCTheme.colors.primaryRed,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = CCTheme.colors.white)
                    .size(28.dp)
            )
        }

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
                modifier = Modifier.padding(
                    start = 4.dp,
                    end = 15.dp,
                    top = 7.dp,
                    bottom = 7.dp
                ),
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 1
            )
        }

        Crossfade(targetState = screen) { targetState ->
            when (targetState) {
                EmergencyScreen.NORMAL -> {
                    IconActionButton(
                        buttonIcon = R.drawable.ic_settings,
                        buttonClick = { onClick.invoke(EmergencyActions.GoSettings) },
                        tint = CCTheme.colors.primaryRed,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = CCTheme.colors.white)
                            .size(28.dp)
                    )
                }
                else -> {
                    IconActionButton(
                        buttonIcon = if (targetState == EmergencyScreen.COUPLED)
                            R.drawable.ic_map_extended
                        else
                            R.drawable.ic_map_minimize,
                        buttonClick = {
                            onClick.invoke(
                                if (targetState == EmergencyScreen.COUPLED)
                                    EmergencyActions.ClickChangeScreen(EmergencyScreen.MAP_EXTENDED)
                                else
                                    EmergencyActions.ClickChangeScreen(EmergencyScreen.COUPLED)
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
}