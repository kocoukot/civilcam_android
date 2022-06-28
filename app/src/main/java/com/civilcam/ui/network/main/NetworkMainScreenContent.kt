package com.civilcam.ui.network.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.guard.NetworkType
import com.civilcam.ui.common.compose.AvatarButton
import com.civilcam.ui.common.compose.IconActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.EmptyListText
import com.civilcam.ui.common.compose.inputs.SearchInputField
import com.civilcam.ui.network.main.content.NetworkTabRow
import com.civilcam.ui.network.main.model.NetworkMainActions

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
                TopAppBarContent(
                    title = stringResource(id = R.string.navigation_bar_network),
                    navigationItem = {
                        AvatarButton {
                            viewModel.setInputActions(NetworkMainActions.ClickGoMyProfile)
                        }
                    },
                    actionItem = {
                        IconActionButton(R.drawable.ic_settings) {
                            viewModel.setInputActions(NetworkMainActions.ClickGoSettings)
                        }
                    }
                )

                SearchInputField(isEnable = false) {

                }

                NetworkTabRow(
                    tabPage
                ) {
                    tabPage = it
                    viewModel.setInputActions(NetworkMainActions.ClickNetworkTypeChange(it))
                }

                Divider(color = CCTheme.colors.grayThree)
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
//            if (tabPage == NetworkType.GUARDIANS)

        }
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            EmptyListText(
                stringResource(
                    id =
                    when (tabPage) {
                        NetworkType.ON_GUARD -> R.string.network_main_on_guard_list_is_empty
                        NetworkType.GUARDIANS -> R.string.network_main_guardians_list_is_empty
                    }
                )
            )
        }
    }
}
 