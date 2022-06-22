package com.civilcam.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.settings.model.SettingsActions

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {

    val state = viewModel.state.collectAsState()

    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            TopAppBarContent(
                title = "Settings",
                navigationAction = {
                    viewModel.setInputActions(SettingsActions.ClickGoBack)
                },
            )

        },
        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray)
    ) {


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

        }
    }
}