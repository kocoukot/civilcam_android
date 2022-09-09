package com.civilcam.ui.settings

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.serviceCast
import com.civilcam.domainLayer.model.AuthType
import com.civilcam.domainLayer.model.ScreenAlert
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.domainLayer.model.user.SettingsNotificationType
import com.civilcam.domainLayer.usecase.subscriptions.GetCurrentSubscriptionPlanUseCase
import com.civilcam.domainLayer.usecase.user.*
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.settings.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val checkCurrentPasswordUseCase: CheckCurrentPasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val setUserLanguageUseCase: SetUserLanguageUseCase,
    private val getCurrentSubscriptionPlan: GetCurrentSubscriptionPlanUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
    private val contactSupportUseCase: ContactSupportUseCase,
    private val toggleSettingsUseCase: ToggleSettingsUseCase,
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
            is SettingsActions.ClickSaveLanguage -> onLanguageChange(action.languageType)
            is SettingsActions.ClickCloseAlertDialog -> {
                if (action.isConfirm) doActionOnAccount(action.isLogOut) else goBack()
            }
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
            SettingsActions.ClearErrorText -> clearErrorText()
            SettingsActions.ClickCloseScreenAlert -> closeScreenAlert()
        }
    }

    private fun closeScreenAlert() {
        _state.update { it.copy(screenAlert = null) }
    }

    private fun onLanguageChange(languageType: LanguageType) {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { setUserLanguageUseCase(languageType) },
            onSuccess = { goBack() },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } }
        )
    }

    override fun clearErrorText() {
        _state.update { it.copy(errorText = "") }
    }

    private fun goSubManage() {
        navigateRoute(SettingsRoute.GoSubManage)
    }

    private fun doActionOnAccount(isLogOut: Boolean) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { if (isLogOut) logoutUseCase() else deleteAccountUseCase() }
                .onSuccess { navigateRoute(SettingsRoute.GoLanguageSelect) }
                .onFailure { error ->
                    error.serviceCast { msg, _, isForceLogout ->
                        if (isForceLogout) navigateRoute(SettingsRoute.ForceLogout)
                        _state.update { it.copy(errorText = msg) }
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun fetchSubscriptionPlan() {
        viewModelScope.launch {
            kotlin.runCatching { getCurrentSubscriptionPlan.getCurrentSubscriptionPlan() }
                .onSuccess {
                    val response = getCurrentSubscriptionPlan.getCurrentSubscriptionPlan()
                    _state.value.data = _state.value.data.copy(subscriptionData = response)
                }
                .onFailure {

                }
        }
    }


    private fun goBack(screenAlert: ScreenAlert? = null) {
        when (_state.value.settingsType) {
            SettingsType.MAIN -> navigateRoute(SettingsRoute.GoBack)
            SettingsType.CREATE_PASSWORD -> {
                _state.value = _state.value.copy(settingsType = SettingsType.CHANGE_PASSWORD)
            }
            else -> _state.value = SettingsState()
        }
        _state.update { it.copy(screenAlert = screenAlert) }
    }

    private fun changeSection(section: SettingsType) {
        when (section) {
            SettingsType.ALERTS -> {
                viewModelScope.launch {
                    getLocalCurrentUserUseCase().settings.let { settings ->
                        Timber.i("getLocalCurrentUserUseCase ${getLocalCurrentUserUseCase.invoke()}")
                        _state.update {
                            it.copy(
                                settingsType = section,
                                data = it.data.copy(
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
                val user = getLocalCurrentUserUseCase()
                _state.update {
                    it.copy(
                        settingsType = section,
                        data = it.data.copy(
                            contactSupportSectionData = ContactSupportSectionData(
                                replyEmail = if (user.sessionUser.authType == AuthType.email) user.sessionUser.email else "",
                                canChangeEmail = getLocalCurrentUserUseCase().sessionUser.canChangeEmail
                            )
                        )
                    )
                }
            }
            SettingsType.CHANGE_PASSWORD -> {
                _state.update {
                    it.copy(
                        settingsType = section,
                        data = SettingsModel(changePasswordSectionData = ChangePasswordSectionData())
                    )
                }
            }
            SettingsType.TERMS_AND_POLICY -> navigateRoute(SettingsRoute.GoTerms)
            else -> _state.update { it.copy(settingsType = section) }
        }
    }

    private fun notificationChanged(status: Boolean, notifyType: SettingsNotificationType) {
        Timber.d("updateSettingsModel ${_state.value}")
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { toggleSettingsUseCase(type = notifyType.domain, isOn = status) }
                .onSuccess {
                    _state.update {
                        it.copy(
                            data = _state.value.data.copy(
                                alertsSectionData = when (notifyType) {
                                    SettingsNotificationType.SMS -> _state.value.data.alertsSectionData.copy(
                                        isSMS = status
                                    )
                                    else -> _state.value.data.alertsSectionData.copy(isEmail = status)
                                }
                            )
                        )
                    }
                }
                .onFailure { error ->
                    error.serviceCast { msg, _, isForceLogout ->
                        if (isForceLogout) navigateRoute(SettingsRoute.ForceLogout)
                        _state.update { it.copy(errorText = msg) }
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun setContactSupportInfo(
        issueTheme: String,
        issueDescription: String,
        replyEmail: String,
    ) {
        _state.update {
            it.copy(
                data =
                _state.value.data.copy(
                    contactSupportSectionData = _state.value.data.contactSupportSectionData.copy(
                        issueTheme = issueTheme,
                        issueDescription = issueDescription,
                        replyEmail = replyEmail
                    )
                )
            )
        }
        Timber.i("setContactSupportInfo ${_state.value.data}  issueTheme $issueTheme")
    }


    private fun contactSupport() {
        _state.update { it.copy(isLoading = true) }
        _state.value.data.contactSupportSectionData.let {
            viewModelScope.launch {
                kotlin.runCatching {
                    contactSupportUseCase(
                        issue = it.issueTheme,
                        text = it.issueDescription,
                        email = it.replyEmail,
                    )
                }
                    .onSuccess { goBack(ScreenAlert.ReportSentAlert) }
                    .onFailure { error ->
                        error.serviceCast { msg, _, isForceLogout ->
                            if (isForceLogout) navigateRoute(SettingsRoute.ForceLogout)
                            _state.update { it.copy(errorText = msg) }
                        }
                    }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun enteredCurrentPassword(password: String) {
        _state.update {
            it.copy(
                data = _state.value.data.copy(
                    changePasswordSectionData = ChangePasswordSectionData(
                        currentPassword = password,
                        hasError = false
                    )
                )
            )
        }
    }

    private fun checkCurrentPassword() {
        _state.value.data.changePasswordSectionData?.let { data ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                kotlin.runCatching { checkCurrentPasswordUseCase(data.currentPassword) }
                    .onSuccess { isCorrect ->
                        Timber.d("checkCurrentPassword $isCorrect")
                        if (isCorrect) _state.update { it.copy(settingsType = SettingsType.CREATE_PASSWORD) }
                        else {
                            _state.update {
                                it.copy(
                                    data = _state.value.data.copy(
                                        changePasswordSectionData = _state.value.data.changePasswordSectionData?.copy(
                                            hasError = true
                                        )
                                    )
                                )
                            }
                        }
                    }
                    .onFailure { error ->
                        error.serviceCast { msg, _, isForceLogout ->
                            _state.update { it.copy(errorText = msg) }
                        }
                    }
                _state.update { it.copy(isLoading = false) }

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
            PasswordInputDataType.PASSWORD -> passwordData =
                passwordData.copy(password = password, meetCriteria = meetCriteria)
            PasswordInputDataType.PASSWORD_REPEAT -> passwordData =
                passwordData.copy(confirmPassword = password)
            else -> {}
        }
        _state.update { it.copy(data = _state.value.data.copy(createPasswordSectionData = passwordData)) }
        Timber.d("passwordEntered ${_state.value.data}")
    }


    private fun savePassword() {
        _state.value.data.changePasswordSectionData?.currentPassword?.let { currentPassword ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                kotlin.runCatching {
                    changePasswordUseCase(
                        currentPassword = currentPassword,
                        newPassword = _state.value.data.createPasswordSectionData.password,
                    )
                }
                    .onSuccess {
                        _state.value = SettingsState(screenAlert = ScreenAlert.PasswordChangedAlert)
                    }
                    .onFailure { error ->
                        error.serviceCast { msg, _, isForceLogout ->
                            _state.update { it.copy(errorText = msg) }
                        }
                    }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}