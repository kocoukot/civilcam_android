package com.civilcam.ui.auth.password.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.compose.elements.passwordCheck.PasswordStrategyBlocks
import com.civilcam.ext_features.compose.elements.passwordCheck.PasswordStrategyState
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.auth.password.create.model.CreatePasswordActions

@Composable
fun CreatePasswordScreenContent(viewModel: CreatePasswordViewModel) {

	val state = viewModel.state.collectAsState()

	val checkedStrategies = remember { mutableStateOf(0) }
	var passwordFocusState by remember { mutableStateOf(PasswordStrategyState.NONE) }
	var passwordHadFocus by remember { mutableStateOf(false) }
	
	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.create_new_password),
					navigationItem = {
						BackButton {
							viewModel.setInputActions(CreatePasswordActions.ClickGoBack)
						}
					},
				)
				RowDivider()
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.fillMaxSize()
				.imePadding()
				.padding(horizontal = 16.dp)
				.verticalScroll(rememberScrollState())
		) {
			
			Spacer(modifier = Modifier.height(32.dp))
			
			PasswordField(
				name = stringResource(id = com.civilcam.ext_features.R.string.password),
				text = state.value.password,
				placeholder = stringResource(id = com.civilcam.ext_features.R.string.create_password),
				onValueChanged = {
					viewModel.setInputActions(
						CreatePasswordActions.EnterInputData(
							PasswordInputDataType.PASSWORD,
							it
						)
					)
				},
//				hasError = checkedStrategies.value != 4,
				noMatch = state.value.noMatch,
				onFocusChanged = {
					if (it) passwordHadFocus = true
					if (passwordHadFocus) {
						passwordFocusState =
							if (!it) PasswordStrategyState.LOOSE_FOCUS else PasswordStrategyState.NONE
					}
				}
			)
			
			Spacer(modifier = Modifier.height(12.dp))
			
			PasswordStrategyBlocks(
				input = state.value.password,
				strategyUpdate = {
					checkedStrategies.value = it
				},
				onLooseFocus = passwordFocusState
			)
			
			Spacer(modifier = Modifier.height(8.dp))
			
			PasswordField(
				name = stringResource(id = com.civilcam.ext_features.R.string.confirm_password),
				text = state.value.confirmPassword,
				placeholder = stringResource(id = com.civilcam.ext_features.R.string.re_enter_password),
				onValueChanged = {
					viewModel.setInputActions(
						CreatePasswordActions.EnterInputData(
							PasswordInputDataType.PASSWORD_REPEAT,
							it
						)
					)
				},
				noMatch = state.value.noMatch,
				isReEnter = state.value.noMatch,
				onFocusChanged = {}
			)
			
			Spacer(modifier = Modifier.weight(1f))
			
			ComposeButton(
				title = stringResource(id = R.string.save_password),
				Modifier
					.padding(horizontal = 8.dp)
					.padding(top = 40.dp),
				isActivated = state.value.isFilled,
				buttonClick = {
					viewModel.setInputActions(
						CreatePasswordActions.ClickSave
					)
				}
			)
			
			Spacer(modifier = Modifier.height(16.dp))
			
		}
	}
}


