package com.civilcam.ui.auth.password.reset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.auth.password.reset.model.ResetActions
import timber.log.Timber

@Composable
fun ResetPasswordScreenContent(viewModel: ResetPasswordViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier
			.fillMaxSize()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = {
					viewModel.setInputActions(ResetActions.CheckIfEmail)
				},
			),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.reset_password),
					navigationItem = {
						BackButton {
							viewModel.setInputActions(ResetActions.ClickBack)
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
				errorMessage = state.value.errorText,
				hasError = !state.value.isEmail || state.value.emailError,
				onValueChanged = {
					viewModel.setInputActions(
						ResetActions.EnterInputData(
							PasswordInputDataType.EMAIL,
							it
						)
					)
				},
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
			
			Spacer(modifier = Modifier.weight(1f))
			
			ComposeButton(
				title = stringResource(id = R.string.continue_text),
				Modifier
					.padding(horizontal = 8.dp)
					.padding(top = 40.dp),
				isActivated = state.value.isFilled,
				buttonClick = {
					viewModel.setInputActions(
						ResetActions.ClickContinue
					)
				}
			)
			
			Spacer(modifier = Modifier.height(16.dp))
			
		}
	}
}