package com.civilcam.ui.profile.userProfile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserBaseInfo
import com.civilcam.domain.usecase.profile.GetUserProfileUseCase
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
	private val getUserProfileUseCase: GetUserProfileUseCase,
	private val mediaStorage: MediaStorage
) : ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>() {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())

	private val disposables = CompositeDisposable()

	init {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getUserProfileUseCase.invoke() }
				.onSuccess { user ->
					_state.update { it.copy(data = user) }
				}
				.onFailure { error ->
					error as ServiceException
					_state.update { it.copy(errorText = error.errorMessage) }
				}
			_state.value = _state.value.copy(isLoading = false)
		}
	}
	
	override fun setInputActions(action: UserProfileActions) {
		when (action) {
			UserProfileActions.GoBack -> goBack()
			UserProfileActions.ClickEdit -> goEdit()
			UserProfileActions.ClickSave -> goSave()
			is UserProfileActions.GoCredentials -> {
				when (action.userProfileType) {
					UserProfileType.EMAIL -> goCredentials(action.userProfileType)
					UserProfileType.PIN_CODE -> goPinCode()
					UserProfileType.PHONE_NUMBER -> goCredentials(action.userProfileType)
				}
			}
			is UserProfileActions.EnterInputData -> {
				when (action.dataType) {
					UserInfoDataType.FIRST_NAME -> firstNameEntered(action.data)
					UserInfoDataType.LAST_NAME -> lastNameEntered(action.data)
					else -> {}
				}
			}
			UserProfileActions.ClickAvatarSelect -> goAvatarSelect()
			UserProfileActions.ClickDateSelect -> openDatePicker()
			is UserProfileActions.ClickCloseDatePicker -> closeDatePicker()
			is UserProfileActions.ClickSelectDate -> getDateFromCalendar(action.date)
		}
	}
	
	private fun openDatePicker() {
		_state.value = _state.value.copy(showDatePicker = true)
	}
	
	private fun closeDatePicker() {
		_state.value = _state.value.copy(showDatePicker = false)
	}
	
	private fun getDateFromCalendar(birthDate: Long) {
		val data = getUserInfo()
		data.dob = birthDate.toString()
		_state.update { it.copy(data = data) }
		closeDatePicker()
	}
	
	private fun firstNameEntered(firstName: String) {
		val data = getUserInfo()
		data.firstName = firstName
		_state.update { it.copy(data = data) }
	}
	
	private fun lastNameEntered(lastName: String) {
		val data = getUserInfo()
		data.lastName = lastName
		_state.update { it.copy(data = data) }
	}
	
	private fun goSave() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
	}
	
	private fun goBack() {
		if (_state.value.screenState == UserProfileScreen.EDIT) {
			_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
		} else {
			navigateRoute(UserProfileRoute.GoBack)
		}
	}
	
	private fun goPinCode() {
		navigateRoute(UserProfileRoute.GoPinCode)
	}
	
	private fun goEdit() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.EDIT)
	}
	
	private fun goCredentials(userProfileType: UserProfileType) {
		navigateRoute(UserProfileRoute.GoCredentials(userProfileType))
	}
	
	private fun goAvatarSelect() {
		navigateRoute(UserProfileRoute.GoGalleryOpen)
	}
	
	fun onPictureUriReceived(uri: Uri) {
		mediaStorage.getImageMetadata(uri)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe {
				if (it.sizeMb > 5f) {
					_state.value = _state.value.copy(errorText = "Max image size is 5MB")
				} else {
					_state.value =
						_state.value.copy(profileImage = PictureModel(it.name, it.uri, it.sizeMb))
				}
			}
			.addTo(disposables)
	}

	private fun getUserInfo() = _state.value.data?.copy() ?: UserBaseInfo()
}