package com.civilcam.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.civilcam.R
import com.civilcam.domainLayer.ext.LocaleHelper
import com.civilcam.domainLayer.ext.LocaleHelper.SetLanguageCompose
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.isEmail
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.settings.content.*
import com.civilcam.ui.settings.model.SettingsActions
import com.civilcam.ui.settings.model.SettingsType
import com.civilcam.ui.settings.model.SettingsType.Companion.hasActionButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreenContent(viewModel: SettingsViewModel) {

	val state = viewModel.state.collectAsState()
	val context = LocalContext.current
	var currentLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage(context)) }
	var selectedLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage(context)) }
	var isActionActive by remember { mutableStateOf(false) }
	SetLanguageCompose(selectedLanguage)

	BackHandler {
		viewModel.setInputActions(SettingsActions.ClickGoBack)
	}

	if (state.value.isLoading) {
		DialogLoadingContent()
	}
	if (state.value.errorText.isNotEmpty()) {
		AlertDialogComp(
			dialogText = state.value.errorText,
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { viewModel.setInputActions(SettingsActions.ClearErrorText) }
		)
	}
	state.value.screenAlert?.let { alert ->
		AlertDialogComp(
			dialogText = stringResource(id = alert.text),
			alertType = AlertDialogTypes.OK,
			onOptionSelected = { viewModel.setInputActions(SettingsActions.ClickCloseScreenAlert) }
		)
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

					val isActionEnabled =
						if (settingsType.hasActionButton()) isActionActive else true
					TopAppBarContent(
						title = stringResource(id = screenTitle(settingsType)),
						navigationItem = {
							BackButton {
								if (settingsType == SettingsType.LANGUAGE) selectedLanguage =
									currentLanguage
								viewModel.setInputActions(SettingsActions.ClickGoBack)
							}
						},
						actionItem = {
							if (settingsType.hasActionButton()) {
								TextActionButton(
									isEnabled = isActionEnabled,
									actionTitle = stringResource(id = settingsType.actionBtnTitle)
								) {
									viewModel.setInputActions(setAction(settingsType, context))
								}
							}
						}
					)
				}
				RowDivider()
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
						currentLanguage = LocaleHelper.getSelectedLanguage(context)
						selectedLanguage = LocaleHelper.getSelectedLanguage(context)
						isActionActive = false
					}

				SettingsType.ALERTS -> {
					state.value.data.alertsSectionData.let { settingsAlertsSectionData ->
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

				SettingsType.CREATE_PASSWORD -> {
					state.value.data.createPasswordSectionData.let { data ->
						isActionActive = data.isFilled
						CreatePasswordSettingsContent(
							data
						) { type, meetCriteria, password ->
							isActionActive = data.isFilled

							viewModel.setInputActions(
								SettingsActions.NewPasswordEntered(
									type,
									meetCriteria,
									password
								)
							)
						}
					}

				}

				SettingsType.CHANGE_PASSWORD -> {
					state.value.data.changePasswordSectionData?.let { data ->
						isActionActive = !data.hasError && data.currentPassword.isNotEmpty()
						ChangePasswordSettingsContent(data) {
							viewModel.setInputActions(SettingsActions.EnterCurrentPassword(it))
						}
					}
				}


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
						contactSupportModel = state.value.data.contactSupportSectionData,
						supportInformation = { issue, description, email ->
							isActionActive =
								issue.isNotEmpty() && description.isNotEmpty() && email.isNotEmpty() && email.isEmail()
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
				SettingsType.SUBSCRIPTION -> {
					viewModel.setInputActions(SettingsActions.ClickGoSubscription)
					SubscriptionSettingsContent(
						onManageClicked = { viewModel.setInputActions(SettingsActions.GoSubscriptionManage) },
						onRestoreClicked = {},
						subscriptionPlan = state.value.data.subscriptionData,
						onSubscriptionPlanClick = {}
					)
				}

				else -> {}
			}
		}
	}
}

private fun setAction(settingsType: SettingsType, context: Context): SettingsActions =
	when (settingsType) {
		SettingsType.LANGUAGE -> SettingsActions.ClickSaveLanguage(
			LocaleHelper.getSelectedLanguage(
				context
			)
		)
		SettingsType.CONTACT_SUPPORT -> SettingsActions.ClickSendToSupport
		SettingsType.CHANGE_PASSWORD -> SettingsActions.CheckCurrentPassword
		SettingsType.CREATE_PASSWORD -> SettingsActions.SaveNewPassword
		else -> SettingsActions.ClickGoBack

	}

private fun screenTitle(settingsType: SettingsType) = when (settingsType) {
	SettingsType.MAIN, SettingsType.LOG_OUT, SettingsType.TERMS_AND_POLICY, SettingsType.DELETE_ACCOUNT -> R.string.settings_title
	SettingsType.CHANGE_PASSWORD -> R.string.settings_password_title
	else -> settingsType.rowTitle
}

