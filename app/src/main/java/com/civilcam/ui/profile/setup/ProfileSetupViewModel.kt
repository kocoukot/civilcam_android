package com.civilcam.ui.profile.setup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.PictureModel
import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.domainLayer.model.SearchModel
import com.civilcam.domainLayer.model.profile.UserSetupModel
import com.civilcam.domainLayer.usecase.places.GetPlacesAutocompleteUseCase
import com.civilcam.domainLayer.usecase.profile.SetAvatarUseCase
import com.civilcam.domainLayer.usecase.profile.SetPersonalInfoUseCase
import com.civilcam.domainLayer.usecase.user.SetFCMTokenUseCase
import com.civilcam.ui.common.ext.SearchQuery
import com.civilcam.ui.profile.setup.model.*
import com.civilcam.utils.DateUtils.dateOfBirthFormat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileSetupViewModel(
    private val mediaStorage: MediaStorage,
    private val getPlacesAutocompleteUseCase: GetPlacesAutocompleteUseCase,
    private val setPersonalInfoUseCase: SetPersonalInfoUseCase,
    private val setAvatarUseCase: SetAvatarUseCase,
    private val setFCMTokenUseCase: SetFCMTokenUseCase
) : ComposeViewModel<ProfileSetupState, ProfileSetupRoute, ProfileSetupActions>(), SearchQuery {

    override var _state: MutableStateFlow<ProfileSetupState> = MutableStateFlow(ProfileSetupState())
    override val mTextSearch = MutableStateFlow("")

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    init {
        query(viewModelScope) { query ->
            Timber.d("viewModelScope $query")
            query
                .takeIf { it.isNotEmpty() }
                ?.let {
                    viewModelScope.launch {
                        kotlin.runCatching { getPlacesAutocompleteUseCase.invoke(it) }
                            .onSuccess { setSearchResult(it) }
                            .onFailure { error ->
                                _state.update {
                                    it.copy(
                                        errorText = error.localizedMessage ?: "Something went wrong"
                                    )
                                }
                            }
                    }
                } ?: run {
                setSearchResult(emptyList())
            }
        }
    }

    override fun setInputActions(action: ProfileSetupActions) {
        when (action) {
            ProfileSetupActions.ClickGoBack -> goBack()
            ProfileSetupActions.ClickSave -> goNext()
            ProfileSetupActions.ClickDateSelect -> openDatePicker()
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
            ProfileSetupActions.ClickGoLocationPicker -> goLocationPicker()

            is ProfileSetupActions.LocationSearchQuery -> searchAddress(action.searchQuery)
            is ProfileSetupActions.ClickAddressSelect -> addressSelected(action.address)
        }
    }

    private fun goBack() {
        when (_state.value.profileSetupScreen) {
            ProfileSetupScreen.SETUP -> navigateRoute(ProfileSetupRoute.GoBack)
            ProfileSetupScreen.LOCATION -> _state.value =
                _state.value.copy(
                    profileSetupScreen = ProfileSetupScreen.SETUP,
                    searchLocationModel = SearchModel(),
                )
        }
    }

    private fun goNext() {
        _state.value.data?.let { userdata ->
            _state.update { it.copy(isLoading = true) }
            viewModelScope.launch {
                try {
                    userdata.profileImage?.uri?.let { uri -> setAvatarUseCase.invoke(uri) }
                    val isSuccessResult = setPersonalInfoUseCase.invoke(userdata)
                    if (isSuccessResult) userdata.phoneNumber?.let {
                        setFCMTokenUseCase.invoke()
                        ProfileSetupRoute.GoVerification(it)
                    }?.let { navigateRoute(it) }

                } catch (e: ServiceException) {
                    Timber.d("resourceLocalized throwable ${e.errorCode}")
                    _state.update { it.copy(errorText = e.errorMessage) }
                }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun openDatePicker() {
        _state.update { it.copy(showDatePicker = true) }
    }

    private fun closeDatePicker() {
        _state.update { it.copy(showDatePicker = false) }
    }

    private fun getDateFromCalendar(birthDate: Long) = updateInfo {
        copy(dateBirth = dateOfBirthFormat(birthDate))
    }

    private fun firstNameEntered(firstName: String) = updateInfo {
        copy(firstName = firstName)
    }

    private fun lastNameEntered(lastName: String) = updateInfo {
        copy(lastName = lastName)
    }

    private fun phoneEntered(phoneNumber: String) = updateInfo {
        copy(phoneNumber = phoneNumber)
    }

    private fun updateInfo(info: (UserSetupModel.() -> UserSetupModel?)) {
        _state.update { it.copy(data = info.invoke(getSetupUser())) }
        Timber.i("updateInfo ${_state.value}")
    }

    private fun goAvatarSelect() {
        navigateRoute(ProfileSetupRoute.GoGalleryOpen)
    }

    fun onPictureUriReceived(uri: Uri) {
        mediaStorage.getImageMetadata(uri)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.sizeMb > 5f) {
                    _state.value = _state.value.copy(errorText = "Max image size is 5MB")
                } else {
                    val data = _state.value.data?.copy() ?: UserSetupModel()
                    data.profileImage =
                        PictureModel(it.name, it.uri, it.sizeMb)
                    _state.value = _state.value.copy(data = data)

                }
            }
            .addTo(disposables)
    }

    private fun getSetupUser() = _state.value.data?.copy() ?: UserSetupModel()

    private fun goLocationPicker() {
        _state.update { it.copy(profileSetupScreen = ProfileSetupScreen.LOCATION) }
    }

    private fun searchAddress(searchQuery: String) {
        mTextSearch.value = searchQuery
        _state.update {
            it.copy(
                searchLocationModel = _state.value.searchLocationModel.copy(
                    searchQuery = searchQuery
                )
            )
        }
    }

    private fun setSearchResult(result: List<AutocompletePlace>) {
        Timber.i("location result $result")
        _state.update {
            it.copy(
                searchLocationModel = _state.value.searchLocationModel.copy(
                    searchResult = result
                ).copy()
            )
        }
    }

    private fun addressSelected(result: AutocompletePlace) {
        _state.update {
            it.copy(
                profileSetupScreen = ProfileSetupScreen.SETUP,
                searchLocationModel = SearchModel(),
                data = getSetupUser().copy(location = result.address)
            )
        }
        Timber.i("location selected ${_state.value.data}")
    }

}