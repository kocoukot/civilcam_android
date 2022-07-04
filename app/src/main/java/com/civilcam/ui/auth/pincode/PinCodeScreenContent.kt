package com.civilcam.ui.auth.pincode

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.auth.pincode.model.PinCodeInputDataType
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.PinCodeInputField


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PinCodeScreenContent(viewModel: PinCodeViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	BackHandler {
		viewModel.setInputActions(PinCodeActions.GoBack)
	}
	
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
				.padding(horizontal = 16.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			
			AnimatedVisibility(visible = state.value.isConfirm) {
				Text(
					stringResource(id = R.string.pin_code_re_enter_title),
					color = CCTheme.colors.black,
					fontSize = 17.sp,
					fontWeight = FontWeight.SemiBold
				)
			}
			
			Spacer(modifier = Modifier.height(23.dp))
			
			PinCodeInputField(
				pinCodeValue = {
					if (state.value.isConfirm) {
						viewModel.setInputActions(
							PinCodeActions.EnterPinCode(
								it,
								PinCodeInputDataType.PIN_CONFIRM
							)
						)
					} else {
						viewModel.setInputActions(
							PinCodeActions.EnterPinCode(
								it,
								PinCodeInputDataType.PIN
							)
						)
					}
				},
			)
			
			Text(
				stringResource(id = R.string.pin_code_description),
				color = CCTheme.colors.grayOne,
				fontSize = 13.sp,
				textAlign = TextAlign.Center,
				modifier = Modifier.offset(y = (-35).dp)
			)
			
			Crossfade(targetState = state.value.noMatch) {
				if (it) {
					AlertDialogComp(
						dialogTitle = "",
						dialogText = stringResource(id = R.string.pin_code_no_match_title),
						AlertDialogTypes.OK,
					)
					{ if (it) viewModel.clearStates() }
				}
			}
		}
	}
}


