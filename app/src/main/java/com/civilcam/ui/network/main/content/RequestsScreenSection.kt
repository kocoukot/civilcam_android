package com.civilcam.ui.network.main.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.IconActionButton
import com.civilcam.ext_features.compose.elements.InformationRow
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.network.main.model.NetworkMainActions

@Composable
fun RequestsScreenSection(
    guardRequestsList: List<GuardianItem>,
    clickRequest: (NetworkMainActions) -> Unit
) {

    Column {
        RowDivider()
        Spacer(modifier = Modifier.height(32.dp))
        RowDivider()


        for ((index, guard) in guardRequestsList.withIndex()) {
            InformationRow(
                title = guard.guardianName,
                titleFont = FontFamily(Font(R.font.roboto_regular)),
                needDivider = index < guardRequestsList.lastIndex,
                leadingIcon = {
                    guard.guardianAvatar?.imageUrl?.let {
                        CircleUserAvatar(
                            avatar = it,
                            avatarSize = 36,
                        )
                    }
                },
                trailingIcon = {
                    RequestAnswers {
                        clickRequest.invoke(NetworkMainActions.SetRequestReaction(guard, it))
                    }
                },
                rowClick = { clickRequest.invoke(NetworkMainActions.ClickUser(guard)) }
            )
        }
        if (guardRequestsList.isNotEmpty()) RowDivider()

    }
}

@Composable
private fun RequestAnswers(
    isAccepted: (ButtonAnswer) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconActionButton(
            buttonIcon = R.drawable.ic_deny,
            modifier = Modifier
                .border(2.dp, CCTheme.colors.primaryRed, CircleShape)
                .size(24.dp),
            tint = CCTheme.colors.primaryRed,
        ) {
            isAccepted.invoke(ButtonAnswer.DECLINE)
        }

        Spacer(modifier = Modifier.width(8.dp))
        IconActionButton(
            buttonIcon = R.drawable.ic_check,
            modifier = Modifier
                .border(2.dp, CCTheme.colors.primaryGreen, CircleShape)
                .size(24.dp),
            tint = CCTheme.colors.primaryGreen,
        ) {
            isAccepted.invoke(ButtonAnswer.ACCEPT)
        }
    }

}