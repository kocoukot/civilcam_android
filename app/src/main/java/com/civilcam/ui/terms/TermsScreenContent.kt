@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.terms

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.TermsType
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.DividerLightGray
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.terms.content.AcceptTermsContent
import com.civilcam.ui.terms.content.WebButton
import com.civilcam.ui.terms.model.TermsActions
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun TermsScreenContent(viewModel: TermsViewModel) {

    var isAccepted by remember { mutableStateOf(false) }

    Scaffold(
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.terms_conditions_title),
                    titleSize = 16,
                    navigationAction = {
                        viewModel.setInputActions(TermsActions.ClickGoBack)
                    },
                )
                DividerLightGray()
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
                    text = "Terms & Conditions\nand Privacy Policy",
                    style = CCTheme.typography.big_title,
                    modifier = Modifier.padding(bottom = 20.dp),
                    color = CCTheme.colors.grayOneDark
                )

                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Lorem sit sit lacus nisl donec egestas amet id consequat. Ut velit accumsan in lorem lectus et quis.",
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

                AcceptTermsContent(isAccepted) {
                    isAccepted = !isAccepted
                }

            }

            ComposeButton(
                title = stringResource(id = R.string.continue_text),
                Modifier.padding(horizontal = 8.dp),
                isActivated = isAccepted,
                buttonClick = {
                    viewModel.setInputActions(TermsActions.ClickContinue)
                }
            )
        }
    }
}