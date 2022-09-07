@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.profile.userDetails

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.profile.userDetails.content.UserDetailsSection
import com.civilcam.ui.profile.userDetails.content.UserRequestSection
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.google.accompanist.pager.ExperimentalPagerApi

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CCTheme.colors.lightGray,
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
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
            BackButton(modifier = Modifier.align(Alignment.TopStart)) {
                viewModel.setInputActions(UserDetailsActions.ClickGoBack)
            }
        }
    }
}