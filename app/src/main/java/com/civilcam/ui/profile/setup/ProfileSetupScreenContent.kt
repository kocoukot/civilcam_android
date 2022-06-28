package com.civilcam.ui.profile.setup

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.DividerLightGray
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.InputField
import com.civilcam.ui.common.compose.inputs.PhoneInputField
import com.civilcam.ui.profile.setup.content.AvatarContent
import com.civilcam.ui.profile.setup.content.CalendarIcon
import com.civilcam.ui.profile.setup.content.DatePickerContent
import com.civilcam.ui.profile.setup.model.InputDataType
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.utils.DateUtils
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ProfileSetupScreenContent(viewModel: ProfileSetupViewModel) {


    val state = viewModel.state.collectAsState()
    val listState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var dateOfBirth by remember { mutableStateOf("") }




    if (state.value.showDatePicker) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            ), onDismissRequest = {
                viewModel.setInputActions(ProfileSetupActions.ClickCloseDatePicker)
            }) {
            DatePickerContent(
                onClosePicker = {
                    viewModel.setInputActions(ProfileSetupActions.ClickCloseDatePicker)
                },
                onSelectDate = {
                    viewModel.setInputActions(ProfileSetupActions.ClickSelectDate(it))

                },
            )
        }
    }

    Scaffold(
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.profile_setup_title),
                    navigationAction = {
                        viewModel.setInputActions(ProfileSetupActions.ClickGoBack)
                    },
                )
                DividerLightGray()
            }
        }

    ) {

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
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(16.dp))


                dateOfBirth = state.value.birthDate?.let {
                    DateUtils.dateOfBirthFormat(it)
                } ?: ""
                Timber.d("getDateFromCalendar dateOfBirth $dateOfBirth ")

                val calendarColor =
                    animateColorAsState(targetValue = if (dateOfBirth.isEmpty()) CCTheme.colors.grayOne else CCTheme.colors.primaryRed)
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
                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    isEnable = false,
                    title = stringResource(id = R.string.profile_setup_address_label),
                    placeHolder = stringResource(id = R.string.profile_setup_address_placeholder)
                ) {

                }
                Spacer(modifier = Modifier.height(16.dp))

                PhoneInputField(
                    onValueChanged = {
                        viewModel.setInputActions(
                            ProfileSetupActions.EnterInputData(
                                InputDataType.PHONE_NUMBER,
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
                    isActivated = state.value.data?.isFilled ?: false,
                    buttonClick = {
                        viewModel.setInputActions(ProfileSetupActions.ClickSave)
                    }
                )
            }
        }
    }
}