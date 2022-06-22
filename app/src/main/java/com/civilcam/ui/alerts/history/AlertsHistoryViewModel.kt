package com.civilcam.ui.alerts.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.domain.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.alerts.history.model.AlertHistoryRoute
import com.civilcam.ui.alerts.history.model.AlertHistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsHistoryViewModel(
    private val getHistoryAlertListUseCase: GetHistoryAlertListUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AlertHistoryState> = MutableStateFlow(AlertHistoryState())
    val state = _state.asStateFlow()

    private val _steps: SingleLiveEvent<AlertHistoryRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<AlertHistoryRoute> = _steps

    init {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
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

    fun setInputActions(action: AlertHistoryActions) {
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