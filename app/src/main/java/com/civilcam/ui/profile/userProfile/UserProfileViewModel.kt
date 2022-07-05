package com.civilcam.ui.profile.userProfile

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.ui.profile.userProfile.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
	private val userId: Int,
	private val getUserInformationUseCase: GetUserInformationUseCase
) :
	ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>() {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
	
	init {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getUserInformationUseCase.getUser("") }
				.onSuccess { user ->
					_state.value = _state.value.copy(data = user)
				}
				.onFailure {
					_state.value = _state.value.copy(errorText = it.localizedMessage)
				}
			_state.value = _state.value.copy(isLoading = false)
		}
		
	}
	
	override fun setInputActions(action: UserProfileActions) {
		when (action) {
			UserProfileActions.GoBack -> goBack()
			is UserProfileActions.GoEdit -> goEdit()
			is UserProfileActions.GoSave -> {}
			is UserProfileActions.GoCredentials -> {
				when (action.userProfileType) {
					UserProfileType.EMAIL -> goCredentials(action.userProfileType)
					UserProfileType.PIN_CODE -> {}
					UserProfileType.PHONE_NUMBER -> goCredentials(action.userProfileType)
				}
			}
		}
	}
	
	private fun goBack() {
		if (_state.value.screenState == UserProfileScreen.EDIT) {
			_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
		} else {
			_steps.value = UserProfileRoute.GoBack
		}
	}
	
	private fun goEdit() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.EDIT)
	}
	
	private fun goCredentials(userProfileType: UserProfileType) {
		_steps.value = UserProfileRoute.GoCredentials(userProfileType)
	}
}