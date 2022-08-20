package com.civilcam.ui.alerts.map

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.map.content.AlertMapScreenContent
import com.civilcam.ui.alerts.map.content.AlertStreamScreenContent
import com.civilcam.ui.alerts.map.content.UserInformationContent
import com.civilcam.ui.alerts.map.model.LiveMapActions
import com.civilcam.ui.common.Constant.ANIMATION_DURATION
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.emergency.model.EmergencyScreen

@Composable
fun LiveMapScreenContent(viewModel: LiveMapViewModel) {

    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

    val liveHeight by animateDpAsState(
        targetValue = when (state.emergencyScreen) {
            EmergencyScreen.LIVE_EXTENDED -> screenHeight
            EmergencyScreen.COUPLED -> screenHeight / 2
            else -> 0.dp
        },
        animationSpec = tween(ANIMATION_DURATION)
    )

    val mapHeight by animateDpAsState(
        targetValue = when (state.emergencyScreen) {
            EmergencyScreen.MAP_EXTENDED, EmergencyScreen.NORMAL -> screenHeight
            EmergencyScreen.COUPLED -> (screenHeight / 2) + systemBarsPadding.calculateTopPadding()
            else -> 0.dp
        },
        animationSpec = tween(ANIMATION_DURATION)
    )

    if (state.isResolveAlertVisible) {
        AlertDialogComp(
            dialogTitle = stringResource(id = R.string.resolve_alert_title),
            dialogText = stringResource(id = R.string.resolve_alert_text),
            alertType = AlertDialogTypes.CANCEL_RESOLVE,
            onOptionSelected = {
                viewModel.setInputActions(LiveMapActions.ClickResolveAlertAnswer(it))
            })
    }
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarContent(
                title = stringResource(id = R.string.alert_user_detail_title),
                navigationItem = {
                    BackButton {
                        viewModel.setInputActions(LiveMapActions.ClickGoBack)
                    }
                },
                actionItem = {
                    TextActionButton(
                        actionTitle = stringResource(id = R.string.resolve_text),
                        modifier = Modifier,
                        isEnabled = !state.isResolved,
                        actionAction = {
                            viewModel.setInputActions(LiveMapActions.ClickResolveAlert)
                        },
                    )
                },
            )
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AlertStreamScreenContent(
                    modifier = Modifier
                        .height(liveHeight)
                        .fillMaxWidth(),
                    cameraState = state.cameraState,
                    alertScreenState = state.emergencyScreen,
                    onActionClick = viewModel::setInputActions
                )

                AlertMapScreenContent(
                    modifier = Modifier
                        .height(mapHeight)
                        .fillMaxWidth(),
                    isLocationAllowed = state.isLocationAllowed,
                    alertScreenState = state.emergencyScreen,
                    guardianInformation = state.userInformation,
                    userAlertLocationData = state.userAlertLocationData,
                    onActionClick = viewModel::setInputActions
                )
            }

            state.userInformation?.let { userInfo ->
                UserInformationContent(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .background(CCTheme.colors.white, CircleShape)
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .align(Alignment.TopCenter),
                    userInformation = userInfo,
                    onAction = viewModel::setInputActions,
                )
            }


            ComposeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                textStyle = CCTheme.typography.common_text_medium,
                textFontWeight = FontWeight.W500,
                title = stringResource(id = R.string.alert_user_detail_call)
            ) {
                viewModel.setInputActions(LiveMapActions.ClickCallPolice)
            }

        }

    }
}
 