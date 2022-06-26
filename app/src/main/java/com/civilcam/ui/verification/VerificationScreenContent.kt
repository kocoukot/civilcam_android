package com.civilcam.ui.verification

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.common.compose.OtpCodeInputField
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.verification.model.VerificationActions

@Composable
fun VerificationScreenContent(
	viewModel: VerificationViewModel,
	verificationFlow: VerificationFlow,
	verificationSubject: String
) {
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = when (verificationFlow) {
						VerificationFlow.PHONE -> {
							stringResource(id = R.string.phone_verification)
						}
						VerificationFlow.NEW_EMAIL -> {
							stringResource(id = R.string.email_verification)
						}
						VerificationFlow.RESET_PASSWORD -> {
							stringResource(id = R.string.reset_password)
						}
					},
					navigationAction = {
						viewModel.setInputActions(VerificationActions.ClickGoBack)
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
				
				Row {
					Text(
						text = stringResource(id = R.string.code_sent_title),
						color = CCTheme.colors.grayOne,
						fontSize = 13.sp
					)
					
					Spacer(Modifier.width(4.dp))
					
					Text(
						text = verificationSubject,
						color = CCTheme.colors.primaryRed,
						fontSize = 13.sp
					)
				}
				
				Text(
					text = stringResource(id = R.string.code_arrive),
					color = CCTheme.colors.grayOne,
					fontSize = 13.sp
				)
			}
			
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
			
//				Text(
//					text = stringResource(id = R.string.code_arrive),
//					color = CCTheme.colors.grayOne,
//					fontSize = 17.sp
//				)
				
				Text(
					text = stringResource(id = R.string.resend_code),
					color = CCTheme.colors.primaryRed,
					fontSize = 17.sp
				)
				
				Spacer(modifier = Modifier.height(20.dp))
			}
		}
		
	}
}

