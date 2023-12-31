package com.civilcam.ui.network.main.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.HeaderTitleText
import com.civilcam.ext_features.theme.CCTheme


@Composable
fun GuardRequestRowSection(
    requestList: List<GuardianItem>,
    clickToRequest: () -> Unit
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        HeaderTitleText(stringResource(id = R.string.network_main_requests))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = CCTheme.colors.black),
                ) { clickToRequest.invoke() }
                .background(CCTheme.colors.white),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            when (requestList.size) {
                1 -> {
                    requestList.first().guardianAvatar?.imageUrl?.let {
                        CircleUserAvatar(avatar = it, avatarSize = 36)
                    }
                }
                else -> {
                    Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
                        if (requestList.size > 2) {
                            Box(
                                modifier = Modifier.size(36.dp),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                requestList[2].guardianAvatar?.imageUrl?.let {
                                    CircleUserAvatar(
                                        avatar = it,
                                        avatarSize = 24
                                    )
                                }

                            }
                        }
                        Box(
                            modifier = Modifier.size(36.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            requestList[1].guardianAvatar?.imageUrl?.let {
                                CircleUserAvatar(
                                    avatar = it,
                                    avatarSize = 24
                                )
                            }

                        }
                        Box(
                            modifier = Modifier.size(36.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            requestList[0].guardianAvatar?.imageUrl?.let {
                                CircleUserAvatar(
                                    avatar = it,
                                    avatarSize = 24
                                )
                            }

                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                stringResource(id = R.string.network_main_new_requests),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 14.dp)
            )
            Box(
                modifier = Modifier
                    .background(CCTheme.colors.primaryRed, CircleShape)
                    .size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = requestList.size.toString(),
                    style = CCTheme.typography.notification,

                    )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_nav_forward),
                tint = CCTheme.colors.grayOne,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}