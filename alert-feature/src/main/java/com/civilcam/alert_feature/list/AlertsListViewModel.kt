package com.civilcam.alert_feature.list

import androidx.lifecycle.viewModelScope
import com.civilcam.alert_feature.list.model.AlertListActions
import com.civilcam.alert_feature.list.model.AlertListRoute
import com.civilcam.alert_feature.list.model.AlertListState
import com.civilcam.domainLayer.model.alerts.AlertStatus
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetAlertsListUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertsListViewModel(
    private val getAlertsListUseCase: GetAlertsListUseCase,
    getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
) : ComposeViewModel<AlertListState, AlertListRoute, AlertListActions>() {

    override var _state: MutableStateFlow<AlertListState> = MutableStateFlow(AlertListState())

    init {
        getLocalCurrentUserUseCase().let { user ->
            _state.update { it.copy(userAvatar = user.userBaseInfo.avatar) }
        }
        getAlertsList()
    }

    private fun getAlertsList() {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { getAlertsListUseCase() },
            onSuccess = { user ->
                _state.update { it.copy(data = user) }
            },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } },
        )
    }

    override fun setInputActions(action: AlertListActions) {
        when (action) {
            AlertListActions.ClickGoMyProfile -> goMyProfile()
            AlertListActions.ClickGoSettings -> goSettings()
            is AlertListActions.ClickResolveAlert -> showResolveAlert(action.alertId)
            is AlertListActions.ClickGoUserProfile -> goUserProfile(action.alertId)
            AlertListActions.ClickGoAlertsHistory -> goAlertHistory()
            is AlertListActions.ClickConfirmResolve -> alertResult(action.result)
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
                    }?.alertStatus = AlertStatus.RESOLVED
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