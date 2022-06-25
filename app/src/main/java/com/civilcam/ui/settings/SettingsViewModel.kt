package com.civilcam.ui.settings

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.settings.NotificationsType
import com.civilcam.ui.settings.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel : ComposeViewModel<SettingsState, SettingsRoute, SettingsActions>() {

    override var _state = MutableStateFlow(SettingsState())

    init {

    }

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
                    _steps.value = SettingsRoute.GoLanguageSelect

                } else goBack()
            }


            SettingsActions.ClickChangePassword -> TODO()
            SettingsActions.ClickContactSupport -> TODO()
            SettingsActions.ClickDeleteAccount -> TODO()
            SettingsActions.ClickLanguage -> TODO()
            SettingsActions.ClickLogOut -> TODO()
            SettingsActions.ClickSubscription -> TODO()
            SettingsActions.ClickTermsAndPolicy -> TODO()

            SettingsActions.ClickAlerts -> TODO()
        }
    }


    private fun goBack() {
        when (_state.value.settingsType) {
            SettingsType.MAIN -> _steps.value = SettingsRoute.GoBack
            else -> {
                _state.value = SettingsState()
//                _state.value = _state.value.copy(settingsType = SettingsType.MAIN)
            }
        }
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
//            _state.value.alertsSectionModel?.let { model ->
//                model.notificationList.find { it.type == notifyType }?.isOn = status
////                _state.value = _state.value.copy(alertsSectionModel = model.copy())
//                var data =_state.value
//                data = data.copy(alertsSectionModel = model)
//                _state.emit(data)
//                Timber.d("updateSettingsModel ${_state.value}")
//            }
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

}