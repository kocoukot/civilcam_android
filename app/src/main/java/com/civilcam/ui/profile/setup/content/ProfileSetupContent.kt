package com.civilcam.ui.profile.setup.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.PictureModel
import com.civilcam.domainLayer.model.UserSetupModel
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.inputs.InputField
import com.civilcam.ui.common.compose.inputs.PhoneInputField
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.utils.DateUtils
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ProfileSetupContent(
    avatar: PictureModel?,
    data: UserSetupModel?,
    birthDate: Long?,
    setupAction: (ProfileSetupActions) -> Unit
) {
    val listState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var dateOfBirth by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(listState)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(32.dp))
            AvatarContent(avatar) {
                setupAction(ProfileSetupActions.ClickAvatarSelect)
            }


            InputField(
                text = data?.firstName.orEmpty(),
                title = stringResource(id = R.string.profile_setup_first_name_label),
                placeHolder = stringResource(id = R.string.profile_setup_first_name_placeholder),
            ) {
                setupAction(
                    ProfileSetupActions.EnterInputData(
                        UserInfoDataType.FIRST_NAME,
                        it
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            InputField(
                text = data?.lastName.orEmpty(),
                title = stringResource(id = R.string.profile_setup_last_name_label),
                placeHolder = stringResource(id = R.string.profile_setup_last_name_placeholder),
            ) {
                setupAction(
                    ProfileSetupActions.EnterInputData(
                        UserInfoDataType.LAST_NAME,
                        it
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            dateOfBirth = birthDate
                ?.takeIf {
                    it > 0
                }?.let {
                    DateUtils.dateOfBirthFormat(it)
                }.orEmpty()
            Timber.d("getDateFromCalendar dateOfBirth $dateOfBirth birthDate $birthDate")

            val calendarColor =
                animateColorAsState(targetValue = if (dateOfBirth.isEmpty()) CCTheme.colors.grayOne else CCTheme.colors.primaryRed)
            InputField(
                isEnable = false,
                text = dateOfBirth,
                trailingIcon = { CalendarIcon(calendarColor.value) },
                title = stringResource(id = R.string.profile_setup_date_of_birth_label),
                placeHolder = stringResource(id = R.string.profile_setup_date_of_birth_placeholder),
                onTextClicked = {
                    setupAction(ProfileSetupActions.ClickDateSelect)
                },
                onValueChanged = {},
            )
            Spacer(modifier = Modifier.height(16.dp))

            InputField(
                text = data?.location.orEmpty(),
                isEnable = false,
                title = stringResource(id = R.string.profile_setup_address_label),
                placeHolder = stringResource(id = R.string.profile_setup_address_placeholder),
                onTextClicked = {
                    setupAction(ProfileSetupActions.ClickGoLocationPicker)
                },
                onValueChanged = {},
            )
            Spacer(modifier = Modifier.height(16.dp))

            PhoneInputField(
                text = data?.phoneNumber.orEmpty(),
                onValueChanged = {
                    setupAction(
                        ProfileSetupActions.EnterInputData(
                            UserInfoDataType.PHONE_NUMBER,
                            it
                        )
                    )
                },
                isInFocus = {
                    coroutineScope.launch {
                        listState.scrollTo(1000)
                    }
                }
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            ComposeButton(
                title = stringResource(id = R.string.save_text),
                Modifier.padding(horizontal = 8.dp),
                isActivated = data?.isFilled ?: false,
                buttonClick = {
                    setupAction(ProfileSetupActions.ClickSave)
                }
            )
        }
    }
}