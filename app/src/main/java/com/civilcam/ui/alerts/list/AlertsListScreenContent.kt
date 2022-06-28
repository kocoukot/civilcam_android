package com.civilcam.ui.alerts.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                        title = item.userInfo.userName,
                        text = DateUtils.getFullDateAndTimeString(item.alertDate),
                        needDivider = index < data.lastIndex,
                        leadingIcon = { CircleUserAvatar(item.userInfo.avatar, 36) },
                        trailingIcon = {
                            if (!item.isResolved) {
                                TextActionButton(
                                    actionTitle = stringResource(id = R.string.resolve_text)
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
                            viewModel.setInputActions(AlertListActions.ClickGoUserProfile(item.userInfo.userId))
                        },
                    )
                }
                item {
                    Divider(color = CCTheme.colors.grayThree)
                }
            }



            item {
                AlertHistoryRowSection {
                    viewModel.setInputActions(AlertListActions.ClickGoAlertsHistory)
                }
            }
        }
    }
}