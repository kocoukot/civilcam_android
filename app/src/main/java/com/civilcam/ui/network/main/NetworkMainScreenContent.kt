package com.civilcam.ui.network.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.guard.NetworkType
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.network.main.content.GuardsMainSection
import com.civilcam.ui.network.main.content.NetworkTabRow
import com.civilcam.ui.network.main.content.RequestsScreenSection
import com.civilcam.ui.network.main.model.GuardianItem
import com.civilcam.ui.network.main.model.NetworkMainActions
import com.civilcam.ui.network.main.model.NetworkScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NetworkMainScreenContent(viewModel: NetworkMainViewModel) {

    val state = viewModel.state.collectAsState()
    var tabPage by remember { mutableStateOf(NetworkType.ON_GUARD) }



    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CCTheme.colors.white)
            ) {
                AnimatedContent(targetState = state.value.screenState) { screenState ->
                    Column {
                        TopAppBarContent(
                            title = when (screenState) {
                                NetworkScreen.MAIN -> stringResource(id = R.string.navigation_bar_network)
                                NetworkScreen.REQUESTS -> stringResource(id = R.string.network_main_requests)
                            },
                            navigationItem = {
                                when (screenState) {
                                    NetworkScreen.MAIN -> AvatarButton {
                                        viewModel.setInputActions(NetworkMainActions.ClickGoMyProfile)
                                    }
                                    NetworkScreen.REQUESTS -> BackButton {
                                        viewModel.setInputActions(NetworkMainActions.ClickGoBack)
                                    }
                                }


                            },
                            actionItem = {
                                if (screenState == NetworkScreen.MAIN) {
                                    IconActionButton(R.drawable.ic_settings) {
                                        viewModel.setInputActions(NetworkMainActions.ClickGoSettings)
                                    }
                                }
                            }
                        )

                        when (screenState) {
                            NetworkScreen.MAIN -> {
                                SearchInputField(isEnable = false) {


                                }

                                NetworkTabRow(
                                    tabPage
                                ) {
                                    tabPage = it
                                    viewModel.setInputActions(
                                        NetworkMainActions.ClickNetworkTypeChange(
                                            it
                                        )
                                    )
                                }
                            }
                            NetworkScreen.REQUESTS -> {}
                        }

                        if (!(state.value.data?.guardiansList?.isNotEmpty() == true ||
                                    state.value.data?.requestsList?.isNotEmpty() == true)
                        ) RowDivider()
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = tabPage == NetworkType.GUARDIANS,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                FloatingActionButton(
                    elevation = FloatingActionButtonDefaults.elevation(8.dp),
                    modifier = Modifier.size(64.dp),
                    onClick = {
                        viewModel.setInputActions(NetworkMainActions.ClickAddGuardian)
                    },
                    shape = CircleShape,
                    backgroundColor = CCTheme.colors.primaryRed
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = null,
                        tint = CCTheme.colors.white
                    )
                }
            }
        }
    ) {
        state.value.data?.let { screenData ->
            Crossfade(targetState = state.value.screenState) { screenState ->
                when (screenState) {
                    NetworkScreen.MAIN -> {
                        GuardsMainSection(
                            screenData,
                            tabPage = tabPage
                        ) {
                            viewModel.setInputActions(NetworkMainActions.ClickGoRequests)
                        }

                    }


                    NetworkScreen.REQUESTS -> {
                        RequestsScreenSection(screenData.requestsList as List<GuardianItem>) { request, isAccepted ->

                        }
                    }

                }
            }

        }
    }
}