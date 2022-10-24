package com.civilcam.alert_feature.map

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.content.AlertMapScreenContent
import com.civilcam.alert_feature.map.content.AlertStreamScreenContent
import com.civilcam.alert_feature.map.content.UserInformationContent
import com.civilcam.alert_feature.map.model.LiveMapActions
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.Constant.ANIMATION_DURATION
import com.civilcam.ext_features.alert.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun LiveMapScreenContent(viewModel: LiveMapViewModel) {

    val state by viewModel.state.collectAsState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            viewModel.setInputActions(
                LiveMapActions.SelectLocationPermission(
                    it.values.contains(
                        true
                    )
                )
            )
        }
    )

    LaunchedEffect(key1 = true) {
        permissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
    }

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
            dialogTitle = stringResource(id = com.civilcam.alert_feature.R.string.resolve_alert_title),
            dialogText = stringResource(id = com.civilcam.alert_feature.R.string.resolve_alert_text),
            alertType = AlertDialogTypes.CANCEL_RESOLVE,
            onOptionSelected = {
                viewModel.setInputActions(LiveMapActions.ClickResolveAlertAnswer(it))
            })
    }

    if (state.isLoading) DialogLoadingContent()

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
                        actionTitle = stringResource(id = com.civilcam.alert_feature.R.string.resolve_text),
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
                    guardianInformation = state.onGuardUserInformation,
                    userAlertLocationData = state.currentUserLocationData,
                    detectLocation = {
                        permissionRequest.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            )
                        )
                    },
                    onActionClick = viewModel::setInputActions
                )
            }

            state.onGuardUserInformation?.let { userInfo ->
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
 