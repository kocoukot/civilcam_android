package com.civilcam.ui.settings

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.settings.NotificationsType
import com.civilcam.domain.usecase.settings.CheckCurrentPasswordUseCase
import com.civilcam.ui.settings.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val checkCurrentPasswordUseCase: CheckCurrentPasswordUseCase
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
                        _steps.value = SettingsRoute.GoLanguageSelect // todo add api
                    } else {
                        _steps.value = SettingsRoute.GoLanguageSelect// todo add api
                    }
                } else goBack()
            }
            is SettingsActions.IsNavBarVisible -> navBarStatus(action.isVisible)
            SettingsActions.ClickSendToSupport -> contactSupport()
            is SettingsActions.EnterContactSupportInfo -> setContactSupportInfo(
                action.issue,
                action.description,
                action.email
            )
            SettingsActions.CheckCurrentPassword -> checkCurrentPassword()
            is SettingsActions.EnterCurrentPassword -> enteredCurrentPassword(action.password)
        }
    }


    private fun goBack() {
        when (_state.value.settingsType) {
            SettingsType.MAIN -> _steps.value = SettingsRoute.GoBack
            else -> {
                _state.value = SettingsState()
            }
        }
    }

    private fun navBarStatus(isVisible: Boolean) {
        _steps.value = SettingsRoute.IsNavBarVisible(isVisible)
    }

    private fun changeSection(section: SettingsType) {
        when (section) {
//            SettingsType.MAIN -> TODO()
            SettingsType.ALERTS -> {
                _state.value = _state.value.copy(settingsType = section)
                _state.value = _state.value.copy(
                    data = SettingsModel(alertsSectionData = getNotificationsTypeList())
                )
            }
//            SettingsType.SUBSCRIPTION -> TODO()
//            SettingsType.CHANGE_PASSWORD -> TODO()
//            SettingsType.CONTACT_SUPPORT -> TODO()
            SettingsType.TERMS_AND_POLICY -> _steps.value = SettingsRoute.GoTerms
            else -> _state.value = _state.value.copy(settingsType = section)
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
}

