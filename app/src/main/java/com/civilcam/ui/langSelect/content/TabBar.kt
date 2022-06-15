package com.civilcam.ui.langSelect.content

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.LanguageType
import timber.log.Timber

@Composable
fun RecordTabBar(
    tabPage: LanguageType,
    onTabSelected: (tabPage: LanguageType) -> Unit
) {
    TabRow(
        modifier = Modifier
            .size(350.dp, 70.dp),
        selectedTabIndex = tabPage.ordinal,
//        backgroundColor = CCTheme.colors.lightGray, ,
        indicator = { tabPositions ->
            RecordTabIndicator(tabPositions, tabPage)
        }
    ) {
        RecordTab(
            isSelected = tabPage == LanguageType.ENGLISH,
            title = "English",
            onClick = {
                Timber.d("selectedRecords clicked personal")
                onTabSelected(LanguageType.ENGLISH)
            }
        )
        RecordTab(
            isSelected = tabPage == LanguageType.SPAIN,
            title = "Spain",
            onClick = {
                Timber.d("selectedRecords clicked world")
                onTabSelected(LanguageType.SPAIN)
            }
        )
    }
}

@Composable
private fun RecordTabIndicator(
    tabPositions: List<TabPosition>,
    tabPage: LanguageType
) {
    val transition = updateTransition(
        tabPage,
        label = "Tab indicator"
    )
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (LanguageType.ENGLISH isTransitioningTo LanguageType.SPAIN) {
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                spring(stiffness = Spring.StiffnessHigh)
            }
        },
        label = "Indicator left",

        ) { page ->
        tabPositions[page.ordinal].left
    }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (LanguageType.ENGLISH isTransitioningTo LanguageType.SPAIN) {
                spring(stiffness = Spring.StiffnessHigh)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator right"
    ) { page ->
        tabPositions[page.ordinal].right
    }

    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.CenterStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .fillMaxSize()
            .shadow(16.dp, RoundedCornerShape(4.dp), true)
            .background(
                CCTheme.colors.white,
                RoundedCornerShape(4.dp)
            )
    )
}

@Composable
private fun RecordTab(
    isSelected: Boolean,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(24.dp)
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = if (isSelected) CCTheme.colors.black else CCTheme.colors.grayText,
            text = title,
            modifier = Modifier.zIndex(2f),
            style = CCTheme.typography.roboto_bold_title,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun testToggle(selected: LanguageType) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.primaryRed, RoundedCornerShape(4.dp))
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .background(
                    CCTheme.colors.lightGray,
                    RoundedCornerShape(bottomStart = 4.dp, topStart = 4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = if (selected == LanguageType.ENGLISH) CCTheme.colors.black else CCTheme.colors.grayText,
                text = "English",
                modifier = Modifier.padding(vertical = 18.dp),
                style = CCTheme.typography.roboto_bold_title,
                textAlign = TextAlign.Center,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
                .background(
                    CCTheme.colors.lightGray,
                    RoundedCornerShape(bottomEnd = 4.dp, topEnd = 4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = if (selected == LanguageType.SPAIN) CCTheme.colors.black else CCTheme.colors.grayText,
                text = "Español",
                modifier = Modifier.padding(vertical = 18.dp),
                style = CCTheme.typography.roboto_bold_title,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private val BACKGROUND_SHAPE = RoundedCornerShape(8.dp)

@Composable
private fun Thumb(state: SegmentedControlState) {
    Box(
        Modifier
            .then(
                state.segmentScaleModifier(
                    pressed = state.pressedSegment == state.selectedSegment,
                    segment = state.selectedSegment
                )
            )
            .shadow(4.dp, BACKGROUND_SHAPE)
            .background(Color.Green, BACKGROUND_SHAPE)
    )
}
//
//@Preview
//@Composable
//private fun PreviewHomeTabBar() {
////    SegmentedDemo()
//    testToggle(LanguageType.ENGLISH)
////    RecordTabBar(
////        tabPage = LanguageType.ENGLISH,
////        onTabSelected = {}
////    )
//}