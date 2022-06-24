package com.civilcam.ui.settings

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsRoute
import com.civilcam.ui.settings.model.SettingsState
import com.civilcam.ui.settings.model.SettingsType
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : ComposeViewModel<SettingsState, SettingsRoute, SettingsActions>() {

    override var _state = MutableStateFlow(SettingsState())


    override fun setInputActions(action: SettingsActions) {
        when (action) {
            SettingsActions.ClickGoBack -> goBack()
            is SettingsActions.ClickSection -> changeSection(action.section)
            SettingsActions.ClickChangePassword -> TODO()
            SettingsActions.ClickContactSupport -> TODO()
            SettingsActions.ClickDeleteAccount -> TODO()
            SettingsActions.ClickLanguage -> TODO()
            SettingsActions.ClickLogOut -> TODO()
            SettingsActions.ClickSubscription -> TODO()
            SettingsActions.ClickTermsAndPolicy -> TODO()
        }
    }


    private fun goBack() {
        when (_state.value.settingsType) {
            SettingsType.MAIN -> _steps.value = SettingsRoute.GoBack
            else -> _state.value = _state.value.copy(settingsType = SettingsType.MAIN)
        }
    }

    private fun changeSection(section: SettingsType) {
        when (section) {
//            SettingsType.MAIN -> TODO()
//            SettingsType.ALERTS -> TODO()
//            SettingsType.SUBSCRIPTION -> TODO()
//            SettingsType.CHANGE_PASSWORD -> TODO()
//            SettingsType.LANGUAGE -> TODO()
//            SettingsType.CONTACT_SUPPORT -> TODO()
            SettingsType.TERMS_AND_POLICY -> _steps.value = SettingsRoute.GoTerms
//            SettingsType.LOG_OUT -> TODO()
//            SettingsType.DELETE_ACCOUNT -> TODO()
            else -> _state.value = _state.value.copy(settingsType = section)

        }

    }
}