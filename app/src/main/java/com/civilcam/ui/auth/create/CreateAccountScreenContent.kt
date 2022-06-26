package com.civilcam.ui.auth.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.InputField
import com.civilcam.ui.common.compose.PasswordField
import com.civilcam.ui.common.compose.TopAppBarContent

@Composable
fun CreateAccountScreenContent(viewModel: CreateAccountViewModel) {
	
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.white,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			Column {
				TopAppBarContent(
					title = stringResource(id = R.string.create_account),
					navigationAction = {
						viewModel.setInputActions(CreateAccountActions.ClickGoBack)
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
		) {
			
			Column(Modifier.weight(1f)) {
				
				Spacer(modifier = Modifier.height(12.dp))
				
				InputField(
					title = stringResource(id = R.string.create_account_email_label),
					placeHolder = stringResource(id = R.string.create_account_email_placeholder)
				) {
				
				}
				
				PasswordField(
					name = stringResource(id = R.string.password),
					placeholder = stringResource(id = R.string.create_password),
					hasStrategy = true,
					onValueChanged = {
					
					}
				)
				
				Spacer(modifier = Modifier.height(16.dp))
				
				PasswordField(
					name = stringResource(id = R.string.confirm_password),
					placeholder = stringResource(id = R.string.re_enter_password),
					onValueChanged = {
					
					}
				)
			}
			
			Column {
				
				ComposeButton(
					title = stringResource(id = R.string.continue_text),
					Modifier.padding(horizontal = 8.dp),
					isActivated = false,
					buttonClick = {
					
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
						fontWeight = FontWeight.Bold
					)
				}
				
				Spacer(modifier = Modifier.height(16.dp))
			}
		}
	}
}

@Composable
private fun SocialImage(
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