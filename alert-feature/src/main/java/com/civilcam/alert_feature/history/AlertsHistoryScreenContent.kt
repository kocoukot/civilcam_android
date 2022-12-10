package com.civilcam.alert_feature.history

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.civilcam.alert_feature.history.content.AlertHistoryDetailScreenContent
import com.civilcam.alert_feature.history.content.AlertHistoryListScreenContent
import com.civilcam.alert_feature.history.content.VideoDownloadScreenContent
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.alert_feature.history.model.AlertHistoryScreen
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.compose.elements.AlertDialogComp
import com.civilcam.ext_features.compose.elements.BackButton
import com.civilcam.ext_features.compose.elements.DialogLoadingContent
import com.civilcam.ext_features.compose.elements.TopAppBarContent
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AlertsListScreenContent(viewModel: AlertsHistoryViewModel) {

    val state by viewModel.state.collectAsState()
    val alertList = viewModel.searchList.collectAsLazyPagingItems()

    BackHandler {
        viewModel.setInputActions(AlertHistoryActions.ClickGoBack)
    }

    var selectedVideo by remember {
        mutableStateOf<AlertHistoryActions?>(null)
    }

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionsMap ->
            if (permissionsMap.all { it.value }) {
                selectedVideo?.let { action ->
                    viewModel.setInputActions(action)
                }
            }
        }
    )

    val topAppBarColor by
    animateColorAsState(
        targetValue = if (state.alertHistoryScreen == AlertHistoryScreen.VIDEO_DOWNLOAD)
            CCTheme.colors.lightGray
        else
            CCTheme.colors.white
    )

    if (state.refreshList == Unit) {
        alertList.refresh()
        viewModel.setInputActions(AlertHistoryActions.StopRefresh)
    }

    if (state.isLoading) {
        DialogLoadingContent()
    }

    if (state.errorText.isNotEmpty()) {
        AlertDialogComp(
            dialogText = state.errorText,
            alertType = AlertDialogButtons.OK,
            onOptionSelected = { viewModel.setInputActions(AlertHistoryActions.ClearErrorText) }
        )
    }

    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(
                backgroundColor = topAppBarColor,
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
                            alertDetail = state.alertDetailModel,
                            onScreenAction = viewModel::setInputActions,
                            alertType = state.alertType
                        )
                    }
                    AlertHistoryScreen.VIDEO_DOWNLOAD -> {
                        VideoDownloadScreenContent(
                            state.alertDetailModel?.alertDownloads.orEmpty(),
//                            viewModel::setInputActions
                        ) { action ->
                            selectedVideo = action
                            viewModel.setInputActions(action)
//                            permissionRequest.launch(
//                                arrayOf(
//                                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                                )
//                            )

                        }
                    }
                }
            }
        }
    }
}