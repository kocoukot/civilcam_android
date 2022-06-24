package com.civilcam.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.settings.content.AlertsSettingsContent
import com.civilcam.ui.settings.content.MainSettingsContent
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsType

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {

    val state = viewModel.state.collectAsState()
    var topBarTitle by remember { mutableStateOf(state.value.settingsType.title) }
    topBarTitle = state.value.settingsType.title
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Crossfade(state.value.settingsType.title) { title ->
                    TopAppBarContent(
                        title = stringResource(id = title),
                        navigationAction = {
                            viewModel.setInputActions(SettingsActions.ClickGoBack)
                        },
                    )
                }

                Divider(color = CCTheme.colors.grayThree)
            }

        },
        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray)
    ) {

        Crossfade(targetState = state.value.settingsType) { state ->
            when (state) {
                SettingsType.MAIN -> MainSettingsContent {
                    viewModel.setInputActions(SettingsActions.ClickSection(it))
                }
                SettingsType.ALERTS -> AlertsSettingsContent()
//                SettingsType.SUBSCRIPTION -> TODO()
//                SettingsType.CHANGE_PASSWORD -> TODO()
//                SettingsType.LANGUAGE -> TODO()
//                SettingsType.CONTACT_SUPPORT -> TODO()
//                SettingsType.LOG_OUT -> TODO()
//                SettingsType.DELETE_ACCOUNT -> TODO()
                else -> {}
            }
        }
    }
}