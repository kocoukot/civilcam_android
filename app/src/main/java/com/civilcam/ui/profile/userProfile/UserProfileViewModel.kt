package com.civilcam.ui.profile.userProfile

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
import com.civilcam.domain.usecase.profile.UpdateUserProfileUseCase
import com.civilcam.domain.usecase.user.GetCurrentUserUseCase
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.*
import com.civilcam.utils.DateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(FlowPreview::class)
class UserProfileViewModel(
	private val mediaStorage: MediaStorage,
	private val getCurrentUserUseCase: GetCurrentUserUseCase,
	private val updateUserProfileUseCase: UpdateUserProfileUseCase,
	private val setAvatarUseCase: SetAvatarUseCase,
	private val getPlacesAutocompleteUseCase: GetPlacesAutocompleteUseCase
) : ComposeViewModel<UserProfileState, UserProfileRoute, UserProfileActions>() {
	override var _state: MutableStateFlow<UserProfileState> = MutableStateFlow(UserProfileState())
	
	private val disposables = CompositeDisposable()
	
	private val _textSearch = MutableStateFlow("")
	private val textSearch: StateFlow<String> = _textSearch.asStateFlow()
	
	init {
		fetchCurrentUser()
		
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
			UserProfileActions.ClickLocationSelect -> openLocationSelect()
			is UserProfileActions.ClickCloseDatePicker -> closeDatePicker()
			is UserProfileActions.ClickSelectDate -> getDateFromCalendar(action.date)
			is UserProfileActions.LocationSearchQuery -> searchAddress(action.searchQuery)
			is UserProfileActions.ClickAddressSelect -> addressSelected(action.address)
		}
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
	
	private fun fetchCurrentUser() {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getCurrentUserUseCase.invoke() }
				.onSuccess { user ->
					_state.update {
						it.copy(
							data = user,
							profileImage = user.userBaseInfo.avatar?.imageUrl,
							address = user.userBaseInfo.address,
							birthDate = user.userBaseInfo.dob
						)
					}
				}
				.onFailure { error ->
					error as ServiceException
					_state.update { it.copy(errorText = error.errorMessage) }
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
		data.dateBirth = birthDate
		_state.update { it.copy(user = data, birthDate = DateUtils.dateOfBirthDomainFormat(birthDate)) }
		closeDatePicker()
	}
	
	private fun firstNameEntered(firstName: String) {
		val data = getUserInfo()
		data.firstName = firstName
		_state.update { it.copy(user = data) }
	}
	
	private fun lastNameEntered(lastName: String) {
		val data = getUserInfo()
		data.lastName = lastName
		_state.update { it.copy(user = data) }
	}
	
	private fun goSave() {
		_state.value.user?.let { userdata ->
			_state.update { it.copy(isLoading = true) }
			viewModelScope.launch {
				try {
					userdata.profileImage?.uri.let { uri ->
						if (uri != null) {
							setAvatarUseCase.invoke(uri)
						}
					}
					val result = updateUserProfileUseCase.invoke(
						UserSetupModel(
							firstName = userdata.firstName,
							lastName = userdata.lastName,
							dateBirth = userdata.dateBirth,
							phoneNumber = userdata.phoneNumber,
							location = userdata.location
						)
					)
					if (result) _state.value =
						_state.value.copy(screenState = UserProfileScreen.PROFILE)
					_state.value = _state.value.copy(user = UserSetupModel())
					fetchCurrentUser()
					
				} catch (e: ServiceException) {
					Timber.d("resourceLocalized throwable ${e.errorCode}")
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
				_state.value = _state.value.copy(user = UserSetupModel())
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
		navigateRoute(UserProfileRoute.GoCredentials(userProfileType))
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
					data.profileImage = PictureModel(image.name, image.uri, image.sizeMb)
					_state.update { it.copy(user = data) }
					_state.update { it.copy(profileImage = image.uri.toString()) }
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
				user = getUserInfo().copy(location = result.address),
				address = result.address
			)
		}
		Timber.i("location selected ${_state.value.user}")
	}
	
	private fun getUserInfo() = _state.value.user?.copy() ?: UserSetupModel()
}