package com.civilcam.ui.profile.userProfile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.local.MediaStorage
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.*
import com.civilcam.domainLayer.usecase.places.GetPlacesAutocompleteUseCase
import com.civilcam.domainLayer.usecase.profile.SetAvatarUseCase
import com.civilcam.domainLayer.usecase.profile.UpdateUserProfileUseCase
import com.civilcam.domainLayer.usecase.user.GetCurrentUserUseCase
import com.civilcam.ui.common.ext.SearchQuery
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.*
import com.civilcam.utils.DateUtils
import com.standartmedia.common.ext.castSafe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class UserProfileViewModel(
	private val mediaStorage: MediaStorage,
	private val getCurrentUserUseCase: GetCurrentUserUseCase,
	private val updateUserProfileUseCase: UpdateUserProfileUseCase,
	private val setAvatarUseCase: SetAvatarUseCase,
	private val getPlacesAutocompleteUseCase: GetPlacesAutocompleteUseCase
) : ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>(), SearchQuery {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
	override val mTextSearch = MutableStateFlow("")
	private val disposables = CompositeDisposable()

	init {
		fetchCurrentUser()
		query(viewModelScope) { query ->
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

	override fun setInputActions(action: UserProfileActions) {
		when (action) {
			UserProfileActions.GoBack -> goBack()
			UserProfileActions.ClickEdit -> goEdit()
			UserProfileActions.ClickSave -> goSave()
			is UserProfileActions.GoCredentials -> {
				when (action.userProfileType) {
					UserProfileType.PHONE_NUMBER -> goCredentials(action.userProfileType)
                    UserProfileType.EMAIL -> goCredentials(action.userProfileType)
					UserProfileType.PIN_CODE -> goPinCode()
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
			UserProfileActions.ClickLocationSelect -> openLocationSelect()
			is UserProfileActions.ClickCloseDatePicker -> closeDatePicker()
			is UserProfileActions.ClickSelectDate -> getDateFromCalendar(action.date)
			is UserProfileActions.LocationSearchQuery -> searchAddress(action.searchQuery)
			is UserProfileActions.ClickAddressSelect -> addressSelected(action.address)
		}
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

	fun fetchCurrentUser() {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getCurrentUserUseCase.invoke() }
				.onSuccess { user ->
					_state.update {
						it.copy(
							data = user,
							profileImage = user.userBaseInfo.avatar?.imageUrl,
						)
					}
				}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let {
						if (it.isForceLogout) navigateRoute(UserProfileRoute.ForceLogout)
						_state.update { it.copy(errorText = it.errorText) }
					}
				}
			_state.value = _state.value.copy(isLoading = false)
		}
	}

	private fun openLocationSelect() {
		_state.update { it.copy(screenState = UserProfileScreen.LOCATION) }
	}

	private fun openDatePicker() {
		_state.value = _state.value.copy(showDatePicker = true)
	}

	private fun closeDatePicker() {
		_state.value = _state.value.copy(showDatePicker = false)
	}

	private fun getDateFromCalendar(birthDate: Long) {
		val data = getUserInfo()
		data.userBaseInfo.dob = DateUtils.dateOfBirthDomainFormat(birthDate)
		_state.update { it.copy(data = data) }
		closeDatePicker()
	}

	private fun firstNameEntered(firstName: String) {
		val data = getUserInfo()
		data.userBaseInfo.firstName = firstName
		_state.update { it.copy(data = data) }
	}

	private fun lastNameEntered(lastName: String) {
		val data = getUserInfo()
		data.userBaseInfo.lastName = lastName
		_state.update { it.copy(data = data) }
	}

	private fun goSave() {
		_state.value.data?.let { userdata ->
			_state.update { it.copy(isLoading = true) }
			viewModelScope.launch {
				try {
					userdata.userBaseInfo.avatar?.imageUrl?.let { uri ->
						if (!uri.contains("http")) setAvatarUseCase.invoke(Uri.parse(uri))
					}
					val result = updateUserProfileUseCase.invoke(
						UserSetupModel(
							firstName = userdata.userBaseInfo.firstName,
							lastName = userdata.userBaseInfo.lastName,
							dateBirth = userdata.userBaseInfo.dob,
							location = userdata.userBaseInfo.address
						)
					)
					if (result) _state.value =
						_state.value.copy(screenState = UserProfileScreen.PROFILE)
					fetchCurrentUser()

				} catch (e: ServiceException) {
					Timber.d("resourceLocalized throwable ${e.errorCode}")
                    if (e.isForceLogout) navigateRoute(UserProfileRoute.ForceLogout)
					_state.update { it.copy(errorText = e.errorMessage) }
				}
				_state.update { it.copy(isLoading = false) }
			}
		}
	}

	private fun goBack() {
		when (_state.value.screenState) {
			UserProfileScreen.EDIT -> {
				_state.value = _state.value.copy(screenState = UserProfileScreen.PROFILE)
				fetchCurrentUser()
			}
			UserProfileScreen.LOCATION -> {
				_state.value = _state.value.copy(screenState = UserProfileScreen.EDIT)
			}
			else -> {
				navigateRoute(UserProfileRoute.GoBack)
			}
		}
	}

	private fun goPinCode() {
		navigateRoute(UserProfileRoute.GoPinCode)
	}

	private fun goEdit() {
		_state.value = _state.value.copy(screenState = UserProfileScreen.EDIT)
	}

	private fun goCredentials(userProfileType: UserProfileType) {
        navigateRoute(
            UserProfileRoute.GoCredentials(
                userProfileType,
                if (userProfileType == UserProfileType.EMAIL)
                    _state.value.data?.sessionUser?.email
                else
                    ""
            )
        )
    }

	private fun goAvatarSelect() {
		navigateRoute(UserProfileRoute.GoGalleryOpen)
	}

	fun onPictureUriReceived(uri: Uri) {
		mediaStorage.getImageMetadata(uri)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { image ->
				if (image.sizeMb > 5f) {
					_state.value = _state.value.copy(errorText = "Max image size is 5MB")
				} else {
					val data = getUserInfo()
					data.userBaseInfo.avatar =
						ImageInfo(imageUrl = image.uri.toString(), sizeMb = image.sizeMb)
					_state.update { it.copy(data = data, profileImage = image.uri.toString()) }
				}
			}
			.addTo(disposables)
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
				screenState = UserProfileScreen.EDIT,
				searchLocationModel = SearchModel(),
				data = getUserInfo().copy(
					userBaseInfo = it.data?.userBaseInfo?.copy(address = result.address)
						?: UserBaseInfo()
				),
			)
		}
	}

	private fun getUserInfo() = _state.value.data?.copy() ?: CurrentUser()
}