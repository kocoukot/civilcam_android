package com.civilcam.settings_feature

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.AuthType
import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.domainLayer.model.user.SettingsNotificationType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.subscriptions.GetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.user.*
import com.civilcam.ext_features.alert.ScreenAlert
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.settings_feature.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
	private val checkCurrentPasswordUseCase: CheckCurrentPasswordUseCase,
	private val changePasswordUseCase: ChangePasswordUseCase,
	private val setUserLanguageUseCase: SetUserLanguageUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
	private val contactSupportUseCase: ContactSupportUseCase,
	private val toggleSettingsUseCase: ToggleSettingsUseCase,
	private val getUserSubscriptionUseCase: GetUserSubscriptionUseCase
) : BaseViewModel.Base<SettingsState, SettingsActions, SettingsRoute>(
	mState = MutableStateFlow(SettingsState())
) {


	init {
		getLocalCurrentUserUseCase().let { user ->
			updateInfo { copy(canChangePassword = user.sessionUser.canChangePassword) }
		}
	}

	override fun setInputActions(action: SettingsActions) {
		when (action) {
			SettingsActions.ClickGoBack -> goBack()
			is SettingsActions.ClickSection -> changeSection(action.section)
			is SettingsActions.ClickAlertSwitch -> notificationChanged(
				action.status, action.switchType
			)
			is SettingsActions.ClickSaveLanguage -> onLanguageChange(action.languageType)
			is SettingsActions.ClickCloseAlertDialog -> {
				if (action.isConfirm) doActionOnAccount(action.isLogOut) else goBack()
			}
			SettingsActions.ClickSendToSupport -> contactSupport()
			is SettingsActions.EnterContactSupportInfo -> setContactSupportInfo(
				action.issue, action.description, action.email
			)
			SettingsActions.CheckCurrentPassword -> checkCurrentPassword()
			is SettingsActions.EnterCurrentPassword -> enteredCurrentPassword(action.password)
			
			is SettingsActions.NewPasswordEntered -> passwordEntered(
				action.type, action.meetCriteria, action.password
			)
			SettingsActions.SaveNewPassword -> savePassword()
			SettingsActions.ClickGoSubscription -> fetchSubscriptionPlan()
			SettingsActions.GoSubscriptionManage -> sendEvent(SettingsRoute.GoSubManage)
			SettingsActions.ClearErrorText -> clearErrorText()
			SettingsActions.ClickCloseScreenAlert -> closeScreenAlert()
		}
	}
	
	private fun closeScreenAlert() {
		updateInfo { copy(screenAlert = null) }
	}
	
	private fun onLanguageChange(languageType: LanguageType) {
		updateInfo { copy(isLoading = true) }
		networkRequest(action = { setUserLanguageUseCase(languageType) },
			onSuccess = { goBack() },
			onFailure = { error ->
				error.serviceCast { msg, _, _ -> updateInfo { copy(errorText = msg) } }
			},
			onComplete = { updateInfo { copy(isLoading = false) } })
	}
	
	override fun clearErrorText() {
		updateInfo { copy(errorText = "") }
	}
	
	private fun doActionOnAccount(isLogOut: Boolean) {
		updateInfo { copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching { if (isLogOut) logoutUseCase() else deleteAccountUseCase() }
				.onSuccess { sendEvent(SettingsRoute.GoStartScreen) }
				.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout ->
						if (isForceLogout) sendEvent(SettingsRoute.ForceLogout)
						updateInfo { copy(errorText = msg) }
					}
				}
			updateInfo { copy(isLoading = false) }
		}
	}
	
	private fun fetchSubscriptionPlan() {
		viewModelScope.launch {
			kotlin.runCatching { getUserSubscriptionUseCase.getUserSubscription() }
				.onSuccess {
					val response = getUserSubscriptionUseCase.getUserSubscription()
					updateInfo { copy(data = data.copy(subscriptionData = response)) }
				}.onFailure {
				
				}
		}
	}
	
	
	private fun goBack(screenAlert: ScreenAlert? = null) {
		when (getState().settingsType) {
			SettingsType.MAIN -> sendEvent(SettingsRoute.GoBack)
			SettingsType.CREATE_PASSWORD -> {
				updateInfo { copy(settingsType = SettingsType.CHANGE_PASSWORD) }
			}
			else -> updateInfo { SettingsState() }
		}
		updateInfo { copy(screenAlert = screenAlert) }
	}
	
	private fun changeSection(section: SettingsType) {
		when (section) {
			SettingsType.ALERTS -> {
				viewModelScope.launch {
					getLocalCurrentUserUseCase().settings.let { settings ->
						Timber.i("getLocalCurrentUserUseCase ${getLocalCurrentUserUseCase.invoke()}")
						updateInfo {
							copy(
								settingsType = section, data = data.copy(
									alertsSectionData = SettingsAlertsSectionData(
										isSMS = settings.smsNotifications,
										isEmail = settings.emailNotification
									)
								)
							)
						}
					}
				}
			}
			
			SettingsType.CONTACT_SUPPORT -> {
				getLocalCurrentUserUseCase().let { user ->
					updateInfo {
						copy(
							settingsType = section, data = data.copy(
								contactSupportSectionData = ContactSupportSectionData(
									replyEmail = if (user.sessionUser.authType == AuthType.email) user.sessionUser.email else "",
									canChangeEmail = user.sessionUser.canChangeEmail
								)
							)
						)
					}
				}
			}
			SettingsType.CHANGE_PASSWORD -> {
				updateInfo {
					copy(
						settingsType = section,
						data = SettingsModel(changePasswordSectionData = ChangePasswordSectionData())
					)
				}
			}
			SettingsType.TERMS_AND_POLICY -> sendEvent(SettingsRoute.GoTerms)
			else -> updateInfo { copy(settingsType = section) }
		}
	}
	
	private fun notificationChanged(status: Boolean, notifyType: SettingsNotificationType) {
		updateInfo { copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching { toggleSettingsUseCase(type = notifyType.domain, isOn = status) }
				.onSuccess {
					updateInfo {
						copy(
							data = data.copy(
								alertsSectionData = when (notifyType) {
									SettingsNotificationType.SMS -> data.alertsSectionData.copy(
										isSMS = status
									)
									else -> data.alertsSectionData.copy(isEmail = status)
								}
							)
						)
					}
				}.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout ->
						if (isForceLogout) sendEvent(SettingsRoute.ForceLogout)
						updateInfo { copy(errorText = msg) }
					}
				}
			updateInfo { copy(isLoading = false) }
		}
	}
	
	private fun setContactSupportInfo(
		issueTheme: String,
		issueDescription: String,
		replyEmail: String,
	) {
		updateInfo {
			copy(
				data = data.copy(
					contactSupportSectionData = data.contactSupportSectionData.copy(
						issueTheme = issueTheme,
						issueDescription = issueDescription,
						replyEmail = replyEmail
					)
				)
			)
		}
		Timber.i("setContactSupportInfo ${getState().data}  issueTheme $issueTheme")
	}
	
	
	private fun contactSupport() {
		updateInfo { copy(isLoading = true) }
		getState().data.contactSupportSectionData.let {
			viewModelScope.launch {
				kotlin.runCatching {
					contactSupportUseCase(
						issue = it.issueTheme,
						text = it.issueDescription,
						email = it.replyEmail,
					)
				}.onSuccess { goBack(ScreenAlert.ReportSentAlert) }.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout ->
						if (isForceLogout) sendEvent(SettingsRoute.ForceLogout)
						updateInfo { copy(errorText = msg) }
					}
					}
				updateInfo { copy(isLoading = false) }
			}
		}
	}
	
	private fun enteredCurrentPassword(password: String) {
		updateInfo {
			copy(
				data = data.copy(
					changePasswordSectionData = ChangePasswordSectionData(
						currentPassword = password, hasError = false
					)
				)
			)
		}
	}
	
	private fun checkCurrentPassword() {
		getState().data.changePasswordSectionData?.let { data ->
			viewModelScope.launch {
				updateInfo { copy(isLoading = true) }
				kotlin.runCatching { checkCurrentPasswordUseCase(data.currentPassword) }
					.onSuccess { isCorrect ->
						Timber.d("checkCurrentPassword $isCorrect")
						if (isCorrect) updateInfo { copy(settingsType = SettingsType.CREATE_PASSWORD) }
						else {
							updateInfo {
								copy(
									data = this.data.copy(
										changePasswordSectionData = this.data.changePasswordSectionData?.copy(
											hasError = true
										)
									)
								)
							}
						}
					}.onFailure { error ->
						error.serviceCast { msg, _, _ ->
							updateInfo { copy(errorText = msg) }
						}
					}
				updateInfo { copy(isLoading = false) }
				
			}
		}
	}
	
	private fun passwordEntered(
		type: PasswordInputDataType, meetCriteria: Boolean, password: String
	) {
		var passwordData = getState().data.createPasswordSectionData
		when (type) {
			PasswordInputDataType.PASSWORD -> passwordData =
				passwordData.copy(password = password, meetCriteria = meetCriteria)
			PasswordInputDataType.PASSWORD_REPEAT -> passwordData =
				passwordData.copy(confirmPassword = password)
			else -> {}
		}
		updateInfo { copy(data = data.copy(createPasswordSectionData = passwordData)) }
		Timber.d("passwordEntered ${getState().data}")
	}
	
	
	private fun savePassword() {
		getState().data.changePasswordSectionData?.currentPassword?.let { currentPassword ->
			viewModelScope.launch {
				updateInfo { copy(isLoading = true) }
				kotlin.runCatching {
					changePasswordUseCase(
						currentPassword = currentPassword,
						newPassword = getState().data.createPasswordSectionData.password,
					)
				}.onSuccess {
					updateInfo { SettingsState(screenAlert = ScreenAlert.PasswordChangedAlert) }
				}.onFailure { error ->
					error.serviceCast { msg, _, _ ->
						updateInfo { copy(errorText = msg) }
					}
				}
				updateInfo { copy(isLoading = false) }
			}
		}
	}
}