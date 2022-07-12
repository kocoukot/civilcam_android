package com.civilcam.ui.profile.setup

import android.net.Uri
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.ProfileSetupRoute
import com.civilcam.ui.profile.setup.model.ProfileSetupState
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileSetupViewModel(
    private val mediaStorage: MediaStorage,
) : ComposeViewModel<ProfileSetupState, ProfileSetupRoute, ProfileSetupActions>() {

    override var _state: MutableStateFlow<ProfileSetupState> = MutableStateFlow(ProfileSetupState())

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    init {

    }

    override fun setInputActions(action: ProfileSetupActions) {
        when (action) {
            ProfileSetupActions.ClickGoBack -> goBack()
            ProfileSetupActions.ClickSave -> goNext()
            ProfileSetupActions.ClickDateSelect -> openDatePicker()
            ProfileSetupActions.ClickLocationSelect -> {}
            is ProfileSetupActions.EnterInputData -> {
                when (action.dataType) {
                    UserInfoDataType.FIRST_NAME -> firstNameEntered(action.data)
                    UserInfoDataType.LAST_NAME -> lastNameEntered(action.data)
                    UserInfoDataType.PHONE_NUMBER -> phoneEntered(action.data)
                }
            }
            ProfileSetupActions.ClickAvatarSelect -> goAvatarSelect()
            is ProfileSetupActions.ClickCloseDatePicker -> closeDatePicker()
            is ProfileSetupActions.ClickSelectDate -> getDateFromCalendar(action.date)
        }
    }

    private fun goBack() {
        _steps.value = ProfileSetupRoute.GoBack
    }

    private fun goNext() {
        _state.value.data?.let {
            _steps.value = ProfileSetupRoute.GoVerification(it.phoneNumber)
        }
    }

    private fun openDatePicker() {
        _state.value = _state.value.copy(showDatePicker = true)
    }

    private fun closeDatePicker() {
        _state.value = _state.value.copy(showDatePicker = false)
    }

    private fun getDateFromCalendar(birthDate: Long) {
        val data = getSetupUser()
        data.dateBirth = birthDate
        _state.value = _state.value.copy(data = data, birthDate = birthDate)
        closeDatePicker()
    }

    private fun firstNameEntered(firstName: String) {
        val data = getSetupUser()
        data.firstName = firstName
        _state.value = _state.value.copy(data = data)
    }

    private fun lastNameEntered(lastName: String) {
        val data = getSetupUser()
        data.lastName = lastName
        _state.value = _state.value.copy(data = data)
    }

    private fun phoneEntered(phoneNumber: String) {
        val data = getSetupUser()
        data.phoneNumber = phoneNumber
        _state.value = _state.value.copy(data = data)
    }

    private fun goAvatarSelect() {
        _steps.value = ProfileSetupRoute.GoGalleryOpen
    }

    fun onPictureUriReceived(uri: Uri) {
        mediaStorage.getImageMetadata(uri)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.sizeMb > 5f) {
                    _state.value = _state.value.copy(errorText = "Max image size is 5MB")
                } else {
                    val data = _state.value.data?.copy() ?: UserSetupModel()
                    data.profileImage = PictureModel(it.name, it.uri, it.sizeMb)
                    _state.value = _state.value.copy(data = data)

                }
            }
            .addTo(disposables)
    }

    private fun getSetupUser() = _state.value.data?.copy() ?: UserSetupModel()
}
