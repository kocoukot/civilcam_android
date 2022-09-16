package com.civilcam.alert_feature.history

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.alert_feature.history.model.AlertHistoryRoute
import com.civilcam.alert_feature.history.model.AlertHistoryScreen
import com.civilcam.alert_feature.history.model.AlertHistoryState
import com.civilcam.alert_feature.history.source.AlertHistoryListDataSource
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetAlertDetailUseCase
import com.civilcam.ext_features.KoinInjector
import com.civilcam.ext_features.compose.ComposeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import org.koin.core.parameter.parametersOf

class AlertsHistoryViewModel(
    injector: KoinInjector,
    private val getAlertDetailUseCase: GetAlertDetailUseCase
) : ComposeViewModel<AlertHistoryState, AlertHistoryRoute, AlertHistoryActions>(),
    KoinInjector by injector {

    override var _state: MutableStateFlow<AlertHistoryState> = MutableStateFlow(AlertHistoryState())
    var searchList = loadAlertHistoryList()

    override fun setInputActions(action: AlertHistoryActions) {
        when (action) {
            AlertHistoryActions.ClickGoBack -> goBack()
            is AlertHistoryActions.ClickGoAlertDetails -> goAlertDetails(action.alertId)
            is AlertHistoryActions.ClickAlertTypeChange -> changeAlertType(action.alertType)
            AlertHistoryActions.CLickCallUser -> {}
            AlertHistoryActions.CLickUploadVideo -> {}
            is AlertHistoryActions.SetErrorText -> TODO()
            AlertHistoryActions.StopRefresh -> stopRefresh()
            AlertHistoryActions.ClearErrorText -> clearErrorText()
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
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { getAlertDetailUseCase(alertId) },
            onSuccess = { detail ->
                _state.update {
                    it.copy(
                        alertHistoryScreen = AlertHistoryScreen.HISTORY_DETAIL,
                        alertDetailModel = detail
                    )
                }
            },
            onFailure = { error ->
                error.serviceCast { msg, _, _ -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = {
                _state.update { it.copy(isLoading = false) }
            },
        )
    }

    private fun changeAlertType(alertType: AlertType) {
        searchList = emptyFlow()
        _state.update { it.copy(alertType = alertType) }
        searchList = loadAlertHistoryList()
        _state.update { it.copy(refreshList = Unit) }
    }

    private fun loadAlertHistoryList(): Flow<PagingData<AlertModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = {
                koin.get<AlertHistoryListDataSource> { parametersOf(_state.value.alertType.domain) }
            }
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