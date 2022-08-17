package com.civilcam.ui.verification

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.ext.formatToPhoneNumber
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.OtpCodeInputField
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.verification.model.VerificationActions

@Composable
fun VerificationScreenContent(
	viewModel: VerificationViewModel
) {
	val state = viewModel.state.collectAsState()
	val context = LocalContext.current

	val timer = remember {
		derivedStateOf {
			state.value.timeOut
		}
	}
	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	if (state.value.errorText.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.value.errorText,
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { state.value.errorText = "" }
		)
	}

    Scaffold(
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
					title = stringResource(id = state.value.verificationFlow.title),
					navigationItem = {
						BackButton {
							viewModel.setInputActions(VerificationActions.ClickGoBack)
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
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.weight(1f)
			) {
				Spacer(modifier = Modifier.height(32.dp))
				
				OtpCodeInputField(
					onValueChanged = {
						viewModel.setInputActions(VerificationActions.EnterCodeData(it))
					},
					hasError = state.value.hasError
				)
				
				Text(modifier = Modifier
					.padding(end = 12.dp, top = 8.dp),
					text = buildAnnotatedString {
						withStyle(
							style = SpanStyle(
								color = CCTheme.colors.grayOne,
								fontSize = 13.sp,
								fontWeight = FontWeight.W400
							),
						) {
							append("${context.resources.getString(R.string.code_sent_title)} ")
						}
						withStyle(
							style = SpanStyle(
								color = CCTheme.colors.primaryRed,
								fontSize = 13.sp,
								fontWeight = FontWeight.W500
							),
						) {
							if (state.value.verificationFlow == VerificationFlow.NEW_PHONE ||
								state.value.verificationFlow == VerificationFlow.CHANGE_PHONE
							) {
								append("${state.value.verificationSubject.formatToPhoneNumber()}\n")
							} else {
								append("${state.value.verificationSubject}\n")
							}
						}
						withStyle(
							style = SpanStyle(
								color = CCTheme.colors.grayOne,
								fontSize = 13.sp,
								fontWeight = FontWeight.W400
							),
						) {
							append(context.resources.getString(R.string.code_arrive))
						}
					})
			}
			
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {

				Crossfade(
					targetState = state.value.timeOut != "",
					modifier = Modifier
				) { targetState ->
					when (targetState) {
						true -> Text(
							text = stringResource(
								id = R.string.otp_code_timer,
								timer.value
							),
							color = CCTheme.colors.grayOne,
							fontSize = 17.sp,
							fontWeight = FontWeight.SemiBold,
							textAlign = TextAlign.Center
						)
						false -> Text(
							text = stringResource(id = R.string.resend_code),
							color = CCTheme.colors.primaryRed,
							fontSize = 17.sp,
							modifier = Modifier
								.clickable {
									viewModel.setInputActions(VerificationActions.ResendClick)
								}
								.background(Color.Transparent, RectangleShape),
							fontWeight = FontWeight.SemiBold,
							textAlign = TextAlign.Center
						)
					}
				}
				Spacer(modifier = Modifier.height(20.dp))
			}
		}
		
	}
}

