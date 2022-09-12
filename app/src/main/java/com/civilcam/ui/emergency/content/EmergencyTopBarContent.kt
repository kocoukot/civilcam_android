package com.civilcam.ui.emergency.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.compose.elements.AvatarButton
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.emergency.model.EmergencyActions


@Composable
fun EmergencyTopBarContent(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    onClick: (EmergencyActions) -> Unit,
    locationDetectContent: (@Composable () -> Unit),
    locationDataContent: (@Composable () -> Unit),
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
                AvatarButton(avatarUrl) {
                    onClick.invoke(EmergencyActions.GoUserProfile)
                }
            }
            locationDetectContent()
        }

        locationDataContent()


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

