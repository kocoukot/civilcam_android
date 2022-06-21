@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.terms

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.TermsType
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.terms.content.WebButton
import com.civilcam.ui.terms.model.TermsActions
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun TermsScreenContent(viewModel: TermsViewModel) {

    var isAccepted by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState()
    Scaffold(
        backgroundColor = CCTheme.colors.lightGray,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.terms_conditions_title),
                    titleSize = 15,
                    navigationTitle = stringResource(R.string.back_text),
                    navigationAction = {
                        viewModel.setInputActions(TermsActions.ClickGoBack)
                    },
                )

                Divider(
                    color = CCTheme.colors.grayThree,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
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


@Composable
fun AcceptTermsContent(isAccepted: Boolean, acceptTerms: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                acceptTerms.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .border(
                    2.dp,
                    if (isAccepted) CCTheme.colors.primaryRed else CCTheme.colors.grayOne,
                    shape = RoundedCornerShape(2.dp)
                ),
            contentAlignment = Alignment.Center,

            ) {
            Checkbox(
                modifier = Modifier.padding(5.dp),
                colors = CheckboxDefaults.colors(
                    uncheckedColor = CCTheme.colors.lightGray,
                    checkedColor = CCTheme.colors.lightGray,
                    checkmarkColor = CCTheme.colors.primaryRed,
                ),
                checked = isAccepted,
                onCheckedChange = { acceptTerms.invoke() }
            )
        }


        Text(modifier = Modifier
            .padding(end = 12.dp, top = 6.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.grayOne,
                        fontWeight = FontWeight.W400
                    ),
                ) {
                    append("${context.resources.getString(R.string.terms_conditions_i_agree)} ")
                }

                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.black,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append(context.resources.getString(R.string.terms_conditions_terms_button))
                }

                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.grayOne,
                        fontWeight = FontWeight.W400
                    ),
                ) {
                    append(" ${context.resources.getString(R.string.and_text)}\n")
                }
                withStyle(
                    style = SpanStyle(
                        color = CCTheme.colors.black,
                        fontWeight = FontWeight.W500
                    ),
                ) {
                    append(context.resources.getString(R.string.terms_conditions_privacy_button))
                }
            })
    }
}
