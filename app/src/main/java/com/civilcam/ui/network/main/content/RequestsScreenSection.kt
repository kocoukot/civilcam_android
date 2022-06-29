package com.civilcam.ui.network.main.content

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.CircleUserAvatar
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.common.compose.InformationRow
import com.civilcam.ui.network.main.model.GuardianItem

@Composable
fun RequestsScreenSection(
    guardRequestsList: List<GuardianItem>,
    clickRequest: (GuardianItem, Boolean) -> Unit
) {

    Column {
        Divider(color = CCTheme.colors.grayThree)
        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = CCTheme.colors.grayThree)


        for ((index, guard) in guardRequestsList.withIndex()) {
            InformationRow(
                title = guard.guardianName,
                titleFont = FontFamily(Font(R.font.roboto_regular)),
                needDivider = index < guardRequestsList.lastIndex,
                isClickable = false,
                leadingIcon = {
                    CircleUserAvatar(
                        avatar = guard.guardianAvatar,
                        avatarSize = 36,
                    )
                },
                trailingIcon = {
                    RequestAnswers {
                        clickRequest.invoke(guard, it)
                    }
                },
            ) {}
        }
        Divider(color = CCTheme.colors.grayThree)

    }
}

@Composable
private fun RequestAnswers(
    isAccepted: (Boolean) -> Unit
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
                .border(2.dp, CCTheme.colors.primaryRed, RoundedCornerShape(50))
                .size(24.dp),
            tint = CCTheme.colors.primaryRed,
        ) {
            isAccepted.invoke(false)
        }

        Spacer(modifier = Modifier.width(8.dp))
        IconActionButton(
            buttonIcon = R.drawable.ic_check,
            modifier = Modifier
                .border(2.dp, CCTheme.colors.primaryGreen, RoundedCornerShape(50))
                .size(24.dp),
            tint = CCTheme.colors.primaryGreen,
        ) {
            isAccepted.invoke(true)
        }
    }

}