package com.civilcam.ui.profile.credentials

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.compose.elements.BackButton
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.compose.elements.TextActionButton
import com.civilcam.ext_features.compose.elements.TopAppBarContent
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.profile.credentials.content.ChangeEmailScreenContent
import com.civilcam.ui.profile.credentials.content.ChangePhoneScreenContent
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.userProfile.model.UserProfileType

@Composable
fun ChangeCredentialsScreenContent(viewModel: ChangeCredentialsViewModel) {
	val state = viewModel.state.collectAsState()
	val listState = rememberScrollState()
	val isActionActive = remember { mutableStateOf(false) }
	
	Scaffold(
		backgroundColor = CCTheme.colors.lightGray,
		modifier = Modifier
			.fillMaxSize()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {
					viewModel.setInputActions(ChangeCredentialsActions.CheckCredential)
				},
			),
		topBar = {
			Column {
				Crossfade(targetState = state.value.screenState) { credentialType ->
					TopAppBarContent(
						title = if (credentialType == UserProfileType.EMAIL) stringResource(id = R.string.change_credentials_email_title) else stringResource(
							id = credentialType.title
						),
						navigationItem = {
							BackButton {
								viewModel.setInputActions(ChangeCredentialsActions.ClickBack)
							}
						},
						actionItem = {
							TextActionButton(
								isEnabled = isActionActive.value,
								actionTitle = if (state.value.screenState == UserProfileType.EMAIL)
									stringResource(id = com.civilcam.ext_features.R.string.continue_text) else stringResource(
									id = com.civilcam.ext_features.R.string.save_text
								)
							) {
								viewModel.setInputActions(
									ChangeCredentialsActions.ClickSave(
										credentialType
									)
								)
							}
						}
					)
				}
				RowDivider()
			}
		},
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
				
				Spacer(modifier = Modifier.height(32.dp))
				
				when (state.value.screenState) {
					UserProfileType.EMAIL -> {
						isActionActive.value = state.value.isFilled
						ChangeEmailScreenContent(
							onValueChanged = {
								viewModel.setInputActions(ChangeCredentialsActions.EnteredEmail(it))
							},
							value = state.value.email,
							errorMessage = state.value.errorText,
							hasError = state.value.emailError || !state.value.isEmail,
							currentEmail = state.value.currentEmail
						)
					}
					UserProfileType.PHONE_NUMBER -> {
						isActionActive.value = state.value.validPhone
						ChangePhoneScreenContent(
							onValueChanged = {
								viewModel.setInputActions(ChangeCredentialsActions.EnteredPhone(it))
							},
							errorMessage = state.value.errorText,
							isPhoneTaken = state.value.phoneError
						)
					}
					else -> {}
				}
			}
		}
	}
}