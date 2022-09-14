package com.civilcam.alert_feature.history

import androidx.lifecycle.viewModelScope
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.alert_feature.history.model.AlertHistoryRoute
import com.civilcam.alert_feature.history.model.AlertHistoryScreen
import com.civilcam.alert_feature.history.model.AlertHistoryState
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetHistoryAlertListUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertsHistoryViewModel(
    private val getHistoryAlertListUseCase: GetHistoryAlertListUseCase,
//    private val getAlertDetailUseCase: GetAlertDetailUseCase
) : ComposeViewModel<AlertHistoryState, AlertHistoryRoute, AlertHistoryActions>() {

    override var _state: MutableStateFlow<AlertHistoryState> = MutableStateFlow(AlertHistoryState())

    override fun setInputActions(action: AlertHistoryActions) {
        when (action) {
            AlertHistoryActions.ClickGoBack -> goBack()
            is AlertHistoryActions.ClickGoAlertDetails -> goAlertDetails(action.alertId)
            is AlertHistoryActions.ClickAlertTypeChange -> changeAlertType(action.alertType)
            AlertHistoryActions.ClickGetMockLis -> {
                _state.value = _state.value.copy(mockNeedToLoad = true)
                getAlertHistoryList()
            }
            AlertHistoryActions.CLickCallUser -> {}
            AlertHistoryActions.CLickUploadVideo -> {}
        }
    }

    private fun getAlertHistoryList() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            kotlin.runCatching { getHistoryAlertListUseCase.getAlerts(_state.value.alertType) }
                .onSuccess { user -> _state.update { it.copy(data = user) } }
                .onFailure { error ->
                    error.serviceCast { msg, _, isForceLogout ->
                        if (isForceLogout) navigateRoute(AlertHistoryRoute.ForceLogout)
                        _state.update { it.copy(errorText = msg) }
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun goBack() {
        when (_state.value.alertHistoryScreen) {
            AlertHistoryScreen.HISTORY_LIST -> navigateRoute(AlertHistoryRoute.GoBack)
            AlertHistoryScreen.HISTORY_DETAIL -> {
                _state.update { it.copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_LIST) }
            }
        }
    }

    private fun goAlertDetails(alertId: Int) {
        _state.update { it.copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_DETAIL) }
    }

    private fun changeAlertType(alertType: AlertType) {
        _state.update { it.copy(alertType = alertType) }
        if (_state.value.mockNeedToLoad) getAlertHistoryList()
    }

    override fun clearErrorText() {

    }
}