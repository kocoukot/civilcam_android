package com.civilcam.alert_feature.history.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.history.model.AlertHistoryActions
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.compose.elements.CircleUserAvatar
import com.civilcam.ext_features.compose.elements.EmptyListText
import com.civilcam.ext_features.compose.elements.InformationRow
import com.civilcam.ext_features.compose.elements.RowDivider

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


                itemsIndexed(data, key = { _, item -> item.alertId }) { index, item ->
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