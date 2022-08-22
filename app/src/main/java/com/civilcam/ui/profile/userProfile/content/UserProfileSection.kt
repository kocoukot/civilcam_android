package com.civilcam.ui.profile.userProfile.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.profile.userProfile.model.UserProfileActions
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen
import com.civilcam.utils.DateUtils
import timber.log.Timber

@OptIn(ExperimentalCoilApi::class)
@Composable
fun UserProfileSection(
    userData: CurrentUser,
    screenType: UserProfileScreen,
    isSaveEnabled: Boolean,
    onActionClick: (UserProfileActions) -> Unit,
    mockAction: () -> Unit
) {

    Timber.i("userData ${userData.userBaseInfo}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val avatarPainter = rememberImagePainter(data = userData.userBaseInfo.avatar?.imageUrl)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(modifier = Modifier.padding(start = 8.dp)) {
                onActionClick.invoke(
                    UserProfileActions.GoBack
                )
            }
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable(enabled = screenType == UserProfileScreen.EDIT) {
                        mockAction.invoke()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (avatarPainter.state is ImagePainter.State.Loading) {
                    CircularProgressIndicator(color = CCTheme.colors.primaryRed)
                } else {
                    Image(
                        contentScale = if (userData.userBaseInfo.avatar?.imageUrl == null) ContentScale.Fit else ContentScale.Crop,
                        painter =
                        if (userData.userBaseInfo.avatar?.imageUrl == null)
                            painterResource(id = R.drawable.img_avatar)
                        else
                            avatarPainter,
                        contentDescription = null,
                        modifier = Modifier,
                    )
                }
            }


            TextActionButton(
                isEnabled = if (screenType == UserProfileScreen.PROFILE) true else isSaveEnabled,
                actionTitle = when (screenType) {
                    UserProfileScreen.PROFILE -> stringResource(id = R.string.user_profile_edit_title)
                    UserProfileScreen.EDIT -> stringResource(id = R.string.save_text)
                    UserProfileScreen.LOCATION -> stringResource(id = R.string.save_text)
                }
            ) {
                onActionClick.invoke(if (screenType == UserProfileScreen.EDIT) UserProfileActions.ClickSave else UserProfileActions.ClickEdit)
            }
        }

        val dateOfBirth = userData.userBaseInfo.dob.takeIf { it.isNotEmpty() }
            ?.let { DateUtils.dateOfBirthFormat(it) }
            .orEmpty()

        AnimatedVisibility(visible = screenType == UserProfileScreen.PROFILE) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${userData.userBaseInfo.firstName} ${userData.userBaseInfo.lastName}",
                    style = CCTheme.typography.button_text,
                    color = CCTheme.colors.black,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                AdditionalInfo(dateOfBirth)

                Spacer(modifier = Modifier.height(4.dp))

                AdditionalInfo(userData.userBaseInfo.address)
            }
        }

        AnimatedVisibility(visible = screenType == UserProfileScreen.EDIT) {
            Text(
                text = stringResource(id = R.string.user_profile_change_profile_image_title),
                color = CCTheme.colors.primaryRed,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { mockAction.invoke() },
                fontWeight = FontWeight.W500,
                fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RowDivider()
    }
}

@Composable
private fun AdditionalInfo(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        text = text,
        textAlign = TextAlign.Center,
        style = CCTheme.typography.common_text_regular,
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
            .background(CCTheme.colors.white, CircleShape)
            .border(1.dp, CCTheme.colors.grayOne, CircleShape)
            .clip(CircleShape)
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