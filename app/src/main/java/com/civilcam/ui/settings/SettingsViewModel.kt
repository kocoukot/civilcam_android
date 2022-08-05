package com.civilcam.ui.settings

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.settings.NotificationsType
import com.civilcam.domain.usecase.settings.CheckCurrentPasswordUseCase
import com.civilcam.domain.usecase.settings.GetCurrentSubscriptionPlanUseCase
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.settings.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
	private val checkCurrentPasswordUseCase: CheckCurrentPasswordUseCase,
	private val getCurrentSubscriptionPlan: GetCurrentSubscriptionPlanUseCase
) : ComposeViewModel<SettingsState, SettingsRoute, SettingsActions>() {
	
	override var _state = MutableStateFlow(SettingsState())
	
	override fun setInputActions(action: SettingsActions) {
		when (action) {
			SettingsActions.ClickGoBack -> goBack()
			is SettingsActions.ClickSection -> changeSection(action.section)
			is SettingsActions.ClickAlertSwitch -> notificationChanged(
				action.status,
				action.switchType
			)
			SettingsActions.ClickSaveLanguage -> goBack()
			is SettingsActions.ClickCloseAlertDialog -> {
				if (action.isConfirm) {
					if (action.isLogOut) {
						navigateRoute(SettingsRoute.GoLanguageSelect) // todo add api
					} else {
						navigateRoute(SettingsRoute.GoLanguageSelect)// todo add api
					}
				} else goBack()
			}
			// is SettingsActions.IsNavBarVisible -> navBarStatus(action.hideNavBar)
			SettingsActions.ClickSendToSupport -> contactSupport()
			is SettingsActions.EnterContactSupportInfo -> setContactSupportInfo(
				action.issue,
				action.description,
				action.email
			)
			SettingsActions.CheckCurrentPassword -> checkCurrentPassword()
			is SettingsActions.EnterCurrentPassword -> enteredCurrentPassword(action.password)
			
			is SettingsActions.NewPasswordEntered -> passwordEntered(
				action.type,
				action.meetCriteria,
				action.password
			)
			SettingsActions.SaveNewPassword -> savePassword()
			SettingsActions.ClickGoSubscription -> fetchSubscriptionPlan()
			SettingsActions.GoSubscriptionManage -> goSubManage()
		}
	}
	
	private fun goSubManage() {
		navigateRoute(SettingsRoute.GoSubManage)
	}
	
	private fun fetchSubscriptionPlan() {
		viewModelScope.launch {
			kotlin.runCatching {
				getCurrentSubscriptionPlan.getCurrentSubscriptionPlan()
			}
				.onSuccess {
					val response = getCurrentSubscriptionPlan.getCurrentSubscriptionPlan()
					_state.value.data = _state.value.data.copy(subscriptionData = response)
				}
				.onFailure {
				
				}
		}
	}
	
	
	private fun goBack() {
		when (_state.value.settingsType) {
			SettingsType.MAIN -> navigateRoute(SettingsRoute.GoBack)
			SettingsType.CREATE_PASSWORD -> {
				navBarStatus(true)
				_state.value = _state.value.copy(settingsType = SettingsType.CHANGE_PASSWORD)
			}
			else -> {
				navBarStatus(false)
				_state.value = SettingsState()
			}
		}
	}
	
	private fun changeSection(section: SettingsType) {
		navBarStatus(!(section == SettingsType.LOG_OUT || section == SettingsType.DELETE_ACCOUNT))
		
		when (section) {
			
			SettingsType.ALERTS -> {
				_state.value = _state.value.copy(settingsType = section)
				_state.value = _state.value.copy(
					data = SettingsModel(alertsSectionData = getNotificationsTypeList())
				)
			}
			SettingsType.CHANGE_PASSWORD -> {
				_state.value = _state.value.copy(settingsType = section)
				_state.value = _state.value.copy(
					data = SettingsModel(changePasswordSectionData = ChangePasswordSectionData())
				)
			}
			SettingsType.TERMS_AND_POLICY -> navigateRoute(SettingsRoute.GoTerms)
			else -> {
				_state.value = _state.value.copy(settingsType = section)
			}
		}
	}
	
	private fun navBarStatus(hideBar: Boolean) {
		navigateRoute(SettingsRoute.IsNavBarVisible(hideBar))
	}
	
	private fun notificationChanged(status: Boolean, notifyType: NotificationsType) {
		Timber.d("updateSettingsModel ${_state.value}")
		viewModelScope.launch {
			_state.value.data.let { model ->
				model.alertsSectionData?.let { alert ->
					when (notifyType) {
						NotificationsType.SMS -> alert.isSMS = status
						NotificationsType.EMAIL -> alert.isEmail = status
					}
				}
				updateSettingsModel(model = model)
				_state.value = _state.value.copy(data = model.copy())
				
			}
		}
	}
	
	
	private fun getNotificationsTypeList() = SettingsAlertsSectionData(true, true)
	
	
	private fun updateSettingsModel(model: SettingsModel) {
		_state.value = _state.value.copy(
			data = SettingsModel(
				alertsSectionData = model.alertsSectionData,
			)
		)
	}
	
	private fun setContactSupportInfo(
		issueTheme: String,
		issueDescription: String,
		replyEmail: String,
	) {
		var data = _state.value.data
		data.let {
			data = data.copy(
				contactSupportSectionData = ContactSupportSectionData(
					issueTheme = issueTheme,
					issueDescription = issueDescription,
					replyEmail = replyEmail,
				)
			)
			
			_state.value = _state.value.copy(data = it)
		}
	}
	
	
	private fun contactSupport() {
		//todo api add
		goBack()
	}
	
	private fun enteredCurrentPassword(password: String) {
		var data = _state.value.data
		data = data.copy(
			changePasswordSectionData = ChangePasswordSectionData(
				currentPassword = password,
				error = ""
			)
		)
		_state.value = _state.value.copy(data = data)
	}
	
	private fun checkCurrentPassword() {
		_state.value.data.changePasswordSectionData?.let { data ->
			viewModelScope.launch {
				kotlin.runCatching {
					checkCurrentPasswordUseCase.checkPassword(data.currentPassword)
				}
					.onSuccess {
						Timber.d("checkCurrentPassword $it")
						if (it)
							_state.value =
								_state.value.copy(settingsType = SettingsType.CREATE_PASSWORD)
						else {
							var setModel = _state.value.data
							var section = setModel.changePasswordSectionData
							section =
								section?.copy(error = "The password is incorrect. Please try one more time or Restore your current password.")
							
							setModel = setModel.copy(changePasswordSectionData = section)
							_state.value = _state.value.copy(data = setModel)
						}
					}
					.onFailure {
					
					}
			}
		}
	}
	
	private fun passwordEntered(
		type: PasswordInputDataType,
		meetCriteria: Boolean,
		password: String
	) {
		var passwordData = _state.value.data.createPasswordSectionData
		when (type) {
			PasswordInputDataType.PASSWORD -> {
				passwordData = passwordData.copy(password = password)
				passwordData = passwordData.copy(meetCriteria = meetCriteria)
				
			}
			PasswordInputDataType.PASSWORD_REPEAT ->
				passwordData = passwordData.copy(confirmPassword = password)
			else -> {}
		}
		_state.value =
			_state.value.copy(data = _state.value.data.copy(createPasswordSectionData = passwordData))
		
		Timber.d("passwordEntered ${_state.value.data}")
	}
	
	
	private fun savePassword() {
		viewModelScope.launch {
			navBarStatus(false)
			_state.value = SettingsState()
		}
	}
}

