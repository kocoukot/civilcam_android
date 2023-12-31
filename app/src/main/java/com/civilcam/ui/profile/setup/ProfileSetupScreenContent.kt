package com.civilcam.ui.profile.setup

import android.annotation.SuppressLint
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.profile.setup.content.DatePickerContent
import com.civilcam.ui.profile.setup.content.LocationSelectContent
import com.civilcam.ui.profile.setup.content.ProfileSetupContent
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.ProfileSetupScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileSetupScreenContent(viewModel: ProfileSetupViewModel) {

	val state = viewModel.state.collectAsState()
	val contract = PhotoPickerContract(viewModel::onPictureUriReceived)

	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	if (state.value.errorText.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.value.errorText,
			alertType = AlertDialogButtons.OK,
			onOptionSelected = { viewModel.setInputActions(ProfileSetupActions.ClickCloseAlert) }
		)
	}

	if (state.value.showDatePicker) {
		Dialog(
			properties = DialogProperties(
				dismissOnBackPress = true,
				dismissOnClickOutside = false
			), onDismissRequest = {
				viewModel.setInputActions(ProfileSetupActions.ClickSelectDate())
			}) {
			DatePickerContent(
				onPickerAction = { dateInMillis ->
					viewModel.setInputActions(ProfileSetupActions.ClickSelectDate(dateInMillis))
				}
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
							if (it is ProfileSetupActions.ClickAvatarSelect) {
								contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
							} else {
								viewModel.setInputActions(it)
							}
						}
					)
				}
				ProfileSetupScreen.LOCATION -> {
					LocationSelectContent(
						searchData = state.value.searchLocationModel,
						onAction = { result ->
							when (result) {
								is String -> viewModel.setInputActions(
									ProfileSetupActions.LocationSearchQuery(result)
								)
								is AutocompletePlace -> viewModel.setInputActions(
									ProfileSetupActions.ClickAddressSelect(result)
								)
							}
						},
					)
				}
			}
		}
	}
}