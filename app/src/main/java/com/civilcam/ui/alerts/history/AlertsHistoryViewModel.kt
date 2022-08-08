package com.civilcam.ui.alerts.history

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.alerts.history.model.AlertHistoryRoute
import com.civilcam.ui.alerts.history.model.AlertHistoryScreen
import com.civilcam.ui.alerts.history.model.AlertHistoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertsHistoryViewModel(
    private val getHistoryAlertListUseCase: com.civilcam.domainLayer.usecase.alerts.GetHistoryAlertListUseCase,
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

    private fun goBack() {
        when (_state.value.alertHistoryScreen) {
            AlertHistoryScreen.HISTORY_LIST -> navigateRoute(AlertHistoryRoute.GoBack)
            AlertHistoryScreen.HISTORY_DETAIL -> {
                _state.update {
                    it.copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_LIST)
                }
            }
        }

    }

    private fun goAlertDetails(alertId: Int) {
        _state.update { it.copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_DETAIL) }
    }

    private fun changeAlertType(alertType: com.civilcam.domainLayer.model.alerts.AlertType) {
        _state.value = _state.value.copy(alertType = alertType)
        if (_state.value.mockNeedToLoad) getAlertHistoryList()
    }


}