package com.civilcam.ui.alerts.history

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.domain.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.alerts.history.model.AlertHistoryRoute
import com.civilcam.ui.alerts.history.model.AlertHistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertsHistoryViewModel(
    private val getHistoryAlertListUseCase: GetHistoryAlertListUseCase
) : ComposeViewModel<AlertHistoryState, AlertHistoryRoute, AlertHistoryActions>() {

    override var _state: MutableStateFlow<AlertHistoryState> = MutableStateFlow(AlertHistoryState())

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            kotlin.runCatching { getHistoryAlertListUseCase.getAlerts(_state.value.alertType) }
                .onSuccess { user ->
                    _state.value = _state.value.copy(data = user)
                }
                .onFailure {
                    _state.value = _state.value.copy(errorText = it.localizedMessage)
                }
            _state.value = _state.value.copy(isLoading = false)
        }


    }

    override fun setInputActions(action: AlertHistoryActions) {
        when (action) {
            AlertHistoryActions.ClickGoBack -> goBack()
            is AlertHistoryActions.ClickGoAlertDetails -> goAlertDetails(action.alertId)
            is AlertHistoryActions.ClickAlertTypeChange -> changeAlertType(action.alertType)
        }
    }


    private fun goBack() {
        _steps.value = AlertHistoryRoute.GoBack
    }

    private fun goAlertDetails(alertId: Int) {
        _steps.value = AlertHistoryRoute.GoAlertDetails(alertId)
    }

    private fun changeAlertType(alertType: AlertType) {
        _state.value = _state.value.copy(isLoading = true)
        _state.value = _state.value.copy(alertType = alertType)
        viewModelScope.launch {
            kotlin.runCatching { getHistoryAlertListUseCase.getAlerts(alertType) }
                .onSuccess { user ->
                    _state.value = _state.value.copy(data = user)
                }
                .onFailure {
                    _state.value = _state.value.copy(errorText = it.localizedMessage)
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }


}