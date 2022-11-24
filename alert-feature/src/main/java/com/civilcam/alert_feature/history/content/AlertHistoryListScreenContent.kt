package com.civilcam.alert_feature.history.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.ext_features.DateUtils.alertDateFormat
import com.civilcam.ext_features.compose.elements.*
import timber.log.Timber

@Composable
fun AlertHistoryListScreenContent(
    onScreenAction: (AlertHistoryActions) -> Unit,
    alertListData: LazyPagingItems<AlertModel>,
    alertType: AlertType
) {
    var tabPage by remember { mutableStateOf(alertType) }

    Column(modifier = Modifier.fillMaxSize()) {
        AlertHistoryTabRow(tabPage) {
            tabPage = it
            onScreenAction.invoke(AlertHistoryActions.ClickAlertTypeChange(it))
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                if (alertListData.itemCount > 0) RowDivider()
            }

            itemsIndexed(alertListData, key = { _, item -> item.alertId }) { index, item ->
                item?.let {
                    InformationRow(
                        title = if (alertType == AlertType.RECEIVED)
                            item.userInfo?.personFullName ?: ""
                        else
                            stringResource(id = R.string.alerts_history_sent_alert),
                        text = alertDateFormat(item.alertDate),
                        needDivider = index < alertListData.itemCount - 1,
                        leadingIcon = {
                            if (alertType == AlertType.RECEIVED)
                                item.userInfo?.personAvatar?.imageUrl?.let { avatar ->
                                    CircleUserAvatar(avatar, 36)
                                }
                        },
                        rowClick = {
                            onScreenAction.invoke(AlertHistoryActions.ClickGoAlertDetails(item.alertId))
                        },
                    )
                }
            }
            if (alertListData.itemCount > 0)
                item {
                    RowDivider()
                }
        }

        alertListData.apply {
            Timber.d("lazyPlace loadState $loadState")
            when {
                loadState.source.refresh is LoadState.Loading -> DialogLoadingContent()
                loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        itemCount < 1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyListText(
                            stringResource(
                                id = if (alertType == AlertType.RECEIVED)
                                    R.string.alerts_list_received_empty_state
                                else
                                    R.string.alerts_list_sent_empty_state
                            )
                        )
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    (loadState.refresh as LoadState.Error).error.serviceCast { error, _, _ ->
//                        viewModel.setInputActions(AlertListActions.SetErrorText(error))
                    }
                }
                else -> {}
            }
        }
    }
}
