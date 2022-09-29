package com.civilcam.alert_feature.map.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.model.LiveMapActions
import com.civilcam.domainLayer.model.alerts.AlertGuardianModel
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun UserInformationContent(
    modifier: Modifier = Modifier,
    userInformation: AlertGuardianModel,
    onAction: (LiveMapActions) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        userInformation.avatar.imageUrl?.let { avatar ->
            CircleUserAvatar(avatar = avatar, avatarSize = 34)
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(userInformation.fullName, style = CCTheme.typography.common_text_small_medium)
            Text(
                stringResource(
                    id = R.string.alert_user_detail_request_sent,
                    "" //todo fix add date after added to socket
                ),
                color = CCTheme.colors.grayOne,
                style = CCTheme.typography.common_text_regular_12
            )
        }

        IconActionButton(
            modifier = Modifier
                .background(CCTheme.colors.primaryGreen, CircleShape)
                .size(32.dp),
            buttonIcon = R.drawable.ic_phone_call,
            tint = CCTheme.colors.white
        ) {
            onAction.invoke(LiveMapActions.ClickCallUserPhone)
        }
    }
}