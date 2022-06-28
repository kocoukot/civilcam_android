package com.civilcam.ui.auth.create

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.InputDataType
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.EmailInputField
import com.civilcam.ui.common.compose.inputs.InputField
import com.civilcam.ui.common.compose.inputs.PasswordField
import com.civilcam.ui.common.compose.inputs.PasswordStrategyBlocks

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateAccountScreenContent(viewModel: CreateAccountViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	val checkedStrategies = remember { mutableStateOf(0) }
	
	val focusState = remember { mutableStateOf(false) }

	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
                    title = stringResource(id = R.string.create_account),
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(CreateAccountActions.ClickGoBack)
                        }
                    },
                )
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.fillMaxSize()
//                .imePadding()
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState())
		) {
			
			Spacer(modifier = Modifier.height(12.dp))
			
			EmailInputField(
				title = stringResource(id = R.string.create_account_email_label),
				text = state.value.email,
				placeHolder = stringResource(id = R.string.create_account_email_placeholder),
				hasError = !state.value.isEmail && !focusState.value,
				errorMessage = state.value.errorText,
				onValueChanged = {
					viewModel.setInputActions(
						CreateAccountActions.EnterInputData(
							InputDataType.EMAIL,
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
				placeholder = stringResource(id = R.string.create_password),
				onValueChanged = {
					viewModel.setInputActions(
						CreateAccountActions.EnterInputData(
							InputDataType.PASSWORD,
							it
						)
					)
				},
				hasError = checkedStrategies.value != 4,
				noMatch = state.value.noMatch
			)
			
			Spacer(modifier = Modifier.height(12.dp))
			
			PasswordStrategyBlocks(
				input = state.value.password,
				strategyUpdate = {
					checkedStrategies.value = it
				}
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			PasswordField(
				name = stringResource(id = R.string.confirm_password),
				text = state.value.confirmPassword,
				placeholder = stringResource(id = R.string.re_enter_password),
				onValueChanged = {
					viewModel.setInputActions(
						CreateAccountActions.EnterInputData(
							InputDataType.PASSWORD_REPEAT,
							it
						)
					)
				},
				noMatch = state.value.noMatch,
				isReEnter = state.value.noMatch
			)
			
			Spacer(modifier = Modifier.weight(1f))
			
			ComposeButton(
				title = stringResource(id = R.string.create_account),
				Modifier
					.padding(horizontal = 8.dp)
					.padding(top = 40.dp),
				isActivated = state.value.isFilled,
				buttonClick = {
					viewModel.setInputActions(
						CreateAccountActions.ClickContinue
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
					stringResource(id = R.string.already_have_account),
					color = CCTheme.colors.grayOne
				)
				Spacer(modifier = Modifier.width(4.dp))
				Text(
					stringResource(id = R.string.log_in),
					color = CCTheme.colors.primaryRed,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.clickable {
						viewModel.setInputActions(
							CreateAccountActions.ClickLogin
						)
					}
				)
			}
			
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

@Composable
fun SocialImage(
	painter: Painter
) {
	Image(
		painter = painter,
		contentDescription = null,
		modifier = Modifier
            .height(44.dp)
            .width(44.dp)
	)
}
