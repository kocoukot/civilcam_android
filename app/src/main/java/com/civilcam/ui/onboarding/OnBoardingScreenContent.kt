@file:OptIn(ExperimentalPagerApi::class)

package com.civilcam.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.OnboardingPage
import com.civilcam.ui.common.compose.ComposeButton
import com.civilcam.ui.onboarding.content.PageCardUI
import com.civilcam.ui.onboarding.content.PageUI
import com.civilcam.ui.onboarding.model.OnboardingActions
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal

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
            .collect { pagePart ->
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
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            HorizontalPager(
                count = onboardPages.size,
                state = firstPagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { page ->
                PageUI(page = onboardPages[page])
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp)
            ) {
                IconButton(onClick = { viewModel.setInputActions(OnboardingActions.CLickGoBack) }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_navigation),
                            contentDescription = null,
                            tint = CCTheme.colors.white
                        )
                        Text(
                            stringResource(id = R.string.back_text),
                            style = CCTheme.typography.common_text_medium,
                            color = CCTheme.colors.white
                        )
                    }
                }
            }

        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Card(
                shape = RoundedCornerShape(
                    topStart = 12.dp, topEnd = 12.dp
                ),
                backgroundColor = CCTheme.colors.white,
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 8.dp,

                ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
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
                            .fillMaxWidth(),
                        isActivated = secondPagerState.currentPage == 2,
                        buttonClick = {
                            viewModel.setInputActions(OnboardingActions.CLickContinue)
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
