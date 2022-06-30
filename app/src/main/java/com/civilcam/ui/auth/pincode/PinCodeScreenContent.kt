package com.civilcam.ui.auth.pincode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TopAppBarContent


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PinCodeScreenContent(viewModel: PinCodeViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = if (state.value.isConfirm) R.string.pin_code_confirm_title else R.string.pin_code_create_title),
					navigationItem = {
						BackButton {
							viewModel.setInputActions(
								PinCodeActions.GoBack
							)
						}
					},
				)
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 16.dp)
		) {
		
		}
	}
}


