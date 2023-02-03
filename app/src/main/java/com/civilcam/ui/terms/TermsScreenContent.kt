package com.civilcam.ui.terms

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.docs.TermsType
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.terms.content.AcceptTermsContent
import com.civilcam.ui.terms.content.WebButton
import com.civilcam.ui.terms.model.TermsActions

@Composable
fun TermsScreenContent(viewModel: TermsViewModel) {

    val state by viewModel.state.collectAsState()
    var isAccepted by remember { mutableStateOf(false) }

    if (state.isLoading) {
        DialogLoadingContent()
    }
    if (state.errorText.isNotEmpty()) {
        AlertDialogComp(
            dialogText = state.errorText,
            alertType = AlertDialogButtons.OK,
            onOptionSelected = { viewModel.setInputActions(TermsActions.ClickCloseAlert) }
        )
    }

    Scaffold(
        backgroundColor = if (state.isSettings) CCTheme.colors.lightGray else CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.terms_conditions_title),
                    titleSize = 16,
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(TermsActions.ClickGoBack)
                        }
                    },
                )
                if (state.isSettings) RowDividerGrayThree(0) else DividerLightGray()
            }
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 18.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp)
                    .weight(1f)

            ) {
                Text(
                    text = stringResource(id = R.string.terms_conditions_title),
                    style = CCTheme.typography.big_title,
                    modifier = Modifier.padding(bottom = 20.dp),
                    color = CCTheme.colors.grayOneDark
                )

                Text(
                    text = stringResource(id = R.string.terms_conditions_text),
                    style = CCTheme.typography.common_text_regular,
                    color = CCTheme.colors.black

                )

                Spacer(modifier = Modifier.height(28.dp))
                WebButton(TermsType.TERMS_CONDITIONS) {
                    viewModel.setInputActions(TermsActions.ClickDocument(it))
                }

                Spacer(modifier = Modifier.height(12.dp))
                WebButton(TermsType.PRIVACY_POLICY) {
                    viewModel.setInputActions(TermsActions.ClickDocument(it))
                }
                Spacer(modifier = Modifier.height(40.dp))

                if (!state.isSettings) {
                    AcceptTermsContent(isAccepted) {
                        isAccepted = !isAccepted
                    }
                }
            }

            if (!state.isSettings) {
                ComposeButton(
                    title = stringResource(id = com.civilcam.ext_features.R.string.continue_text),
                    Modifier.padding(horizontal = 8.dp),
                    isActivated = isAccepted,
                    buttonClick = {
                        viewModel.setInputActions(TermsActions.ClickContinue)
                    }
                )
            }
        }
    }
}