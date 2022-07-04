package com.civilcam.ui.profile.credentials

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.EmailInputField
import com.civilcam.ui.common.compose.inputs.PhoneInputField
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.credentials.model.CredentialType
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ChangeCredentialsScreenContent(viewModel: ChangeCredentialsViewModel) {
	val state = viewModel.state.collectAsState()
	val listState = rememberScrollState()
	val coroutineScope = rememberCoroutineScope()
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
						title =
						when (credentialType) {
							CredentialType.EMAIL -> stringResource(id = R.string.create_account_email_label)
							CredentialType.PHONE -> stringResource(id = R.string.phone_number_text)
						},
						navigationItem = {
							BackButton {
								viewModel.setInputActions(ChangeCredentialsActions.ClickBack)
							}
						},
						actionItem = {
							TextActionButton(
								isEnabled = isActionActive.value,
								actionTitle = stringResource(id = R.string.save_text)
							) {
								setAction(viewModel, credentialType)
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
				
				AnimatedVisibility(visible = state.value.screenState == CredentialType.EMAIL) {
					
					Column {
						
						isActionActive.value = state.value.isFilled
						
						EmailInputField(
							title = stringResource(id = R.string.create_account_email_label),
							text = state.value.email,
							placeHolder = stringResource(id = R.string.create_account_email_placeholder),
							errorMessage = state.value.errorText,
							hasError = !state.value.isEmail,
							onValueChanged = {
								viewModel.setInputActions(
									ChangeCredentialsActions.EnterInputData(
										CredentialType.EMAIL,
										it
									)
								)
							},
							isReversed = true,
							onFocusChanged = {
								Timber.i("Focus state: $it")
							}
						)
						
						Spacer(modifier = Modifier.height(8.dp))
						
						AnimatedVisibility(visible = state.value.isEmail || state.value.email.isEmpty()) {
							Text(
								text = stringResource(id = R.string.digit_verification_code),
								modifier = Modifier.fillMaxWidth(),
								color = CCTheme.colors.grayOne,
								fontSize = 13.sp
							)
						}
					}
					
				}
				
				AnimatedVisibility(visible = state.value.screenState == CredentialType.PHONE) {
					
					isActionActive.value = state.value.validPhone
					
					PhoneInputField(
						onValueChanged = {
							viewModel.setInputActions(
								ChangeCredentialsActions.EnterInputData(
									CredentialType.PHONE,
									it
								)
							)
						},
						isReversed = true,
						isInFocus = {
							coroutineScope.launch {
								listState.scrollTo(1000)
							}
						},
						hasError = state.value.phoneTaken,
						errorMessage = state.value.phoneError
					)
					
				}
			}
		}
	}
}

private fun setAction(viewModel: ChangeCredentialsViewModel, credentialType: CredentialType) {
	when (credentialType) {
		CredentialType.EMAIL -> viewModel.setInputActions(
			ChangeCredentialsActions.ClickSave(
				credentialType
			)
		)
		CredentialType.PHONE -> viewModel.setInputActions(
			ChangeCredentialsActions.ClickSave(
				credentialType
			)
		)
	}
}