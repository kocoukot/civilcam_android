package com.civilcam.ui.alerts.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.list.content.AlertHistoryRowSection
import com.civilcam.ui.alerts.list.model.AlertListActions
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertTypes
import com.civilcam.ui.common.compose.InformationRow
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.utils.DateUtils

@Composable
fun AlertsListScreenContent(viewModel: AlertsListViewModel) {

    val state = viewModel.state.collectAsState()


    if (state.value.resolveId != null) {
        AlertDialogComp(
            dialogTitle = stringResource(id = R.string.resolve_alert_title),
            dialogText = stringResource(id = R.string.resolve_alert_text),
            alertType = AlertTypes.CONFIRM_CANCEL,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.setInputActions(AlertListActions.ClickGoMyProfile)
                            }
                    )
                    Text(
                        text = stringResource(id = R.string.alerts_root_list_title),
                        style = CCTheme.typography.button_text,
                    )
                    IconButton(onClick = {
                        viewModel.setInputActions(AlertListActions.ClickGoSettings)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null,
                            tint = CCTheme.colors.primaryRed
                        )
                    }
                }

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
                        leadingIcon = { UserAvatar(item.userInfo.avatar) },
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


@Composable
fun UserAvatar(avatar: Int) {
    Image(
        painter = painterResource(id = avatar), contentDescription = null,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
    )
}