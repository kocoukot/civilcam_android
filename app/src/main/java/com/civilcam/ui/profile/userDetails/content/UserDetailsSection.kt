package com.civilcam.ui.profile.userDetails.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel
import com.civilcam.utils.DateUtils

@Composable
fun UserDetailsSection(
    userData: UserDetailsModel,
    myGuardenceChange: () -> Unit,
    onStopGuarding: () -> Unit,
    mockAction: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_avatar),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    mockAction.invoke()
                }
        )

        Text(
            text = userData.userInfoSection.userName,
            style = CCTheme.typography.button_text,
            color = CCTheme.colors.black,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        AdditionalInfo(DateUtils.dateOfBirthFormat(userData.userInfoSection.dateOfBirth))

        if (userData.isMyGuard || userData.guardRequest?.isGuarding == true) {
            AdditionalInfo(
                userData.userInfoSection.address,
                Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InformationBoxContent(
                    text = userData.userInfoSection.phoneNumber,
                    modifier = Modifier.weight(1f),
                )

                if (userData.guardRequest?.isGuarding == true) {
                    InformationBoxContent(
                        text = stringResource(id = R.string.user_details_stop_guarding),
                        modifier = Modifier.weight(1f)
                    ) { onStopGuarding.invoke() }
                }

            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))

        }


        val buttonTitle =
            if (userData.isMyGuard) stringResource(id = R.string.user_details_remove_gaurdian) else stringResource(
                id = R.string.user_details_ask_to_guard
            )
        ComposeButton(
            title = buttonTitle,
            modifier = Modifier.padding(horizontal = 16.dp),
            textFontWeight = FontWeight.W500,
            buttonClick = myGuardenceChange
        )
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = CCTheme.colors.grayThree)
    }
}

@Composable
private fun AdditionalInfo(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = CCTheme.typography.common_text_medium,
        color = CCTheme.colors.grayOne,
    )
}

@Composable
private fun InformationBoxContent(
    text: String,
    modifier: Modifier = Modifier,
    onButtonClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .background(CCTheme.colors.white, RoundedCornerShape(50))
            .border(1.dp, CCTheme.colors.grayOne, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = CCTheme.colors.black),
                enabled = !text.contains("+")
            ) {
                if (!text.contains("+")) onButtonClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = CCTheme.typography.common_text_small_medium,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}