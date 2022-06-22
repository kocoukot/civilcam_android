package com.civilcam.ui.alerts.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.alerts.AlertType
import com.civilcam.ui.alerts.history.content.AlertHistoryTabRow
import com.civilcam.ui.alerts.history.model.AlertHistoryActions
import com.civilcam.ui.common.compose.CircleUserAvatar
import com.civilcam.ui.common.compose.InformationRow
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.utils.DateUtils

@Composable
fun AlertsListScreenContent(viewModel: AlertsHistoryViewModel) {

    val state = viewModel.state.collectAsState()
    var tabPage by remember { mutableStateOf(AlertType.RECEIVED) }


    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(title = stringResource(id = R.string.alerts_history_title),
                navigationAction = { viewModel.setInputActions(AlertHistoryActions.ClickGoBack) })
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                state.value.data?.let { data ->

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Divider(color = CCTheme.colors.grayThree)
                    }


                    itemsIndexed(data) { index, item ->
                        InformationRow(
                            title = if (state.value.alertType == AlertType.RECEIVED) item.userInfo.userName else stringResource(
                                id = R.string.alerts_history_sent_alert
                            ),
                            text = DateUtils.getFullDateAndTimeString(item.alertDate),
                            needDivider = index < data.lastIndex,
                            leadingIcon = {
                                if (state.value.alertType == AlertType.RECEIVED)
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
            }
        }
    }
}