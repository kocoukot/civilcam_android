package com.civilcam.ui.settings

import androidx.activity.compose.BackHandler
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
import com.civilcam.R
import com.civilcam.common.ext.Keyboard
import com.civilcam.common.ext.keyboardAsState
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.alert.AlertDialogTypes
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.settings.content.AlertsSettingsContent
import com.civilcam.ui.settings.content.ContactSupportContent
import com.civilcam.ui.settings.content.LanguageSettingsContent
import com.civilcam.ui.settings.content.MainSettingsContent
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsType
import com.civilcam.utils.LocaleHelper
import com.civilcam.utils.LocaleHelper.SetLanguageCompose

@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {

    val state = viewModel.state.collectAsState()
    val isKeyboardOpen by keyboardAsState()
    viewModel.setInputActions(SettingsActions.IsNavBarVisible(isKeyboardOpen == Keyboard.Opened))


    var currentLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage()) }
    var selectedLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage()) }
    var isActionActive by remember { mutableStateOf(false) }
    SetLanguageCompose(selectedLanguage)

    BackHandler {
        viewModel.setInputActions(SettingsActions.ClickGoBack)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CCTheme.colors.lightGray),
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

                    val isActionEnabled =
                        if (settingsType == SettingsType.LANGUAGE || settingsType == SettingsType.CONTACT_SUPPORT) {
                            isActionActive
                        } else true
                    TopAppBarContent(
                        title = stringResource(id = title),
                        navigationAction = {
                            when (settingsType) {
                                SettingsType.LANGUAGE -> {
                                    viewModel.setInputActions(SettingsActions.ClickGoBack)
                                    selectedLanguage = currentLanguage
                                }
                                else -> {
                                    viewModel.setInputActions(SettingsActions.ClickGoBack)
                                }
                            }
                        },
                        isActionEnabled = isActionEnabled,
                        actionTitle = getActionTitle(settingsType),
                        actionAction = { setAction(viewModel, settingsType) }
                    )
                }
                Divider(color = CCTheme.colors.grayThree)
            }
        },
    ) {
        Crossfade(
            targetState = state.value.settingsType,
        ) { settingsState ->
            when (settingsState) {
                SettingsType.MAIN ->
                    MainSettingsContent {
                        viewModel.setInputActions(SettingsActions.ClickSection(it))
                        currentLanguage = LocaleHelper.getSelectedLanguage()
                        selectedLanguage = LocaleHelper.getSelectedLanguage()
                        isActionActive = false
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
                SettingsType.LANGUAGE -> {
                    LanguageSettingsContent(
                        selectedLanguage
                    ) {
                        selectedLanguage = it
                        isActionActive = currentLanguage != it
                    }
                }
                SettingsType.CONTACT_SUPPORT -> {
                    ContactSupportContent(
                        supportInformation = { issue, description, email ->
                            isActionActive = couldBeSend(issue, description, email)
                            viewModel.setInputActions(
                                SettingsActions.EnterContactSupportInfo(
                                    issue,
                                    description,
                                    email
                                )
                            )
                        }
                    )
                }
                SettingsType.LOG_OUT -> {
                    MainSettingsContent {}
                    AlertDialogComp(
                        dialogTitle = stringResource(id = R.string.settings_log_out_alert_title),
                        dialogText = stringResource(id = R.string.settings_log_out_alert_text),
                        alertType = AlertDialogTypes.YES_CANCEL,
                        onOptionSelected = {
                            viewModel.setInputActions(
                                SettingsActions.ClickCloseAlertDialog(
                                    it,
                                    true
                                )
                            )
                        },
                    )
                }
                SettingsType.DELETE_ACCOUNT -> {
                    MainSettingsContent {}
                    AlertDialogComp(
                        dialogTitle = stringResource(id = R.string.settings_delete_account_alert_title),
                        dialogText = stringResource(id = R.string.settings_delete_account_alert_text),
                        alertType = AlertDialogTypes.YES_CANCEL,
                        onOptionSelected = {
                            viewModel.setInputActions(
                                SettingsActions.ClickCloseAlertDialog(
                                    it,
                                    false
                                )
                            )
                        },
                    )
                }
                else -> {}
            }
        }
    }
}


@Composable
private fun getActionTitle(settingsType: SettingsType) = when (settingsType) {
    SettingsType.CONTACT_SUPPORT -> stringResource(id = R.string.send_text)
    SettingsType.LANGUAGE -> stringResource(id = R.string.save_text)
    SettingsType.CHANGE_PASSWORD -> stringResource(id = R.string.continue_text)
    else -> ""
}

private fun setAction(viewModel: SettingsViewModel, settingsType: SettingsType) {
    when (settingsType) {
        SettingsType.LANGUAGE -> viewModel.setInputActions(SettingsActions.ClickSaveLanguage)
        SettingsType.CONTACT_SUPPORT -> viewModel.setInputActions(SettingsActions.ClickSendToSupport)
        else -> {

        }
    }
}

private fun couldBeSend(
    issue: String,
    issueDescription: String,
    email: String
) = issue.isNotEmpty() && issueDescription.isNotEmpty() && email.isNotEmpty()
