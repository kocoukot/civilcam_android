package com.civilcam.alert_feature.list

import androidx.lifecycle.viewModelScope
import com.civilcam.alert_feature.list.model.AlertListActions
import com.civilcam.alert_feature.list.model.AlertListRoute
import com.civilcam.alert_feature.list.model.AlertListState
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetAlertsListUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertsListViewModel(
    private val getAlertsListUseCase: GetAlertsListUseCase
) : ComposeViewModel<AlertListState, AlertListRoute, AlertListActions>() {

    override var _state: MutableStateFlow<AlertListState> = MutableStateFlow(AlertListState())


    private fun getAlertsList() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { getAlertsListUseCase.getAlerts() }
                .onSuccess { user -> _state.update { it.copy(data = user) } }
                .onFailure { error ->
                    error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    override fun setInputActions(action: AlertListActions) {
        when (action) {
            AlertListActions.ClickGoMyProfile -> goMyProfile()
            AlertListActions.ClickGoSettings -> goSettings()
            is AlertListActions.ClickResolveAlert -> showResolveAlert(action.userId)
            is AlertListActions.ClickGoUserProfile -> goUserProfile(action.userId)
            AlertListActions.ClickGoAlertsHistory -> goAlertHistory()
            is AlertListActions.ClickConfirmResolve -> alertResult(action.result)
            AlertListActions.ClickGetMockLis -> getAlertsList()
        }
    }


    private fun goMyProfile() {
        navigateRoute(AlertListRoute.GoMyProfile)
    }

    private fun goSettings() {
        navigateRoute(AlertListRoute.GoSettings)
    }

    private fun goAlertHistory() {
        navigateRoute(AlertListRoute.GoAlertHistory)
    }

    private fun goUserProfile(userId: Int) {
        navigateRoute(AlertListRoute.GoUserAlert(userId))
    }

    private fun showResolveAlert(userId: Int) {
        _state.update { it.copy(resolveId = userId) }
    }

    private fun alertResult(isResolved: Boolean) {
        if (isResolved) {
            viewModelScope.launch {
                val data = _state.value.data?.toList() ?: emptyList()
                data.let { list ->
                    list.find {
                        it.alertId == _state.value.resolveId
                    }?.isResolved = true
                }
                _state.update { it.copy(data = data.toList(), resolveId = null) }
            }
        } else {
            _state.update { it.copy(resolveId = null) }
        }
    }

    override fun clearErrorText() {

    }
}