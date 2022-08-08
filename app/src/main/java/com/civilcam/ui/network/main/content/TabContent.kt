package com.civilcam.ui.network.main.content

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.alerts.history.content.TabCell
import com.civilcam.ui.alerts.history.content.customTabIndicatorOffset


@Composable
fun NetworkTabRow(
    tabPage: com.civilcam.domainLayer.model.guard.NetworkType,
    onTabSelected: (tabPage: com.civilcam.domainLayer.model.guard.NetworkType) -> Unit
) {

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>(

        )
        repeat(2) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }


    TabRow(
        selectedTabIndex = tabPage.ordinal,
        divider = {},
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
            isSelected = tabPage == com.civilcam.domainLayer.model.guard.NetworkType.ON_GUARD,
            title = stringResource(id = R.string.network_main_on_guard),
            textLength = { tabWidths[0] = it },
            onTabCLick = { onTabSelected(com.civilcam.domainLayer.model.guard.NetworkType.ON_GUARD) },

            )

        TabCell(
            isSelected = tabPage == com.civilcam.domainLayer.model.guard.NetworkType.GUARDIANS,
            title = stringResource(id = R.string.network_main_guardians),
            textLength = { tabWidths[1] = it },
            onTabCLick = { onTabSelected(com.civilcam.domainLayer.model.guard.NetworkType.GUARDIANS) },
        )

    }
}
