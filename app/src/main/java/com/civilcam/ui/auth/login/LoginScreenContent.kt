package com.civilcam.ui.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.SocialImage
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.EmailInputField
import com.civilcam.ui.common.compose.inputs.PasswordField
import com.civilcam.ui.common.loading.DialogLoadingContent


@Composable
fun LoginScreenContent(viewModel: LoginViewModel) {

	val state = viewModel.state.collectAsState()

	val focusState = remember { mutableStateOf(false) }

	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	if (state.value.alertError.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.value.alertError,
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { viewModel.setInputActions(LoginActions.ClearErrorText) }
		)
	}
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.log_in),
					navigationItem = {
						BackButton {
							viewModel.setInputActions(LoginActions.ClickBack)
						}
					},
				)
				RowDivider()
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxSize()
				.imePadding()
				.verticalScroll(rememberScrollState()),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			
			Spacer(modifier = Modifier.height(32.dp))
			
			EmailInputField(
				title = stringResource(id = R.string.create_account_email_label),
				text = state.value.email,
				placeHolder = stringResource(id = R.string.create_account_email_placeholder),
				errorMessage = if (state.value.credError) "" else state.value.errorText,
				hasError = (!state.value.isEmail && !focusState.value) ||
						state.value.emailError || state.value.credError,
				onValueChanged = {
					viewModel.setInputActions(
						LoginActions.EnterInputData(
							PasswordInputDataType.EMAIL,
							it
						)
					)
				},
				onFocusChanged = {
					focusState.value = it
				}
			)
			
			Spacer(modifier = Modifier.height(16.dp))
			
			PasswordField(
				name = stringResource(id = R.string.password),
				text = state.value.password,
				placeholder = stringResource(id = R.string.your_password),
				onValueChanged = {
					viewModel.setInputActions(
						LoginActions.EnterInputData(
							PasswordInputDataType.PASSWORD,
							it
						)
					)
				},
				onFocusChanged = {},
				hasError = state.value.credError,
				error = state.value.errorText,
				isReEnter = state.value.credError
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			Row(
				modifier = Modifier
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.End
			) {
				Text(
					text = stringResource(id = R.string.reset_password),
					color = CCTheme.colors.primaryRed,
					fontWeight = FontWeight.Medium,
					modifier = Modifier.clickable {
						viewModel.setInputActions(
							LoginActions.ClickReset
						)
					}
				)
			}
			
			Spacer(modifier = Modifier.weight(1f))
			
			ComposeButton(
				title = stringResource(id = R.string.log_in),
				Modifier
					.padding(horizontal = 8.dp)
					.padding(top = 40.dp),
				isActivated = state.value.isFilled,
				buttonClick = {
					viewModel.setInputActions(
						LoginActions.ClickLogin
					)
				}
			)
			
			Spacer(modifier = Modifier.height(24.dp))
			
			Row(
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.fillMaxWidth()
			) {
				SocialImage(painterResource(id = R.drawable.ic_facebook)) {
					viewModel.setInputActions(LoginActions.FBLogin)
				}

				Spacer(modifier = Modifier.width(16.dp))

				SocialImage(painterResource(id = R.drawable.ic_google)) {
					viewModel.setInputActions(LoginActions.GoogleLogin)
				}
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
			Row(
				horizontalArrangement = Arrangement.Center,
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.Bottom
			) {
				Text(
					stringResource(id = R.string.dont_have_account),
					color = CCTheme.colors.grayOne
				)
				Spacer(modifier = Modifier.width(4.dp))
				Text(
					stringResource(id = R.string.register),
					color = CCTheme.colors.primaryRed,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.clickable {
						viewModel.setInputActions(
							LoginActions.ClickRegister
						)
					}
				)
			}
			
			Spacer(modifier = Modifier.height(16.dp))
			
		}
	}
}