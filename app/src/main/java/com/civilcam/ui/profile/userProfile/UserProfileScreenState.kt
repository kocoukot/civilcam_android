package com.civilcam.ui.profile.userProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.profile.userProfile.model.UserProfileActions

@Composable
fun UserProfileScreenState(viewModel: UserProfileViewModel) {
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.lightGray,
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBarContent(
				title = stringResource(id = R.string.user_details_title),
				navigationItem = {
					BackButton {
						viewModel.setInputActions(UserProfileActions.GoBack)
					}
				},
				actionItem = {
					TextActionButton(
						isEnabled = true,
						actionTitle = stringResource(id = R.string.save_text)
					) {
						viewModel.setInputActions(UserProfileActions.GoEdit)
					}
				}
			)
		}
	) {
		
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(CCTheme.colors.white),
		) {
		
		}
	}
}
