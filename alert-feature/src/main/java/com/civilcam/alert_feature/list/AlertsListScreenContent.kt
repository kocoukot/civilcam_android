package com.civilcam.alert_feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.list.content.AlertHistoryRowSection
import com.civilcam.alert_feature.list.model.AlertListActions
import com.civilcam.domainLayer.model.alerts.AlertStatus
import com.civilcam.ext_features.alert.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme


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
                        state.value.userAvatar?.imageUrl?.let { url ->
                            AvatarButton(url) {
                                viewModel.setInputActions(AlertListActions.ClickGoMyProfile)
                            }
                        }
                    },
                    actionItem = {
                        IconActionButton(buttonIcon = com.civilcam.ext_features.R.drawable.ic_settings) {
                            viewModel.setInputActions(AlertListActions.ClickGoSettings)
                        }
                    },
                )
                RowDivider()
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
                        RowDivider()
                    }

                    itemsIndexed(data, key = { _, item -> item.alertId }) { index, item ->
                        InformationRow(
                            title = item.userInfo.personFullName,
                            text = item.alertDate,//DateUtils.getFullDateAndTimeString(),
                            needDivider = index < data.lastIndex,
                            leadingIcon = {
                                item.userInfo.personAvatar?.imageUrl?.let { avatar ->
                                    CircleUserAvatar(avatar, 36)
                                }
                            },
                            trailingIcon = {
                                if (item.alertStatus == AlertStatus.ACTIVE) {
                                    TextActionButton(
                                        modifier = Modifier.padding(end = 8.dp),
                                        actionTitle = stringResource(id = R.string.resolve_text),
                                        textFont = FontFamily(Font(com.civilcam.ext_features.R.font.roboto_regular)),
                                    ) {
                                        viewModel.setInputActions(
                                            AlertListActions.ClickResolveAlert(item.alertId)
                                        )
                                    }
                                }
                            },
                            rowClick = {
                                viewModel.setInputActions(
                                    AlertListActions.ClickGoUserProfile(item.alertId)
                                )
                            },
                        )
                    }
                    item {
                        RowDivider()
                    }
                }
            } ?: kotlin.run {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
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