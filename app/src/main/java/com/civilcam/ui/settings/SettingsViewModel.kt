package com.civilcam.ui.settings

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.LanguageType
import com.civilcam.domainLayer.model.settings.NotificationsType
import com.civilcam.domainLayer.usecase.settings.GetCurrentSubscriptionPlanUseCase
import com.civilcam.domainLayer.usecase.user.ChangePasswordUseCase
import com.civilcam.domainLayer.usecase.user.CheckCurrentPasswordUseCase
import com.civilcam.domainLayer.usecase.user.LogoutUseCase
import com.civilcam.domainLayer.usecase.user.SetUserLanguageUseCase
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
    private val logoutUseCase: LogoutUseCase
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
                if (action.isConfirm) doActionOnAccount(action.isConfirm) else goBack()
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
            SettingsActions.ClearErrorText -> hideAlert()
        }
    }

    private fun onLanguageChange(languageType: LanguageType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            kotlin.runCatching { setUserLanguageUseCase(languageType) }
                .onSuccess { goBack() }
                .onFailure { error ->
                    error as ServiceException
                    _state.update { it.copy(errorText = error.errorMessage) }
                }
            _state.update { it.copy(isLoading = false) }

        }
    }

    private fun hideAlert() {
        _state.update { it.copy(errorText = "") }
    }

    private fun goSubManage() {
        navigateRoute(SettingsRoute.GoSubManage)
    }

    private fun doActionOnAccount(isLogOut: Boolean) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching {
                if (isLogOut) logoutUseCase() else logoutUseCase()
            }
                .onSuccess { navigateRoute(SettingsRoute.GoLanguageSelect) }
                .onFailure { error ->
                    error as ServiceException
                    _state.update { it.copy(errorText = error.errorMessage) }
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


    private fun goBack() {
        when (_state.value.settingsType) {
            SettingsType.MAIN -> navigateRoute(SettingsRoute.GoBack)
            SettingsType.CREATE_PASSWORD -> {
                _state.value = _state.value.copy(settingsType = SettingsType.CHANGE_PASSWORD)
            }
            else -> {
                _state.value = SettingsState()
            }
        }
    }

    private fun changeSection(section: SettingsType) {
        when (section) {
            SettingsType.ALERTS -> {
                _state.update {
                    it.copy(
                        settingsType = section,
                        data = SettingsModel(alertsSectionData = getNotificationsTypeList())
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
            else -> {
                _state.update { it.copy(settingsType = section) }
            }
        }
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


    private fun getNotificationsTypeList() = SettingsAlertsSectionData(isSMS = true, isEmail = true)


    private fun updateSettingsModel(model: SettingsModel) {
        _state.update { it.copy(data = SettingsModel(alertsSectionData = model.alertsSectionData)) }

    }

    private fun setContactSupportInfo(
        issueTheme: String,
        issueDescription: String,
        replyEmail: String,
    ) {
        var data = _state.value.data
        data.let { settingsData ->
            data = data.copy(
                contactSupportSectionData = ContactSupportSectionData(
                    issueTheme = issueTheme,
                    issueDescription = issueDescription,
                    replyEmail = replyEmail,
                )
            )

            _state.update { it.copy(data = settingsData) }
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
                hasError = false
            )
        )
        _state.update { it.copy(data = data) }
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
                        error as ServiceException
                        _state.update { it.copy(errorText = error.errorMessage) }
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
            PasswordInputDataType.PASSWORD -> {
                passwordData = passwordData.copy(password = password, meetCriteria = meetCriteria)
            }
            PasswordInputDataType.PASSWORD_REPEAT ->
                passwordData = passwordData.copy(confirmPassword = password)
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
                    .onSuccess { _state.value = SettingsState() }
                    .onFailure { error ->
                        error as ServiceException
                        _state.update { it.copy(errorText = error.errorMessage) }
                    }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

