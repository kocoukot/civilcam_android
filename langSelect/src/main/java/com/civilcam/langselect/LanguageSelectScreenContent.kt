package com.civilcam.langselect

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.ext.LocaleHelper
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.ext_features.compose.elements.ComposeButton
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.langselect.content.SegmentedItem
import com.civilcam.langselect.model.LangSelectActions
import timber.log.Timber

@Composable
fun LanguageSelectScreenContent(viewModel: LanguageSelectViewModel) {

    val state = viewModel.state.collectAsState()
    LocaleHelper.SetLanguageCompose(state.value.selectedLang)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Timber.i("screenHeight $screenHeight")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight - 300.dp)
                    .padding(horizontal = 48.dp)
                    .padding(bottom = 15.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.img_splash),
                    contentDescription = null,
                    alignment = Alignment.Center
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BottomCard(state.value.selectedLang, viewModel::setInputActions)
            }
        },
    )
}

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun BottomCard(
    selectedLang: LanguageType,
    actionClicked: (LangSelectActions) -> Unit
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
            Crossfade(targetState = selectedLang) {
                Text(
                    stringResource(id = R.string.language_select_hello),
                    style = CCTheme.typography.big_title,
                    color = CCTheme.colors.primaryRed,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 21.dp)
                        .padding(horizontal = 30.dp)
                )
            }

            SegmentedItem(selectedLang) { actionClicked.invoke(LangSelectActions.LanguageSelect(it)) }
            Crossfade(
                targetState = selectedLang,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(id = R.string.language_select_settings),
                        style = CCTheme.typography.common_text_regular,
                        color = CCTheme.colors.grayOne,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 70.dp, top = 16.dp)
                    )
                }
            }
            ComposeButton(
                title = stringResource(id = R.string.continue_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                buttonClick = { actionClicked.invoke(LangSelectActions.ClickContinue) }
            )
        }
    }
}


