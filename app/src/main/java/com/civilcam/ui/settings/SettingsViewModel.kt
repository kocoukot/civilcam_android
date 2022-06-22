package com.civilcam.ui.settings

import androidx.lifecycle.ViewModel
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsRoute
import com.civilcam.ui.settings.model.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _steps: SingleLiveEvent<SettingsRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<SettingsRoute> = _steps


    fun setInputActions(action: SettingsActions) {
        when (action) {
            SettingsActions.ClickGoBack -> goBack()
        }
    }


    private fun goBack() {
        _steps.value = SettingsRoute.GoBack
    }

//    private fun goSettings() {
//        _steps.value = AlertListRoute.GoSettings
//    }
//
//    private fun goAlertHistory() {
//        _steps.value = AlertListRoute.GoAlertHistory
//    }
//
//    private fun goUserProfile(userId: String) {
//        _steps.value = AlertListRoute.GoUserProfile(userId)
//    }

}