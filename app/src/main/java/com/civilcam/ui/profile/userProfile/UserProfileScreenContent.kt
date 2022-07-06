package com.civilcam.ui.profile.userProfile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.profile.setup.content.DatePickerContent
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.content.UserProfileEditContent
import com.civilcam.ui.profile.userProfile.content.UserProfileSection
import com.civilcam.ui.profile.userProfile.model.MainProfileContent
import com.civilcam.ui.profile.userProfile.model.UserProfileActions
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen

@Composable
fun UserProfileScreenContent(viewModel: UserProfileViewModel) {
	val state = viewModel.state.collectAsState()
	
	if (state.value.showDatePicker) {
		Dialog(
			properties = DialogProperties(
				dismissOnBackPress = true,
				dismissOnClickOutside = false
			), onDismissRequest = {
				viewModel.setInputActions(UserProfileActions.ClickCloseDatePicker)
			}) {
			DatePickerContent(
				onClosePicker = {
					viewModel.setInputActions(UserProfileActions.ClickCloseDatePicker)
				},
				onSelectDate = {
					viewModel.setInputActions(UserProfileActions.ClickSelectDate(it))
				},
			)
		}
	}
	
	Scaffold(
		backgroundColor = CCTheme.colors.lightGray,
		modifier = Modifier.fillMaxSize()
	
	) {
		state.value.data?.userInfoSection?.let { data ->
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(CCTheme.colors.white),
			) {
				UserProfileSection(
					userData = data,
					avatar = state.value.profileImage,
					screenType = state.value.screenState,
					mockAction = {
						viewModel.setInputActions(UserProfileActions.ClickAvatarSelect)
					},
					onBackButtonClick = {
						viewModel.setInputActions(UserProfileActions.GoBack)
					},
					onActionClick = { screenType ->
						setAction(viewModel, screenType)
					},
					isSaveEnabled = state.value.isFilled
				)
				
				Divider(
					color = CCTheme.colors.lightGray, modifier = Modifier
						.height(32.dp)
				)
				
				Crossfade(targetState = state.value.screenState) { screenType ->
					when (screenType) {
						UserProfileScreen.PROFILE -> {
							MainProfileContent(
								data = data,
							) {
								viewModel.setInputActions(
									UserProfileActions.GoCredentials(it)
								)
							}
						}
						UserProfileScreen.EDIT -> {
							UserProfileEditContent(
								userData = data,
								onValueChanged = { userInfoDataType, data ->
									when (userInfoDataType) {
										UserInfoDataType.FIRST_NAME -> viewModel.setInputActions(
											UserProfileActions.EnterInputData(
												UserInfoDataType.FIRST_NAME,
												data
											)
										)
										UserInfoDataType.LAST_NAME -> viewModel.setInputActions(
											UserProfileActions.EnterInputData(
												UserInfoDataType.LAST_NAME,
												data
											)
										)
									}
								},
								onDateBirthClick = {
									viewModel.setInputActions(UserProfileActions.ClickDateSelect)
								}
							)
						}
					}
				}
			}
		}
	}
}

private fun setAction(viewModel: UserProfileViewModel, screenType: UserProfileScreen) {
	when (screenType) {
		UserProfileScreen.PROFILE -> viewModel.setInputActions(
			UserProfileActions.GoEdit(
				screenType
			)
		)
		UserProfileScreen.EDIT -> viewModel.setInputActions(
			UserProfileActions.GoSave(
				screenType
			)
		)
	}
}