package com.civilcam.ui.network.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.civilcam.R
import com.civilcam.common.ext.castSafe
import com.civilcam.common.theme.CCTheme
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.ui.common.compose.*
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.common.loading.DialogLoadingContent
import com.civilcam.ui.network.main.content.GuardianSearchContent
import com.civilcam.ui.network.main.content.GuardsMainSection
import com.civilcam.ui.network.main.content.NetworkTabRow
import com.civilcam.ui.network.main.content.RequestsScreenSection
import com.civilcam.ui.network.main.model.NetworkMainActions
import com.civilcam.ui.network.main.model.NetworkScreen
import timber.log.Timber

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NetworkMainScreenContent(viewModel: NetworkMainViewModel) {

    val state = viewModel.state.collectAsState()
    var tabPage by remember { mutableStateOf(NetworkType.ON_GUARD) }
    tabPage = state.value.networkType

    val searchList = if (state.value.screenState == NetworkScreen.SEARCH_GUARD)
        viewModel.searchList.collectAsLazyPagingItems()
    else
        null

//    Timber.tag("networkSearch").i("lazyList ${list?.itemCount}")
    if (state.value.refreshList == Unit) {
        searchList?.refresh()
        viewModel.stopRefresh()
    }

    if (state.value.isLoading) {
        DialogLoadingContent()
    }

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
                            title = stringResource(id = screenState.screenTitle),
                            navigationItem = {
                                when (screenState) {
                                    NetworkScreen.MAIN ->
                                        AvatarButton {
                                            viewModel.setInputActions(NetworkMainActions.ClickGoMyProfile)
                                        }
                                    NetworkScreen.REQUESTS, NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD ->
                                        BackButton {
                                            viewModel.setInputActions(NetworkMainActions.ClickGoBack)
                                        }
                                }
                            },
                            actionItem = {
                                when (screenState) {
                                    NetworkScreen.MAIN -> {
                                        IconActionButton(R.drawable.ic_settings) {
                                            viewModel.setInputActions(NetworkMainActions.ClickGoSettings)
                                        }
                                    }
                                    NetworkScreen.ADD_GUARD -> {
                                        TextActionButton(actionTitle = stringResource(id = R.string.add_from_contacts_title)) {
                                            viewModel.setInputActions(NetworkMainActions.ClickGoContacts)
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        )
                    }
                }
                AnimatedVisibility(
                    visible = state.value.screenState == NetworkScreen.MAIN ||
                            state.value.screenState == NetworkScreen.SEARCH_GUARD ||
                            state.value.screenState == NetworkScreen.ADD_GUARD
                ) {
                    Column {
                        SearchInputField(
                            looseFocus = state.value.screenState == NetworkScreen.MAIN,
                            text = state.value.data.searchText,
                            onValueChanged = {
                                viewModel.setInputActions(NetworkMainActions.EnteredSearchString(it))
                            },
                            isFocused = {
                                if (state.value.screenState == NetworkScreen.MAIN)
                                    viewModel.setInputActions(NetworkMainActions.ClickGoSearch)
                            })
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                AnimatedVisibility(visible = state.value.screenState == NetworkScreen.MAIN) {
                    NetworkTabRow(tabPage) {
                        tabPage = it
                        viewModel.setInputActions(NetworkMainActions.ClickNetworkTypeChange(it))
                    }
                }

                if (!(state.value.data.guardiansList.isNotEmpty() == true ||
                            state.value.data.requestsList.isNotEmpty() == true) ||
                    state.value.screenState == NetworkScreen.SEARCH_GUARD ||
                    state.value.screenState == NetworkScreen.ADD_GUARD
                ) RowDivider()

            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = tabPage == NetworkType.GUARDIANS && state.value.screenState == NetworkScreen.MAIN,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
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
        state.value.data.let { screenData ->
            Crossfade(targetState = state.value.screenState) { screenState ->
                when (screenState) {
                    NetworkScreen.MAIN -> {
                        GuardsMainSection(
                            screenData,
                            tabPage = tabPage,
                            clickGoRequests = {
                                viewModel.setInputActions(NetworkMainActions.ClickGoRequests)
                            },
                            clickGoUser = {
                                viewModel.setInputActions(NetworkMainActions.ClickUser(it))
                            })
                    }


                    NetworkScreen.REQUESTS -> {
                        RequestsScreenSection(screenData.requestsList as List<GuardianItem>) { request, isAccepted ->

                        }
                    }
                    NetworkScreen.SEARCH_GUARD, NetworkScreen.ADD_GUARD -> {
                        state.value.data.let { data ->
                            GuardianSearchContent(
                                searchList,
                                pendingList = state.value.data.searchScreenSectionModel.pendingList,
                                data.searchText,
                                onSearchAction = viewModel::setInputActions
                            )
                        }
                    }
                }
            }

        }
    }

    searchList?.apply {

        Timber.d("lazyPlace loadState $loadState")
        when {
            loadState.refresh is LoadState.Error -> {
                (loadState.refresh as LoadState.Error).error.castSafe<ServiceException>()
                    ?.let { error ->
                        viewModel.resolveSearchError(error)

                    }
            }
            else -> {}
        }
    }
}