package com.civilcam.ui.alerts.details

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.details.model.AlertDetailActions
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.TopAppBarContent

@Composable
fun AlertDetailScreenContent(viewModel: AlertsDetailViewModel) {

//    val state = viewModel.state?.collectAsState()

    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(title = "Alert details",//stringResource(id = R.string.alerts_history_title),
                navigationItem = {
                    BackButton {
                        viewModel.setInputActions(AlertDetailActions.ClickGoBack)
                    }
                }
            )
        },

        modifier = Modifier.fillMaxSize()
    ) {

    }
}