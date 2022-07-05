package com.civilcam.ui.profile.userProfile

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.profile.userProfile.model.*
import kotlinx.coroutines.flow.MutableStateFlow

class UserProfileViewModel(userId: Int, profileType: UserProfileScreen) :
	ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>() {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
	
	init {
		_state.value = _state.value.copy(userId = userId)
		_state.value = _state.value.copy(screenState = profileType)
	}
	
	override fun setInputActions(action: UserProfileActions) {
		TODO("Not yet implemented")
	}
}