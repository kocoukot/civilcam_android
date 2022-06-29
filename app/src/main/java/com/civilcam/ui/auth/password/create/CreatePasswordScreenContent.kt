package com.civilcam.ui.auth.password.create

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
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.password.create.model.CreatePasswordActions
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.PasswordField
import com.civilcam.ui.common.compose.inputs.PasswordStrategyBlocks

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreatePasswordScreenContent(viewModel: CreatePasswordViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	val checkedStrategies = remember { mutableStateOf(0) }
	
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
			
			Spacer(modifier = Modifier.height(12.dp))
			
			PasswordField(
				name = stringResource(id = R.string.password),
				text = state.value.password,
				placeholder = stringResource(id = R.string.create_password),
				onValueChanged = {
					viewModel.setInputActions(
						CreatePasswordActions.EnterInputData(
							PasswordInputDataType.PASSWORD,
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
						CreatePasswordActions.EnterInputData(
							PasswordInputDataType.PASSWORD_REPEAT,
							it
						)
					)
				},
				noMatch = state.value.noMatch,
				isReEnter = state.value.noMatch
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


