package com.civilcam.ui.langSelect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.LanguageType
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.langSelect.content.SegmentedItem
import com.civilcam.ui.langSelect.model.LangSelectActions
import com.civilcam.utils.LocaleHelper
import java.util.*

@Composable
fun LanguageSelectScreenContent(viewModel: LanguageSelectViewModel) {

    val state = viewModel.state.collectAsState()
    SetLanguage(state.value.selectedLang)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp)
                    .padding(top = 130.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_splash),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            BottomCard(state.value.selectedLang, {
                viewModel.setInputAction(LangSelectActions.LanguageSelect(it))
            }, {
                viewModel.setInputAction(LangSelectActions.ClickContinue)
            })
        })
}

@Composable
fun BottomCard(
    selectedLang: LanguageType,
    tabSelected: (LanguageType) -> Unit,
    continueClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(
            topStart = 12.dp, topEnd = 12.dp
        ),
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Text(
                stringResource(id = R.string.language_select_hello),
                style = CCTheme.typography.big_title,
                color = CCTheme.colors.primaryRed,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 21.dp)
                    .padding(horizontal = 30.dp)
            )

            SegmentedItem(selectedLang) {
                tabSelected.invoke(it)
            }

            Text(
                stringResource(id = R.string.language_select_settings),
                style = CCTheme.typography.common_text_regular,
                color = CCTheme.colors.grayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 90.dp, top = 16.dp)
            )

            ComposeButton(
                title = stringResource(id = R.string.continue_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                buttonClick = { continueClicked.invoke() }
            )
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


