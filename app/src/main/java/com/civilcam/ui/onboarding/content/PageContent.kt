package com.civilcam.ui.onboarding.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.OnboardingPage

@Composable
fun PageUI(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(page.image),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
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