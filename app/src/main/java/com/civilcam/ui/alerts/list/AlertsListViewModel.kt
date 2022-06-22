package com.civilcam.ui.alerts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.domain.usecase.alerts.GetAlertsListUseCase
import com.civilcam.ui.alerts.list.model.AlertListActions
import com.civilcam.ui.alerts.list.model.AlertListRoute
import com.civilcam.ui.alerts.list.model.AlertListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AlertsListViewModel(
    getAlertsListUseCase: GetAlertsListUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<AlertListState> = MutableStateFlow(AlertListState())
    val state = _state.asStateFlow()

    private val _steps: SingleLiveEvent<AlertListRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<AlertListRoute> = _steps

    init {
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

    fun setInputActions(action: AlertListActions) {
        when (action) {
            AlertListActions.ClickGoMyProfile -> goMyProfile()
            AlertListActions.ClickGoSettings -> goSettings()
            is AlertListActions.ClickResolveAlert -> showResolveAlert(action.userId)
            is AlertListActions.ClickGoUserProfile -> goUserProfile(action.userId)
            AlertListActions.ClickGoAlertsHistory -> goAlertHistory()
            is AlertListActions.ClickConfirmResolve -> alertResult(action.result)
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
        _steps.value = AlertListRoute.GoUserProfile(userId)
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
                        Timber.d("alertResult ${it.alertId == _state.value.resolveId}")
                        it.alertId == _state.value.resolveId
                    }?.isResolved = true
                }
                Timber.d("alertResult $data ${_state.value.resolveId}")
                _state.value = _state.value.copy(data = data.toList())
                _state.value = _state.value.copy(resolveId = null)
            }
        } else {
            _state.value = _state.value.copy(resolveId = null)
        }

    }


}