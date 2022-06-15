package com.civilcam.ui.langSelect

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.LanguageType
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.langSelect.content.SegmentedDemo

@Composable
fun LanguageSelectScreenContent(viewModel: LanguageSelectViewModel) {

    var tabPage by remember { mutableStateOf(LanguageType.ENGLISH) }


    Scaffold(modifier = Modifier.fillMaxSize(),
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
            BottomCard(tabPage) {
                tabPage = it
            }
        })
}

@Composable
fun BottomCard(tabPage: LanguageType, tabSelected: (LanguageType) -> Unit) {
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
                "Hello, welcome to CivilCam!",
                style = CCTheme.typography.big_title,
                color = CCTheme.colors.primaryRed,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 21.dp)
            )

            SegmentedDemo()

            Text(
                "You can always change it in settings",
                style = CCTheme.typography.common_text,
                color = CCTheme.colors.grayText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 90.dp, top = 21.dp)
            )

            ComposeButton(
                "Continue",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {

            }
        }
    }
}

