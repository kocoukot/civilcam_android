package com.civilcam.alert_feature.history

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.civilcam.alert_feature.history.content.AlertHistoryDetailScreenContent
import com.civilcam.alert_feature.history.content.AlertHistoryListScreenContent
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.alert_feature.history.model.AlertHistoryScreen
import com.civilcam.ext_features.compose.elements.BackButton
import com.civilcam.ext_features.compose.elements.TopAppBarContent
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AlertsListScreenContent(viewModel: AlertsHistoryViewModel) {

    val state by viewModel.state.collectAsState()
    val alertList = viewModel.searchList.collectAsLazyPagingItems()

    if (state.refreshList == Unit) {
        alertList.refresh()
        viewModel.setInputActions(AlertHistoryActions.StopRefresh)
    }

    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(
                title = stringResource(id = state.alertHistoryScreen.screenTitle),
                navigationItem = {
                    BackButton {
                        viewModel.setInputActions(AlertHistoryActions.ClickGoBack)
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Crossfade(targetState = state.alertHistoryScreen) { screenState ->
                when (screenState) {
                    AlertHistoryScreen.HISTORY_LIST -> {
                        AlertHistoryListScreenContent(
                            onScreenAction = viewModel::setInputActions,
                            alertListData = alertList,
                            alertType = state.alertType
                        )
                    }
                    AlertHistoryScreen.HISTORY_DETAIL -> {
                        AlertHistoryDetailScreenContent(
                            onScreenAction = viewModel::setInputActions,
                            alertType = state.alertType
                        )
                    }
                }
            }
        }
    }
}