package com.civilcam.alert_feature.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.civilcam.alert_feature.list.model.AlertListActions
import com.civilcam.alert_feature.list.model.AlertListRoute
import com.civilcam.alert_feature.list.model.AlertListState
import com.civilcam.alert_feature.list.source.AlertListDataSource
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.ResolveAlertUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.KoinInjector
import com.civilcam.ext_features.compose.ComposeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AlertsListViewModel(
    injector: KoinInjector,
    getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase,
) : ComposeViewModel<AlertListState, AlertListRoute, AlertListActions>(),
    KoinInjector by injector {

    override var _state: MutableStateFlow<AlertListState> = MutableStateFlow(AlertListState())
    var searchList = loadAlertsList()

    init {
        getLocalCurrentUserUseCase().let { user ->
            _state.update { it.copy(userAvatar = user.userBaseInfo.avatar) }
        }
    }

    override fun setInputActions(action: AlertListActions) {
        when (action) {
            AlertListActions.ClickGoMyProfile -> goMyProfile()
            AlertListActions.ClickGoSettings -> goSettings()
            is AlertListActions.ClickResolveAlert -> showResolveAlert(action.alertId)
            is AlertListActions.ClickGoUserProfile -> goUserProfile(action.alertId)
            AlertListActions.ClickGoAlertsHistory -> goAlertHistory()
            is AlertListActions.ClickConfirmResolve -> resolveAlertResult()
            AlertListActions.ClearErrorText -> clearErrorText()
            AlertListActions.StopRefresh -> stopRefresh()
            is AlertListActions.SetErrorText -> _state.update { it.copy(errorText = action.error) }
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

    private fun resolveAlertResult() {
        _state.value.resolveId?.let { alertId ->
            _state.update { it.copy(isLoading = true) }
            networkRequest(
                action = { resolveAlertUseCase(alertId) },
                onSuccess = {
                    _state.update { it.copy(refreshList = Unit, resolveId = null) }
                },
                onFailure = { error ->
                    error.serviceCast { msg, _, _ -> _state.update { it.copy(errorText = msg) } }
                },
                onComplete = { _state.update { it.copy(isLoading = false) } },
            )
        }

    }

    private fun loadAlertsList(): Flow<PagingData<AlertModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { koin.get<AlertListDataSource>() }
        ).flow
            .cachedIn(viewModelScope)
    }

    override fun clearErrorText() {
        _state.update { it.copy(errorText = "") }
    }

    private fun stopRefresh() {
        _state.update { it.copy(refreshList = null) }
    }
}