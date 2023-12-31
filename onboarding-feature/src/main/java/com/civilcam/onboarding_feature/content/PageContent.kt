package com.civilcam.onboarding_feature.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.model.OnboardingPage
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun PageUI(page: OnboardingPage) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val imagePadding = (screenHeight - 240.dp) / screenHeight
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(imagePadding)
            .background(CCTheme.colors.primaryRed),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )
    }
}

@Composable
fun PageCardUI(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(page.title),
            style = CCTheme.typography.big_title,
            color = CCTheme.colors.primaryRed,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = stringResource(id = page.text),
            style = CCTheme.typography.common_text_regular,
            textAlign = TextAlign.Center,
            color = CCTheme.colors.black,
        )

    }
}