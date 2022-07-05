package com.civilcam.ui.profile.userProfile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.profile.userProfile.content.UserProfileSection
import com.civilcam.ui.profile.userProfile.model.MainProfileContent
import com.civilcam.ui.profile.userProfile.model.UserProfileActions
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen

@Composable
fun UserProfileScreenContent(viewModel: UserProfileViewModel) {
	val state = viewModel.state.collectAsState()
	
	Scaffold(
		backgroundColor = CCTheme.colors.lightGray,
		modifier = Modifier.fillMaxSize()
	
	) {
		state.value.data?.let { data ->
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.background(CCTheme.colors.white),
			) {
				UserProfileSection(
					userData = data,
					screenType = state.value.screenState,
					mockAction = {
					
					},
					onBackButtonClick = {
						viewModel.setInputActions(UserProfileActions.GoBack)
					},
					onActionClick = { screenType ->
						setAction(viewModel, screenType)
					}
				)
				
				Divider(
					color = CCTheme.colors.lightGray, modifier = Modifier
						.height(32.dp)
				)
				
				Crossfade(targetState = state.value.screenState) { screenType ->
					when (screenType) {
						UserProfileScreen.PROFILE -> {
							MainProfileContent(
								data = data,
							) {
								viewModel.setInputActions(
									UserProfileActions.GoCredentials(it)
								)
							}
						}
						UserProfileScreen.EDIT -> {
						
						}
					}
				}
			}
		}
	}
}

private fun setAction(viewModel: UserProfileViewModel, screenType: UserProfileScreen) {
	when (screenType) {
		UserProfileScreen.PROFILE -> viewModel.setInputActions(
			UserProfileActions.GoSave(
				screenType
			)
		)
		UserProfileScreen.EDIT -> viewModel.setInputActions(
			UserProfileActions.GoEdit(
				screenType
			)
		)
	}
}