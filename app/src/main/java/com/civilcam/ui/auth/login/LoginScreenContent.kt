package com.civilcam.ui.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.SocialImage
import com.civilcam.ui.auth.create.model.InputDataType
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.common.compose.inputs.InputField
import com.civilcam.ui.common.compose.inputs.PasswordField


@Composable
fun LoginScreenContent(viewModel: LoginViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
                    title = stringResource(id = R.string.log_in),
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(LoginActions.ClickGoBack)
                        }
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
					LoginActions.EnterInputData(
						InputDataType.EMAIL,
						it
					)
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			PasswordField(
				name = stringResource(id = R.string.password),
				text = state.value.password,
				placeholder = stringResource(id = R.string.your_password),
				onValueChanged = {
					viewModel.setInputActions(
						LoginActions.EnterInputData(
							InputDataType.PASSWORD,
							it
						)
					)
				}
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
				SocialImage(
					painterResource(
						id = R.drawable.ic_facebook
					)
				)
				Spacer(modifier = Modifier.width(16.dp))
				SocialImage(
					painterResource(
						id = R.drawable.ic_google
					)
				)
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