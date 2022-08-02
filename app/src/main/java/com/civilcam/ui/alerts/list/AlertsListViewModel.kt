package com.civilcam.ui.alerts.list

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import com.civilcam.ui.alerts.list.model.AlertListActions
import com.civilcam.ui.alerts.list.model.AlertListRoute
import com.civilcam.ui.alerts.list.model.AlertListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AlertsListViewModel(
    private val getAlertsListUseCase: GetAlertsListUseCase
) : ComposeViewModel<AlertListState, AlertListRoute, AlertListActions>() {

    override var _state: MutableStateFlow<AlertListState> = MutableStateFlow(AlertListState())

    init {

    }

    private fun getAlertsList() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            kotlin.runCatching { getAlertsListUseCase.getAlerts() }
                .onSuccess { user ->
                    _state.value = _state.value.copy(data = user)
                }
                .onFailure {
                    _state.value = _state.value.copy(errorText = it.localizedMessage)
                }
            _state.value = _state.value.copy(isLoading = false)
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
        _steps.value = AlertListRoute.GoMyProfile
    }

    private fun goSettings() {
        _steps.value = AlertListRoute.GoSettings
    }

    private fun goAlertHistory() {
        _steps.value = AlertListRoute.GoAlertHistory
    }

    private fun goUserProfile(userId: Int) {
        _steps.value = AlertListRoute.GoUserAlert(userId)
    }

    private fun showResolveAlert(userId: Int) {
        _state.value = _state.value.copy(resolveId = userId)
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
                _state.value = _state.value.copy(data = data.toList())
                _state.value = _state.value.copy(resolveId = null)
            }
        } else {
            _state.value = _state.value.copy(resolveId = null)
        }

    }


}