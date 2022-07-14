package com.civilcam.ui.profile.setup

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.profile.setup.content.DatePickerContent
import com.civilcam.ui.profile.setup.content.LocationSelectContent
import com.civilcam.ui.profile.setup.content.ProfileSetupContent
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.ProfileSetupScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileSetupScreenContent(viewModel: ProfileSetupViewModel) {

    val state = viewModel.state.collectAsState()

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
                    title = if (state.value.profileSetupScreen == ProfileSetupScreen.LOCATION)
                        stringResource(id = R.string.profile_setup_address_select_title)
                    else
                        stringResource(id = R.string.profile_setup_title),
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(ProfileSetupActions.ClickGoBack)
                        }
                    },
                )
                RowDivider()
            }
        }

    ) {

        AnimatedContent(targetState = state.value.profileSetupScreen) { screenState ->
            when (screenState) {
                ProfileSetupScreen.SETUP -> {
                    ProfileSetupContent(
                        avatar = state.value.data?.profileImage,
                        data = state.value.data,
                        birthDate = state.value.data?.dateBirth,
                        setupAction = {
                            viewModel.setInputActions(it)
                        }
                    )
                }
                ProfileSetupScreen.LOCATION -> {
                    LocationSelectContent(
                        searchData = state.value.searchLocationModel,
                        locationAction = {
                            viewModel.setInputActions(it)
                        },
                    )
                }
            }
        }
    }
}