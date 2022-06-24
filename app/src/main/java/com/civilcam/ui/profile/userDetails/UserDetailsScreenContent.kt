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
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.profile.userDetails.content.UserDetailsSection
import com.civilcam.ui.profile.userDetails.content.UserRequestSection
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun UserDetailsScreenContent(viewModel: UserDetailsViewModel) {


    val state = viewModel.state.collectAsState()


    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarContent(
                title = stringResource(id = R.string.user_details_title),
                navigationAction = {
                    viewModel.setInputActions(UserDetailsActions.ClickGoBack)
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
                    myGuardenceChange = { viewModel.setInputActions(UserDetailsActions.ClickGuardenceChange) },
                    onStopGuarding = { viewModel.setInputActions(UserDetailsActions.ClickStopGuarding) },
                    mockAction = { viewModel.setInputActions(UserDetailsActions.Mock) },
                )

                if (data.guardRequest != null && data.guardRequest?.isGuarding != true) {
                    Divider(
                        color = CCTheme.colors.lightGray, modifier = Modifier
                            .height(20.dp)
                    )
                    UserRequestSection(
                        data.userInfoSection.userName,
                        stringResource(id = R.string.user_details_request_text)
                    ) {
                        viewModel.setInputActions(UserDetailsActions.ClickRequestAnswer(it))
                    }
                }
            }
        }
    }
}