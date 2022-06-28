package com.civilcam.ui.auth.password.reset

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.model.InputDataType
import com.civilcam.ui.auth.password.reset.model.ResetActions
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.InputField

@Composable
fun ResetPasswordScreenContent(viewModel: ResetPasswordViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.reset_password),
					navigationAction = {
						viewModel.setInputActions(ResetActions.ClickBack)
					},
				)
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
			
			Spacer(modifier = Modifier.height(12.dp))
			
			InputField(
				title = stringResource(id = R.string.create_account_email_label),
				text = state.value.email,
				placeHolder = stringResource(id = R.string.create_account_email_placeholder),
				errorMessage = state.value.errorText,
				inputType = KeyboardType.Email,
				hasError = !state.value.isEmail,
				inputCapitalization = KeyboardCapitalization.None
			) {
				viewModel.setInputActions(
					ResetActions.EnterInputData(
						InputDataType.EMAIL,
						it
					)
				)
			}
			
			Spacer(modifier = Modifier.height(8.dp))
			
			AnimatedVisibility(visible = !state.value.isEmail) {
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