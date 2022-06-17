package com.civilcam.ui.profile.setup

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.data.local.MediaStorage
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.ui.profile.setup.model.InputDataType
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.ProfileSetupRoute
import com.civilcam.ui.profile.setup.model.ProfileSetupState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class ProfileSetupViewModel(
    private val mediaStorage: MediaStorage,
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileSetupState> = MutableStateFlow(ProfileSetupState())
    val state: StateFlow<ProfileSetupState> = _state

    private val _steps: SingleLiveEvent<ProfileSetupRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<ProfileSetupRoute> = _steps

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    init {

    }

    fun setInputActions(action: ProfileSetupActions) {
        when (action) {
            ProfileSetupActions.CLickGoBack -> goBack()
            ProfileSetupActions.ClickSave -> goNext()
            ProfileSetupActions.ClickDateSelect -> openDatePicker()
            ProfileSetupActions.ClickLocationSelect -> {}
            is ProfileSetupActions.EnterInputData -> {
                Timber.d("actionData ${_state.value.data}")
                when (action.dataType) {
                    InputDataType.FIRST_NAME -> firstNameEntered(action.data)
                    InputDataType.LAST_NAME -> lastNameEntered(action.data)
                    InputDataType.PHONE_NUMBER -> phoneEntered(action.data)
                }
            }
            ProfileSetupActions.ClickAvatarSelect -> goAvatarSelect()
        }
    }

    private fun goBack() {
        _steps.value = ProfileSetupRoute.GoBack
    }

    private fun goNext() {
        _steps.value = ProfileSetupRoute.GoSubscription
    }

    private fun openDatePicker() {
        _steps.value = ProfileSetupRoute.OpenDatePicker
    }

    fun getDateFromCalendar(birthDate: Long) {
        val data = getSetupUser()
        data.dateBirth = birthDate
        _state.value = _state.value.copy(data = data)
        _state.value = _state.value.copy(date = birthDate)
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
