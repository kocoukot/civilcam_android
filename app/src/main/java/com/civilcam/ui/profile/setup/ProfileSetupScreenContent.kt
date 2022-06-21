@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.profile.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.InputField
import com.civilcam.ui.common.compose.PhoneInputField
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.profile.setup.content.AvatarContent
import com.civilcam.ui.profile.setup.content.CalendarIcon
import com.civilcam.ui.profile.setup.model.InputDataType
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.utils.DateUtils
import com.google.accompanist.pager.ExperimentalPagerApi
import timber.log.Timber

@Composable
fun ProfileSetupScreenContent(viewModel: ProfileSetupViewModel) {


    val state = viewModel.state.collectAsState()

    Scaffold(
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.profile_setup_title),
                    navigationTitle = stringResource(R.string.back_text),
                    navigationAction = {
                        viewModel.setInputActions(ProfileSetupActions.CLickGoBack)
                    },
                )
            }
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {

                item {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        val avatar = state.value.data?.profileImage
                        AvatarContent(avatar) {
                            viewModel.setInputActions(ProfileSetupActions.ClickAvatarSelect)
                        }


                        InputField(
                            title = stringResource(id = R.string.profile_setup_first_name_label),
                            placeHolder = stringResource(id = R.string.profile_setup_first_name_placeholder)
                        ) {
                            viewModel.setInputActions(
                                ProfileSetupActions.EnterInputData(
                                    InputDataType.FIRST_NAME,
                                    it
                                )
                            )
                        }

                        InputField(
                            title = stringResource(id = R.string.profile_setup_last_name_label),
                            placeHolder = stringResource(id = R.string.profile_setup_last_name_placeholder)
                        ) {
                            viewModel.setInputActions(
                                ProfileSetupActions.EnterInputData(
                                    InputDataType.FIRST_NAME,
                                    it
                                )
                            )
                        }


                        var dateOfBirth by remember { mutableStateOf("") }
                        dateOfBirth = state.value.birthDate?.let {
                            DateUtils.dateOfBirthFormat(it)
                        } ?: ""
                        Timber.d("getDateFromCalendar dateOfBirth $dateOfBirth ")

                        val calendarColor =
                            animateColorAsState(targetValue = if (dateOfBirth.isEmpty()) CCTheme.colors.grayText else CCTheme.colors.primaryRed)
                        InputField(
                            isEnable = false,
                            text = dateOfBirth,
                            trailingIcon = { CalendarIcon(calendarColor.value) },
                            title = stringResource(id = R.string.profile_setup_date_of_birth_label),
                            placeHolder = stringResource(id = R.string.profile_setup_date_of_birth_placeholder),
                            onTextClicked = {
                                viewModel.setInputActions(ProfileSetupActions.ClickDateSelect)
                            },
                            onValueChanged = {},
                        )

                        InputField(
                            isEnable = false,
                            title = stringResource(id = R.string.profile_setup_address_label),
                            placeHolder = stringResource(id = R.string.profile_setup_address_placeholder)
                        ) {

                        }


                        PhoneInputField {
                            viewModel.setInputActions(
                                ProfileSetupActions.EnterInputData(
                                    InputDataType.PHONE_NUMBER,
                                    it
                                )
                            )
                        }
                    }
                }

                item {
                    Column {
                        ComposeButton(
                            title = stringResource(id = R.string.continue_text),
                            Modifier.padding(horizontal = 8.dp),
                            isActivated = state.value.data?.isFilled ?: false,
                            buttonClick = {
                                viewModel.setInputActions(ProfileSetupActions.ClickSave)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

            }

        }
    }
}