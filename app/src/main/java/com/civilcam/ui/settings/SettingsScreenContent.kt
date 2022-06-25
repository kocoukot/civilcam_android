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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.LanguageType
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.settings.content.AlertsSettingsContent
import com.civilcam.ui.settings.content.LanguageSettingsContent
import com.civilcam.ui.settings.content.MainSettingsContent
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsType
import com.civilcam.utils.LocaleHelper
import java.util.*

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {

    val state = viewModel.state.collectAsState()
    var currentLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage()) }

    SetLanguage(currentLanguage)

    var topBarTitle by remember { mutableStateOf(state.value.settingsType.title) }
    topBarTitle = state.value.settingsType.title
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Crossfade(state.value.settingsType) { settingsType ->
                    val title = when (settingsType) {
                        SettingsType.MAIN, SettingsType.LOG_OUT, SettingsType.TERMS_AND_POLICY, SettingsType.DELETE_ACCOUNT -> R.string.settings_title
                        else -> settingsType.title
                    }
                    val actionTitle = when (settingsType) {
                        SettingsType.CONTACT_SUPPORT -> stringResource(id = R.string.send_text)
                        SettingsType.LANGUAGE -> stringResource(id = R.string.save_text)
                        SettingsType.CHANGE_PASSWORD -> stringResource(id = R.string.continue_text)
                        else -> ""
                    }
                    val actionAction = ""
                    TopAppBarContent(
                        title = stringResource(id = title),
                        navigationAction = {
                            viewModel.setInputActions(SettingsActions.ClickGoBack)
                        },
                        actionTitle = actionTitle,
                        actionAction = {}
                    )
                }
                Divider(color = CCTheme.colors.grayThree)
            }

        },
        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray)
    ) {
        Crossfade(targetState = state.value.settingsType) { settingsState ->


            when (settingsState) {
                SettingsType.MAIN ->
                    MainSettingsContent {
                        viewModel.setInputActions(SettingsActions.ClickSection(it))
                    }

                SettingsType.ALERTS -> {

                    state.value.data?.alertsSectionData?.let { settingsAlertsSectionData ->
                        AlertsSettingsContent(settingsAlertsSectionData) { isSwitched, type ->
                            viewModel.setInputActions(
                                SettingsActions.ClickAlertSwitch(
                                    isSwitched,
                                    type
                                )
                            )
                        }
                    }
                }

//                SettingsType.SUBSCRIPTION -> TODO()
//                SettingsType.CHANGE_PASSWORD -> TODO()
                SettingsType.LANGUAGE -> LanguageSettingsContent {
                    currentLanguage = it
                }
//                SettingsType.CONTACT_SUPPORT -> TODO()
//                SettingsType.LOG_OUT -> TODO()
//                SettingsType.DELETE_ACCOUNT -> TODO()
                else -> {}
            }
        }
    }
}

@Composable
private fun SetLanguage(lang: LanguageType) {
    LocaleHelper.setLocale(LocalContext.current, lang.langValue)
    val locale = Locale(lang.langValue)
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}