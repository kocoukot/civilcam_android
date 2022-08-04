package com.civilcam.ui.profile.setup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.AutocompletePlace
import com.civilcam.domain.model.SearchModel
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.domain.usecase.location.GetPlacesAutocompleteUseCase
import com.civilcam.domain.usecase.profile.SetAvatarUseCase
import com.civilcam.domain.usecase.profile.SetPersonalInfoUseCase
import com.civilcam.ui.profile.setup.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(FlowPreview::class)
class ProfileSetupViewModel(
    private val mediaStorage: MediaStorage,
    private val getPlacesAutocompleteUseCase: GetPlacesAutocompleteUseCase,
//    private val getPlaceDetailsUseCase: GetPlaceDetailsUseCase,
    private val setPersonalInfoUseCase: SetPersonalInfoUseCase,
    private val setAvatarUseCase: SetAvatarUseCase,
) : ComposeViewModel<ProfileSetupState, ProfileSetupRoute, ProfileSetupActions>() {

    override var _state: MutableStateFlow<ProfileSetupState> = MutableStateFlow(ProfileSetupState())

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()


    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    init {
        viewModelScope.launch {
            textSearch.debounce(400).collect { query ->
                query
                    .takeIf { it.isNotEmpty() }
                    ?.let {
                        kotlin.runCatching { getPlacesAutocompleteUseCase.invoke(it) }
                            .onSuccess { setSearchResult(it) }
                            .onFailure { error ->
                                error as ServiceException
                                _state.update { it.copy(errorText = error.errorMessage) }
                            }
                    } ?: run {
                    setSearchResult(emptyList())
                }
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
            ProfileSetupScreen.SETUP -> _steps.value = ProfileSetupRoute.GoBack
            ProfileSetupScreen.LOCATION -> _state.value =
                _state.value.copy(
                    profileSetupScreen = ProfileSetupScreen.SETUP,
                    searchLocationModel = SearchModel(),
                )
        }
    }

    private fun goNext() {
        _state.value.data?.let { userdata ->
            viewModelScope.launch {
                try {
                    userdata.profileImage?.uri?.let { uri -> setAvatarUseCase.invoke(uri) }
                    val result = setPersonalInfoUseCase.invoke(userdata)
                    if (result) _steps.value =
                        ProfileSetupRoute.GoVerification(userdata.phoneNumber)

                } catch (e: ServiceException) {
//                    withContext(Dispatchers.Main) {
                    Timber.d("resourceLocalized throwable ${e.errorCode}")
                    _state.update { it.copy(errorText = e.errorMessage) }
//                    }
                }
            }
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
        _state.update { it.copy(data = data, birthDate = birthDate) }
        closeDatePicker()
    }

    private fun firstNameEntered(firstName: String) {
        val data = getSetupUser()
        data.firstName = firstName
        _state.update { it.copy(data = data) }
    }

    private fun lastNameEntered(lastName: String) {
        _state.update { it.copy(data = getSetupUser().copy(lastName = lastName)) }
    }

    private fun phoneEntered(phoneNumber: String) {
        val data = getSetupUser()
        data.phoneNumber = phoneNumber
        _state.update { it.copy(data = data) }
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

    private fun goLocationPicker() {
        Timber.i("location clicked")
        _state.update { it.copy(profileSetupScreen = ProfileSetupScreen.LOCATION) }
    }

    private fun searchAddress(searchQuery: String) {
        _textSearch.value = searchQuery
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
        _state.value = _state.value.copy(
            searchLocationModel = _state.value.searchLocationModel.copy(
                searchResult = result
            ).copy()
        )
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
