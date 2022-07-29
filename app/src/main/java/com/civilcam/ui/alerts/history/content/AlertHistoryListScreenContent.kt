package com.civilcam.ui.alerts.history.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domain.model.alerts.AlertModel
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.common.compose.CircleUserAvatar
import com.civilcam.ui.common.compose.EmptyListText
import com.civilcam.ui.common.compose.InformationRow
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.utils.DateUtils

@Composable
fun AlertHistoryListScreenContent(
    onScreenAction: (AlertHistoryActions) -> Unit,
    alertListData: List<AlertModel>?,
    alertType: AlertType
) {
    var tabPage by remember { mutableStateOf(alertType) }

    Column(modifier = Modifier.fillMaxSize()) {
        AlertHistoryTabRow(tabPage) {
            tabPage = it
            onScreenAction.invoke(AlertHistoryActions.ClickAlertTypeChange(it))
        }
        alertListData?.takeIf { it.isNotEmpty() }?.let { data ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    RowDivider()
                }


                itemsIndexed(data) { index, item ->
                    InformationRow(
                        title = if (alertType == AlertType.RECEIVED) item.userInfo.userName else stringResource(
                            id = R.string.alerts_history_sent_alert
                        ),
                        text = DateUtils.getFullDateAndTimeString(item.alertDate),
                        needDivider = index < data.lastIndex,
                        leadingIcon = {
                            if (alertType == AlertType.RECEIVED)
                                CircleUserAvatar(item.userInfo.avatar, 36)
                        },
                        rowClick = {
                            onScreenAction.invoke(AlertHistoryActions.ClickGoAlertDetails(item.alertId))
                        },
                    )
                }

                item {
                    RowDivider()
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onScreenAction.invoke(AlertHistoryActions.ClickGetMockLis) },
                contentAlignment = Alignment.Center
            ) {
                EmptyListText(
                    stringResource(id = if (alertType == AlertType.RECEIVED) R.string.alerts_list_received_empty_state else R.string.alerts_list_sent_empty_state)
                )
            }
        }
    }


}