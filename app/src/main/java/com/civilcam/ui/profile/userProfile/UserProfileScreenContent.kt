package com.civilcam.ui.profile.userProfile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.profile.setup.content.DatePickerContent
import com.civilcam.ui.profile.setup.content.LocationSelectContent
import com.civilcam.ui.profile.userProfile.content.MainProfileContent
import com.civilcam.ui.profile.userProfile.content.UserProfileEditContent
import com.civilcam.ui.profile.userProfile.content.UserProfileSection
import com.civilcam.ui.profile.userProfile.model.UserProfileActions
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserProfileScreenContent(viewModel: UserProfileViewModel) {
	val state = viewModel.state.collectAsState()


	if (state.value.isLoading) {
		DialogLoadingContent()
	}

	if (state.value.errorText.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.value.errorText,
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { state.value.errorText = "" }
		)
	}

	if (state.value.showDatePicker) {
		Dialog(
			properties = DialogProperties(
				dismissOnBackPress = true,
				dismissOnClickOutside = false
			), onDismissRequest = {
				viewModel.setInputActions(UserProfileActions.ClickSelectDate())
			}) {
			DatePickerContent(
				onPickerAction = { dateInMillis ->
					viewModel.setInputActions(UserProfileActions.ClickSelectDate(dateInMillis))
				}
			)
		}
	}

	Scaffold(
		backgroundColor = CCTheme.colors.lightGray,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			AnimatedVisibility(visible = state.value.screenState == UserProfileScreen.LOCATION) {
				Column {
					TopAppBarContent(
						title = stringResource(id = R.string.profile_setup_address_select_title),
						navigationItem = {
							BackButton {
								viewModel.setInputActions(UserProfileActions.GoBack)
							}
						},
					)
					RowDivider()
				}
			}
		}

	) {
		state.value.data?.let { data ->
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(CCTheme.colors.white),
			) {
				AnimatedVisibility(visible = state.value.screenState != UserProfileScreen.LOCATION) {
					UserProfileSection(
						userData = data,
						screenType = state.value.screenState,
						mockAction = {
							viewModel.setInputActions(UserProfileActions.ClickAvatarSelect)
						},
						onActionClick = { action ->
							viewModel.setInputActions(action)
						},
						isSaveEnabled = state.value.isFilled
					)
				}

				AnimatedVisibility(
					visible = state.value.screenState == UserProfileScreen.PROFILE ||
							state.value.screenState == UserProfileScreen.EDIT
				) {
					Divider(
						color = CCTheme.colors.lightGray, modifier = Modifier
							.height(32.dp)
					)
				}

				Crossfade(targetState = state.value.screenState) { screenType ->
					when (screenType) {
						UserProfileScreen.PROFILE -> {
							MainProfileContent(
								data = data,
								onRowClicked = viewModel::setInputActions
							)
						}
						UserProfileScreen.EDIT -> {
							UserProfileEditContent(
								userData = data,
								onValueChanged = { userInfoDataType, data ->
									viewModel.setInputActions(
										UserProfileActions.EnterInputData(
											userInfoDataType,
											data
										)
									)
								},
								onActionClicked = viewModel::setInputActions
							)
						}
						UserProfileScreen.LOCATION -> {
							LocationSelectContent(
								searchData = state.value.searchLocationModel,
								onAction = { result ->
									when (result) {
										is String -> viewModel.setInputActions(
											UserProfileActions.LocationSearchQuery(result)
										)
										is AutocompletePlace -> viewModel.setInputActions(
											UserProfileActions.ClickAddressSelect(result)
										)
									}
								}
							)
						}
					}
				}
			}
		}
	}
}