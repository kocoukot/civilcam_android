package com.civilcam.ui.alerts.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.ui.alerts.history.content.AlertHistoryTabRow
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.common.compose.*
import com.civilcam.utils.DateUtils

@Composable
fun AlertsListScreenContent(viewModel: AlertsHistoryViewModel) {

    val state by viewModel.state.collectAsState()
    var tabPage by remember { mutableStateOf(AlertType.RECEIVED) }


    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(title = stringResource(id = R.string.alerts_history_title),
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

            AlertHistoryTabRow(tabPage) {
                tabPage = it
                viewModel.setInputActions(AlertHistoryActions.ClickAlertTypeChange(it))
            }
            state.data?.takeIf { it.isNotEmpty() }?.let { data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Divider(color = CCTheme.colors.grayThree)
                    }


                    itemsIndexed(data) { index, item ->
                        InformationRow(
                            title = if (state.alertType == AlertType.RECEIVED) item.userInfo.userName else stringResource(
                                id = R.string.alerts_history_sent_alert
                            ),
                            text = DateUtils.getFullDateAndTimeString(item.alertDate),
                            needDivider = index < data.lastIndex,
                            leadingIcon = {
                                if (state.alertType == AlertType.RECEIVED)
                                    CircleUserAvatar(item.userInfo.avatar, 36)
                            },
                            rowClick = {
                                viewModel.setInputActions(
                                    AlertHistoryActions.ClickGoAlertDetails(
                                        item.alertId
                                    )
                                )
                            },
                        )
                    }

                    item {
                        Divider(color = CCTheme.colors.grayThree)
                    }
                }
            } ?: kotlin.run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.setInputActions(AlertHistoryActions.ClickGetMockLis) },
                    contentAlignment = Alignment.Center
                ) {
                    EmptyListText(
                        stringResource(id = if (state.alertType == AlertType.RECEIVED) R.string.alerts_list_received_empty_state else R.string.alerts_list_sent_empty_state)
                    )
                }
            }

        }
    }
}