package com.civilcam.ui.profile.userProfile

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.civilcam.data.local.MediaStorage
import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.domainLayer.model.SearchModel
import com.civilcam.domainLayer.model.profile.UserSetupModel
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.ImageInfo
import com.civilcam.domainLayer.model.user.UserBaseInfo
import com.civilcam.domainLayer.usecase.places.GetPlacesAutocompleteUseCase
import com.civilcam.domainLayer.usecase.profile.SetAvatarUseCase
import com.civilcam.domainLayer.usecase.profile.UpdateUserProfileUseCase
import com.civilcam.domainLayer.usecase.user.GetCurrentUserUseCase
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.SearchQuery
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.profile.setup.model.UserInfoDataType
import com.civilcam.ui.profile.userProfile.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class UserProfileViewModel(
	private val mediaStorage: MediaStorage,
	private val getCurrentUserUseCase: GetCurrentUserUseCase,
	private val updateUserProfileUseCase: UpdateUserProfileUseCase,
	private val setAvatarUseCase: SetAvatarUseCase,
	private val getPlacesAutocompleteUseCase: GetPlacesAutocompleteUseCase
) : BaseViewModel.Base<UserProfileState>(mState = MutableStateFlow(UserProfileState())),
	SearchQuery {

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
								updateInfo {
									copy(
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

	override fun setInputActions(action: ComposeFragmentActions) {
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
			UserProfileActions.ClickAvatarSelect -> {}
			UserProfileActions.ClickDateSelect -> openDatePicker()
			UserProfileActions.ClickLocationSelect -> openLocationSelect()
			is UserProfileActions.ClickSelectDate -> getDateFromCalendar(action.date)
			is UserProfileActions.LocationSearchQuery -> searchAddress(action.searchQuery)
			is UserProfileActions.ClickAddressSelect -> addressSelected(action.address)
			UserProfileActions.ClickCloseAlert -> clearErrorText()
		}
	}

	private fun searchAddress(searchQuery: String) {
		mTextSearch.value = searchQuery
		updateInfo {
			copy(
				searchLocationModel = getState().searchLocationModel.copy(
					searchQuery = searchQuery
				)
			)
		}
	}

	fun fetchCurrentUser() {
		updateInfo { copy(isLoading = true) }
		viewModelScope.launch {
			networkRequest(
				action = { getCurrentUserUseCase.invoke() },
				onSuccess = { user ->
					updateInfo {
						copy(
							data = user,
							profileImage = user.userBaseInfo.avatar?.imageUrl,
						)
					}
				},
				onFailure = { error -> updateInfo { copy(errorText = error) } },
				onComplete = { updateInfo { copy(isLoading = false) } },
			)

		}
	}

	private fun openLocationSelect() {
		updateInfo { copy(screenState = UserProfileScreen.LOCATION) }
	}

	private fun openDatePicker() {
		updateInfo { copy(showDatePicker = true) }
	}

	private fun closeDatePicker() {
		updateInfo { copy(showDatePicker = false) }
	}

	private fun firstNameEntered(firstName: String) = updateCurrentInfo {
		copy(userBaseInfo = userBaseInfo.copy(firstName = firstName))
	}

	private fun lastNameEntered(lastName: String) = updateCurrentInfo {
		copy(userBaseInfo = userBaseInfo.copy(lastName = lastName))
	}

	private fun getDateFromCalendar(birthDate: Long?) {
		closeDatePicker()
		birthDate?.let {
			updateCurrentInfo {
				copy(
					userBaseInfo = userBaseInfo.copy(
						dob = DateUtils.dateOfBirthDomainFormat(
							birthDate
						)
					)
				)
			}
		}
	}

	private fun updateCurrentInfo(info: (CurrentUser.() -> CurrentUser?)) {
		updateInfo { copy(data = info.invoke(getUserInfo())) }
	}

	private fun goSave() {
		getState().data?.let { userdata ->
			updateInfo { copy(isLoading = true) }
			networkRequest(
				action = {
					userdata.userBaseInfo.avatar?.imageUrl?.let { uri ->
						if (!uri.contains("http")) setAvatarUseCase.invoke(Uri.parse(uri))
					}
					updateUserProfileUseCase.invoke(
						UserSetupModel(
							firstName = userdata.userBaseInfo.firstName,
							lastName = userdata.userBaseInfo.lastName,
							dateBirth = userdata.userBaseInfo.dob,
							location = userdata.userBaseInfo.address
						)
					)
				},
				onSuccess = { result ->
					if (result) updateInfo { copy(screenState = UserProfileScreen.PROFILE) }
				},
				onFailure = { error -> updateInfo { copy(errorText = error) } },
				onComplete = { updateInfo { copy(isLoading = false) } },
			)
		}
	}

	private fun goBack() {
		when (getState().screenState) {
			UserProfileScreen.EDIT -> {
				updateInfo { copy(screenState = UserProfileScreen.PROFILE) }
				fetchCurrentUser()
			}
			UserProfileScreen.LOCATION -> {
				updateInfo { copy(screenState = UserProfileScreen.EDIT) }
			}
			else -> {
				sendRoute(UserProfileRoute.GoBack)
			}
		}
	}

	private fun goPinCode() {
		sendRoute(UserProfileRoute.GoPinCode)
	}

	private fun goEdit() {
		updateInfo { copy(screenState = UserProfileScreen.EDIT) }
	}

	private fun goCredentials(userProfileType: UserProfileType) {
		sendRoute(
			UserProfileRoute.GoCredentials(
				userProfileType,
				if (userProfileType == UserProfileType.EMAIL)
					getState().data?.sessionUser?.email
				else
					""
			)
		)
	}

	fun onPictureUriReceived(uri: Uri) {
		mediaStorage.getImageMetadata(uri)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe { image ->
//				if (image.sizeMb > 5f) {
//					updateInfo { copy(errorText = "Max image size is 5MB") }
//				} else {
				val data = getUserInfo()
				data.userBaseInfo.avatar =
					ImageInfo(imageUrl = image.uri.toString(), sizeMb = image.sizeMb)
				updateInfo { copy(data = data, profileImage = image.uri.toString()) }
//				}
			}
			.addTo(disposables)
	}

	private fun setSearchResult(result: List<AutocompletePlace>) {
		Timber.i("location result $result")
		updateInfo {
			copy(
				searchLocationModel = getState().searchLocationModel.copy(
					searchResult = result
				)
			)
		}
	}

	private fun addressSelected(result: AutocompletePlace) {
		updateInfo {
			copy(
				screenState = UserProfileScreen.EDIT,
				searchLocationModel = SearchModel(),
				data = getUserInfo().copy(
					userBaseInfo = data?.userBaseInfo?.copy(address = result.address)
						?: UserBaseInfo()
				),
			)
		}
	}

	private fun getUserInfo() = getState().data?.copy() ?: CurrentUser()

    override fun clearErrorText() {
		updateInfo { copy(errorText = "") }
    }
}