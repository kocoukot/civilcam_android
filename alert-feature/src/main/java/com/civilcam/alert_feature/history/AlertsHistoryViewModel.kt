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
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.ext.serverPhoneNumberFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.koin.core.parameter.parametersOf

class AlertsHistoryViewModel(
    injector: KoinInjector,
    private val getAlertDetailUseCase: GetAlertDetailUseCase
) : BaseViewModel.Base<AlertHistoryState>(
    mState = MutableStateFlow(AlertHistoryState())
), KoinInjector by injector {

    var searchList = loadAlertHistoryList()

    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            AlertHistoryActions.ClickGoBack -> goBack()
            is AlertHistoryActions.ClickGoAlertDetails -> goAlertDetails(action.alertId)
            is AlertHistoryActions.ClickAlertTypeChange -> changeAlertType(action.alertType)
            AlertHistoryActions.CLickCallUser -> getState()
                .alertDetailModel?.alertModel?.userInfo?.personPhone?.let { phoneNumber ->
                    sendRoute(AlertHistoryRoute.CallUser(phoneNumber.serverPhoneNumberFormat()))
                }
            AlertHistoryActions.CLickUploadVideo -> {} //todo
            AlertHistoryActions.StopRefresh -> stopRefresh()
            AlertHistoryActions.ClearErrorText -> clearErrorText()
        }
    }


    private fun goBack() {
        when (getState().alertHistoryScreen) {
            AlertHistoryScreen.HISTORY_LIST -> sendRoute(AlertHistoryRoute.GoBack)
            AlertHistoryScreen.HISTORY_DETAIL -> {
                updateInfo { copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_LIST) }
            }
        }
    }

    private fun goAlertDetails(alertId: Int) {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { getAlertDetailUseCase(alertId) },
            onSuccess = { detail ->
                updateInfo {
                    copy(
                        alertHistoryScreen = AlertHistoryScreen.HISTORY_DETAIL,
                        alertDetailModel = detail
                    )
                }
            },
            onFailure = { error ->
                error.serviceCast { msg, _, _ -> updateInfo { copy(errorText = msg) } }
            },
            onComplete = {
                updateInfo { copy(isLoading = false) }
            },
        )
    }

    private fun changeAlertType(alertType: AlertType) {
        searchList = emptyFlow()
        updateInfo { copy(alertType = alertType) }
        searchList = loadAlertHistoryList()
        updateInfo { copy(refreshList = Unit) }
    }

    private fun loadAlertHistoryList(): Flow<PagingData<AlertModel>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 20, prefetchDistance = 6),
            pagingSourceFactory = {
                koin.get<AlertHistoryListDataSource> { parametersOf(getState().alertType.domain) }
            }
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