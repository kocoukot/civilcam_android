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
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class AlertsListViewModel(
    injector: KoinInjector,
    private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase,
) : BaseViewModel.Base<AlertListState>(
    mState = MutableStateFlow(AlertListState())
), KoinInjector by injector {

    var searchList = loadAlertsList()

    fun loadAvatar() {
        getLocalCurrentUserUseCase().let { user ->
            updateInfo { copy(userAvatar = user.userBaseInfo.avatar) }
        }
    }

    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            AlertListActions.ClickGoMyProfile -> sendRoute(AlertListRoute.GoMyProfile)
            AlertListActions.ClickGoSettings -> goSettings()
            is AlertListActions.ClickResolveAlert -> showResolveAlert(action.alertId)
            is AlertListActions.ClickGoUserProfile -> goUserProfile(action.alertId)
            AlertListActions.ClickGoAlertsHistory -> goAlertHistory()
            is AlertListActions.ClickConfirmResolve -> resolveAlertResult()
            AlertListActions.ClearErrorText -> clearErrorText()
            AlertListActions.StopRefresh -> stopRefresh()
            is AlertListActions.SetErrorText -> updateInfo { copy(errorText = action.error) }
        }
    }

    private fun goSettings() {
        sendRoute(AlertListRoute.GoSettings)
    }

    private fun goAlertHistory() {
        sendRoute(AlertListRoute.GoAlertHistory)
    }

    private fun goUserProfile(userId: Int) {
        sendRoute(AlertListRoute.GoUserAlert(userId))
    }

    private fun showResolveAlert(userId: Int) {
        updateInfo { copy(resolveId = userId) }
    }

    private fun resolveAlertResult() {
        getState().resolveId?.let { alertId ->
            updateInfo { copy(isLoading = true) }
            networkRequest(
                action = { resolveAlertUseCase(alertId) },
                onSuccess = {
                    refreshList()
                },
                onFailure = { error ->
                    error.serviceCast { msg, _, _ -> updateInfo { copy(errorText = msg) } }
                },
                onComplete = { updateInfo { copy(isLoading = false) } },
            )
        }
    }

    fun refreshList() {
        updateInfo { copy(refreshList = Unit, resolveId = null) }
    }

    private fun loadAlertsList(): Flow<PagingData<AlertModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = { koin.get<AlertListDataSource>() }
        ).flow
            .cachedIn(viewModelScope)
    }

    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun stopRefresh() {
        updateInfo { copy(refreshList = null) }
    }
}