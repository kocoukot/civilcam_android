package com.civilcam.ui.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.OtpCodeInputField
import com.civilcam.ui.common.compose.TopAppBarContent


@Composable
fun LoginScreenContent(viewModel: LoginViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.create_account),
					navigationAction = {
						viewModel.setInputActions(LoginActions.ClickGoBack)
					},
				)
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.weight(1f)
			) {
				
				OtpCodeInputField(onValueChanged = {})
				
			}
			
		}
	}
}