package com.civilcam.ui.profile.userProfile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserInfo
import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
	private val getUserInformationUseCase: GetUserInformationUseCase,
	private val mediaStorage: MediaStorage
) :
	ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>() {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
	
	private val disposables = CompositeDisposable()
	
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
			is UserProfileActions.GoSave -> goSave()
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
		data.dateOfBirth = birthDate
		_state.value.data = _state.value.data?.copy(userInfoSection = data)
		closeDatePicker()
	}
	
	private fun firstNameEntered(firstName: String) {
		val data = getUserInfo()
		data.firstName = firstName
		_state.value.data = _state.value.data?.copy(userInfoSection = data)
	}
	
	private fun lastNameEntered(lastName: String) {
		val data = getUserInfo()
		data.lastName = lastName
		_state.value.data = _state.value.data?.copy(userInfoSection = data)
	}
	
	private fun goSave() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
	}
	
	private fun goBack() {
		if (_state.value.screenState == UserProfileScreen.EDIT) {
			_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
		} else {
			_steps.value = UserProfileRoute.GoBack
		}
	}
	
	private fun goPinCode() {
		_steps.value = UserProfileRoute.GoPinCode
	}
	
	private fun goEdit() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.EDIT)
	}
	
	private fun goCredentials(userProfileType: UserProfileType) {
		_steps.value = UserProfileRoute.GoCredentials(userProfileType)
	}
	
	private fun goAvatarSelect() {
		_steps.value = UserProfileRoute.GoGalleryOpen
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
	
	private fun getUserInfo() = _state.value.data?.userInfoSection?.copy() ?: UserInfo()
}