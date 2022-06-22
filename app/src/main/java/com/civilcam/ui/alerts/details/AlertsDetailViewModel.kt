package com.civilcam.ui.alerts.details

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.usecase.alerts.GetHistoryDetailUseCase
import com.civilcam.ui.alerts.details.model.AlertDetailActions
import com.civilcam.ui.alerts.details.model.AlertDetailRoute
import com.civilcam.ui.alerts.details.model.AlertDetailState
import kotlinx.coroutines.flow.MutableStateFlow

class AlertsDetailViewModel(
    private val getHistoryDetailUseCase: GetHistoryDetailUseCase
) : ComposeViewModel<AlertDetailState, AlertDetailRoute, AlertDetailActions>() {

    override var _state: MutableStateFlow<AlertDetailState> = MutableStateFlow(AlertDetailState())


    init {
    }

    override fun setInputActions(action: AlertDetailActions) {
        when (action) {
            AlertDetailActions.ClickGoBack -> goBack()
            AlertDetailActions.ClickCallPhone -> TODO()
            AlertDetailActions.ClickDownloadVideo -> TODO()
        }
    }


    private fun goBack() {
//        _steps.value = AlertDetailRoute.GoBack
    }


}