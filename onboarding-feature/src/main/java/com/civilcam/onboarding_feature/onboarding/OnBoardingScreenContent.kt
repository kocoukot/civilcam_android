package com.civilcam.onboarding_feature.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.model.OnboardingPage
import com.civilcam.ext_features.compose.elements.BackButton
import com.civilcam.ext_features.compose.elements.ComposeButton
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.onboarding_feature.R
import com.civilcam.onboarding_feature.onboarding.content.PageCardUI
import com.civilcam.onboarding_feature.onboarding.content.PageUI
import com.civilcam.onboarding_feature.onboarding.model.OnboardingActions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

@OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreenContent(viewModel: OnBoardingViewModel) {

    val firstPagerState = rememberPagerState()
    val secondPagerState = rememberPagerState()

    val scrollingFollowingPair by remember {
        derivedStateOf {
            if (firstPagerState.isScrollInProgress) {
                firstPagerState to secondPagerState
            } else if (secondPagerState.isScrollInProgress) {
                secondPagerState to firstPagerState
            } else null
        }
    }

    LaunchedEffect(scrollingFollowingPair) {
        val (scrollingState, followingState) = scrollingFollowingPair ?: return@LaunchedEffect

        snapshotFlow { scrollingState.currentPage + scrollingState.currentPageOffset }
            .collectLatest { pagePart ->
                val divideAndRemainder = BigDecimal.valueOf(pagePart.toDouble())
                    .divideAndRemainder(BigDecimal.ONE)
                followingState.scrollToPage(
                    divideAndRemainder[0].toInt(),
                    divideAndRemainder[1].toFloat(),
                )
            }
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            HorizontalPager(
                itemSpacing = 0.dp,
                count = onboardPages.size,
                state = firstPagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.Top
            ) { page ->
                PageUI(page = onboardPages[page])
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, start = 12.dp)
            ) {
                BackButton(tint = CCTheme.colors.white) {
                    viewModel.setInputActions(OnboardingActions.ClickGoBack)
                }
            }
        }

        Column(
            modifier = Modifier
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 12.dp, topEnd = 12.dp
                ),
                backgroundColor = CCTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                elevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    HorizontalPagerIndicator(
                        pagerState = secondPagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        activeColor = CCTheme.colors.primaryRed
                    )

                    HorizontalPager(
                        count = onboardPages.size,
                        state = secondPagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { page ->
                        PageCardUI(page = onboardPages[page])
                    }

                    Spacer(modifier = Modifier.height(48.dp))
                    ComposeButton(
                        title = stringResource(id = R.string.get_start_text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        isActivated = secondPagerState.currentPage == 2,
                        buttonClick = {
                            viewModel.setInputActions(OnboardingActions.ClickContinue)
                        },
                    )
                }
            }
        }
    }
}


private val onboardPages = listOf(
    OnboardingPage(
        title = R.string.onboarding_page_one_title,
        text = R.string.onboarding_page_one_text,
        image = R.drawable.img_onboarding_one,
    ),
    OnboardingPage(
        title = R.string.onboarding_page_two_title,
        text = R.string.onboarding_page_two_text,
        image = R.drawable.img_onboarding_two,
    ),
    OnboardingPage(
        title = R.string.onboarding_page_three_title,
        text = R.string.onboarding_page_three_text,
        image = R.drawable.img_onboarding_three,
    )
)
