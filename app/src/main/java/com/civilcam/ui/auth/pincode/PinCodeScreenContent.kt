package com.civilcam.ui.auth.pincode

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.pincode.content.PinCodeErrorBlock
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.PinCodeInputField


@OptIn(ExperimentalLayoutApi::class, ExperimentalAnimationApi::class)
@Composable
fun PinCodeScreenContent(viewModel: PinCodeViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	BackHandler {
		viewModel.setInputActions(
			PinCodeActions.GoBack
		)
	}
	
	Crossfade(targetState = state.value.noMatch) { noMatch ->
		if (noMatch) {
			AlertDialogComp(
				dialogTitle = "",
				dialogText = stringResource(id = R.string.pin_code_no_match_title),
				AlertDialogTypes.OK,
			)
			{ if (it) viewModel.clearStates() }
		}
	}
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = when (state.value.screenState) {
						PinCodeFlow.CREATE_PIN_CODE -> stringResource(id = R.string.pin_code_create_title)
						PinCodeFlow.CONFIRM_PIN_CODE -> stringResource(id = R.string.pin_code_confirm_title)
						PinCodeFlow.CURRENT_PIN_CODE -> stringResource(id = R.string.change_credentials_pin_code_title)
						PinCodeFlow.NEW_PIN_CODE -> stringResource(id = R.string.pin_code_new_title)
						PinCodeFlow.CONFIRM_NEW_PIN_CODE -> stringResource(id = R.string.pin_code_confirm_new_title)
					},
					navigationItem = {
						BackButton {
							viewModel.setInputActions(
								PinCodeActions.GoBack
							)
						}
					},
				)
				RowDivider()
			}
		}
	
	) {
		
		Column(
			modifier = Modifier
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			
			AnimatedVisibility(visible = state.value.screenState != PinCodeFlow.CREATE_PIN_CODE) {
				Text(
					text = when (state.value.screenState) {
						PinCodeFlow.CONFIRM_PIN_CODE -> stringResource(id = R.string.pin_code_re_enter_title)
						PinCodeFlow.CURRENT_PIN_CODE -> stringResource(id = R.string.pin_code_current_title)
						PinCodeFlow.NEW_PIN_CODE -> stringResource(id = R.string.pin_code_enter_new_title)
						else -> stringResource(id = R.string.pin_code_re_enter_new_title)
					},
					color = CCTheme.colors.black,
					fontSize = 17.sp,
					fontWeight = FontWeight.SemiBold,
					modifier = Modifier.padding(horizontal = 16.dp)
				)
			}
			
			Spacer(modifier = Modifier.height(23.dp))
			
			PinCodeInputField(
				pinCodeValue = {
					viewModel.setInputActions(
						PinCodeActions.EnterPinCode(
							it,
							state.value.screenState
						)
					)
				},
			)
			
			AnimatedVisibility(visible = state.value.screenState == PinCodeFlow.CREATE_PIN_CODE || state.value.screenState == PinCodeFlow.CONFIRM_PIN_CODE) {
				Column {
					Text(
						stringResource(id = R.string.pin_code_description),
						color = CCTheme.colors.grayOne,
						fontSize = 13.sp,
						textAlign = TextAlign.Center,
						modifier = Modifier
							.offset(y = (-35).dp)
							.padding(horizontal = 16.dp)
					)
				}
			}
			
			AnimatedVisibility(visible = state.value.currentNoMatch || state.value.newPinNoMatch) {
				Column(
					modifier = Modifier.fillMaxSize(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Bottom
				) {
					PinCodeErrorBlock(
						screenState = state.value.screenState
					)
				}
			}
		}
	}
}


