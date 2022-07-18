package com.civilcam.ui.alerts.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.list.content.AlertHistoryRowSection
import com.civilcam.ui.alerts.list.model.AlertListActions
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.*
import com.civilcam.utils.DateUtils

@Composable
fun AlertsListScreenContent(viewModel: AlertsListViewModel) {

    val state = viewModel.state.collectAsState()


    if (state.value.resolveId != null) {
        AlertDialogComp(
            dialogTitle = stringResource(id = R.string.resolve_alert_title),
            dialogText = stringResource(id = R.string.resolve_alert_text),
            alertType = AlertDialogTypes.CONFIRM_CANCEL,
            onOptionSelected = {
                viewModel.setInputActions(AlertListActions.ClickConfirmResolve(it))
            })
    }
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CCTheme.colors.white)
            ) {


                TopAppBarContent(
                    title = stringResource(id = R.string.alerts_root_list_title),
                    navigationItem = {
                        AvatarButton { //todo fix image pass
                            viewModel.setInputActions(AlertListActions.ClickGoMyProfile)
                        }
                    },
                    actionItem = {
                        IconActionButton(buttonIcon = R.drawable.ic_settings) {
                            viewModel.setInputActions(AlertListActions.ClickGoSettings)
                        }
                    },
                )

                Divider(color = CCTheme.colors.grayThree)
            }
        },

        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AlertHistoryRowSection {
                viewModel.setInputActions(AlertListActions.ClickGoAlertsHistory)
            }

            state.value.data?.takeIf { it.isNotEmpty() }?.let { data ->
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
                            title = item.userInfo.userName,
                            text = DateUtils.getFullDateAndTimeString(item.alertDate),
                            needDivider = index < data.lastIndex,
                            leadingIcon = { CircleUserAvatar(item.userInfo.avatar, 36) },
                            trailingIcon = {
                                if (!item.isResolved) {
                                    TextActionButton(
                                        modifier = Modifier.padding(end = 8.dp),
                                        actionTitle = stringResource(id = R.string.resolve_text),
                                        textFont = FontFamily(Font(R.font.roboto_regular)),
                                    ) {
                                        viewModel.setInputActions(
                                            AlertListActions.ClickResolveAlert(
                                                item.alertId
                                            )
                                        )
                                    }
                                }
                            },
                            rowClick = {
                                viewModel.setInputActions(
                                    AlertListActions.ClickGoUserProfile(
                                        item.userInfo.userId
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
                        .clickable { viewModel.setInputActions(AlertListActions.ClickGetMockLis) },
                    contentAlignment = Alignment.Center
                ) {
                    EmptyListText(
                        stringResource(id = R.string.alerts_list_empty_state)
                    )
                }
            }
        }
    }
}