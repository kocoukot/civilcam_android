@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.profile.userDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.profile.userDetails.content.UserDetailsSection
import com.civilcam.ui.profile.userDetails.content.UserRequestSection
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun UserDetailsScreenContent(viewModel: UserDetailsViewModel) {


    val state = viewModel.state.collectAsState()

    if (state.value.isLoading) {
        DialogLoadingContent()
    }

    state.value.showAlert {
        viewModel.setInputActions(UserDetailsActions.ClickCloseErrorAlert)
    }

    if (state.value.alertType != null) {
        AlertDialogComp(
            dialogTitle = "",
            dialogText = if (state.value.alertType == StopGuardAlertType.STOP_GUARDING)
                stringResource(id = R.string.user_details_alert_remove_guard)
            else
                stringResource(id = R.string.user_details_alert_stop_guarding),
            alertType = AlertDialogTypes.CONFIRM_CANCEL,
            onOptionSelected = {
                if (it) {
                    viewModel.setInputActions(
                        if (state.value.alertType == StopGuardAlertType.STOP_GUARDING) {
                            UserDetailsActions.ClickStopGuarding
                        } else {
                            UserDetailsActions.ClickGuardenceChange
                        }
                    )
                } else viewModel.setInputActions(UserDetailsActions.ClickCloseAlert)
            }
        )
    }
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarContent(
                title = stringResource(id = R.string.user_details_title),
                navigationItem = {
                    BackButton {
                        viewModel.setInputActions(UserDetailsActions.ClickGoBack)
                    }
                })
        }
    ) {
        state.value.data?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CCTheme.colors.white),
            ) {
                UserDetailsSection(
                    userData = data,
                    myGuardenceChange = viewModel::setInputActions
                )

                if (data.personStatus?.status == GuardianStatus.PENDING) {
                    Divider(
                        color = CCTheme.colors.lightGray, modifier = Modifier
                            .height(20.dp)
                    )
                    UserRequestSection(
                        data.personFullName,
                        stringResource(id = R.string.user_details_request_text)
                    ) {
                        viewModel.setInputActions(UserDetailsActions.ClickRequestAnswer(it))
                    }
                }
            }
        }
    }
}