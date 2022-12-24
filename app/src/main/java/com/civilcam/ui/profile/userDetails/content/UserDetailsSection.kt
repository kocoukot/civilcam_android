package com.civilcam.ui.profile.userDetails.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.ComposeButton
import com.civilcam.ext_features.compose.elements.InformationBoxContent
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.ext.serverPhoneNumberFormat
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import timber.log.Timber

@Composable
fun UserDetailsSection(
    userData: PersonModel,
    myGuardenceChange: (UserDetailsActions) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userData.personAvatar?.imageUrl?.let { avatar ->
            CircleUserAvatar(
                avatar = avatar,
                avatarSize = 120,
            )
        }

        Text(
            text = userData.personFullName,
            style = CCTheme.typography.button_text,
            color = CCTheme.colors.black,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        userData.personBirth?.let { dob ->
            AdditionalInfo(
                DateUtils.dateOfBirthFormat(dob)
            )
        }

        if (userData.isOnGuard == true || userData.isGuardian) {
            userData.personAddress?.let { address ->
                AdditionalInfo(
                    address,
                    Modifier.padding(top = 4.dp, bottom = 16.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                userData.personPhone?.let { phone ->
                        InformationBoxContent(
                            text = phone.serverPhoneNumberFormat(),
                            modifier = Modifier.weight(1f),
                        )
                }

                if (userData.isOnGuard == true) {
                    InformationBoxContent(
                        text = stringResource(id = R.string.user_details_stop_guarding),
                        modifier = Modifier.weight(1f)
                    ) {
                        myGuardenceChange.invoke(
                            UserDetailsActions.ClickShowAlert(
                                StopGuardAlertType.STOP_GUARDING
                            )
                        )
                    }
                }

            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Timber.i("buttonTitle ${userData.outputRequest}")

        val buttonTitle =
            when (userData.outputRequest?.status) {
                GuardianStatus.PENDING -> stringResource(id = R.string.pending_text)
                GuardianStatus.ACCEPTED -> stringResource(id = R.string.user_details_remove_gaurdian)
                else -> stringResource(id = R.string.user_details_ask_to_guard)
            }


        userData.outputRequest?.status?.takeIf { it != GuardianStatus.DECLINED }?.let {
            ComposeButton(
                title = buttonTitle,
                modifier = Modifier.padding(horizontal = 16.dp),
                textFontWeight = FontWeight.W500,
                isActivated = userData.outputRequest?.status != GuardianStatus.PENDING,
                buttonClick = {
                    myGuardenceChange.invoke(
                        UserDetailsActions.ClickShowAlert(StopGuardAlertType.REMOVE_GUARDIAN)
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        RowDivider()
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
