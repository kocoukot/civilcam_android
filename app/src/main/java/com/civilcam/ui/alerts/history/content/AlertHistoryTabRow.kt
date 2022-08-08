package com.civilcam.ui.alerts.history.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domainLayer.model.alerts.AlertType

@Composable
fun AlertHistoryTabRow(
    tabPage: AlertType,
    onTabSelected: (tabPage: AlertType) -> Unit
) {

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>(

        )
        repeat(2) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }


    TabRow(selectedTabIndex = tabPage.ordinal,
        modifier = Modifier,
        backgroundColor = CCTheme.colors.white,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .customTabIndicatorOffset(
                        currentTabPosition = tabPositions[tabPage.ordinal],
                        tabWidth = tabWidths[tabPage.ordinal]
                    )
                    .clip(RoundedCornerShape(topEnd = 2.dp, topStart = 2.dp)),
                color = CCTheme.colors.primaryRed,
                height = TabRowDefaults.IndicatorHeight * 1.5F,
            )
        }) {

        TabCell(
            isSelected = tabPage == AlertType.RECEIVED,
            title = stringResource(id = R.string.alerts_history_received_tab),
            textLength = { tabWidths[0] = it },
            onTabCLick = { onTabSelected(AlertType.RECEIVED) },

            )

        TabCell(
            isSelected = tabPage == AlertType.SENT,
            title = stringResource(id = R.string.alerts_history_sent_tab),
            textLength = { tabWidths[1] = it },
            onTabCLick = { onTabSelected(AlertType.SENT) },
        )

    }
}

@Composable
fun TabCell(
    isSelected: Boolean,
    title: String,
    textLength: (Dp) -> Unit,
    onTabCLick: () -> Unit
) {
    val density = LocalDensity.current

    val color =
        animateColorAsState(targetValue = if (isSelected) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
    Tab(
        selected = isSelected,
        onClick = onTabCLick
    ) {
        Text(
            text = title,
            style = CCTheme.typography.common_text_medium,
            color = color.value,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 12.dp),
            onTextLayout = { textLayoutResult ->
                with(density) { textLength.invoke(textLayoutResult.size.width.toDp()) }
            }
        )
    }
}

@Preview
@Composable
private fun AlertHistoryTabRowPreview() {
    var tabPage by remember { mutableStateOf(AlertType.RECEIVED) }

    AlertHistoryTabRow(
        tabPage = tabPage
    ) {
        tabPage = it
    }
}


fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    tabWidth: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth) / 2),
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}