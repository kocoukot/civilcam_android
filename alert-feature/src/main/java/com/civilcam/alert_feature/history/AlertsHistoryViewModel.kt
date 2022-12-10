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
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.model.alerts.VideoLoadingState
import com.civilcam.domainLayer.repos.VideoRepository
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.CheckIfVideoLoadedUseCase
import com.civilcam.domainLayer.usecase.alerts.GetAlertDetailUseCase
import com.civilcam.ext_features.KoinInjector
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.ext.serverPhoneNumberFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class AlertsHistoryViewModel(
    injector: KoinInjector,
    private val getAlertDetailUseCase: GetAlertDetailUseCase,
    private val downloadVideoUseCase: VideoRepository,
    private val checkIfVideoLoadedUseCase: CheckIfVideoLoadedUseCase,
) : BaseViewModel.Base<AlertHistoryState>(
    mState = MutableStateFlow(AlertHistoryState())
), KoinInjector by injector {

    var searchList = loadAlertHistoryList()

    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            AlertHistoryActions.ClickGoBack -> goBack()
            is AlertHistoryActions.ClickGoAlertDetails -> goAlertDetails(action.alertId)
            is AlertHistoryActions.ClickAlertTypeChange -> changeAlertType(action.alertType)
            AlertHistoryActions.CLickCallUser -> getState().alertDetailModel?.alertModel?.userInfo?.personPhone?.let { phoneNumber ->
                sendRoute(AlertHistoryRoute.CallUser(phoneNumber.serverPhoneNumberFormat()))
            }
            AlertHistoryActions.ClickShowVideoList -> showDownloadScreen()
            AlertHistoryActions.StopRefresh -> stopRefresh()
            AlertHistoryActions.ClearErrorText -> clearErrorText()
            is AlertHistoryActions.ClickOpenVideo -> {}
            is AlertHistoryActions.ClickDownloadVideo -> startDownLoading(action.videoToLoad)
        }
    }

    private fun startDownLoading(videoToLoad: AlertDetailModel.DownloadInfo) {
        getState().alertDetailModel?.alertDownloads?.takeIf { it.isNotEmpty() && videoToLoad.url?.isNotEmpty() == true }
            ?.let { videosList ->
                videosList.find { it.id == videoToLoad.id }?.videoState = VideoLoadingState.Loading
                downloadVideoUseCase.downloadVideo(
                    videoToLoad.url!!, "${getState().alertDetailModel?.getNameForVideo()}",
                    videoToLoad.id
                )
                updateInfo {
                    copy(
                        alertDetailModel = getState().alertDetailModel?.copy(
                            alertDownloads = videosList, updateUI = (1..100).random()
                        )
                    )
                }
            }
    }

    private fun showDownloadScreen() {
        updateInfo { copy(alertHistoryScreen = AlertHistoryScreen.VIDEO_DOWNLOAD) }
    }

    private fun goBack() {
        when (getState().alertHistoryScreen) {
            AlertHistoryScreen.HISTORY_LIST -> sendRoute(AlertHistoryRoute.GoBack)
            AlertHistoryScreen.HISTORY_DETAIL -> {
                updateInfo { copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_LIST) }
            }
            AlertHistoryScreen.VIDEO_DOWNLOAD -> updateInfo { copy(alertHistoryScreen = AlertHistoryScreen.HISTORY_DETAIL) }
        }
    }

    private fun goAlertDetails(alertId: Int) {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { getAlertDetailUseCase(alertId) },
            onSuccess = { detail ->
                detail.alertDownloads.forEach { video ->
                    viewModelScope.launch {
                        kotlin.runCatching { checkIfVideoLoadedUseCase(detail.getNameForVideo()) }
                            .onSuccess { result ->
                                if (result) video.videoState = VideoLoadingState.DownLoaded
                            }

                    }
                }

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
        return Pager(config = PagingConfig(
            pageSize = 40,
            initialLoadSize = 20,
            prefetchDistance = 6
        ),
            pagingSourceFactory = {
                koin.get<AlertHistoryListDataSource> { parametersOf(getState().alertType.domain) }
            }).flow.cachedIn(viewModelScope)
    }

    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun stopRefresh() {
        updateInfo { copy(refreshList = null) }
    }


}