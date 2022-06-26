package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.settings.model.SettingsType

@Composable
fun MainSettingsContent(
    onRowClicked: (SettingsType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (type in SettingsType.values()) {
            when (type) {
                SettingsType.MAIN -> {
                    Spacer(modifier = Modifier.height(30.dp))
                    Divider(color = CCTheme.colors.grayThree)
                }
                SettingsType.CONTACT_SUPPORT, SettingsType.TERMS_AND_POLICY -> {
                    SettingsRow(
                        title = stringResource(id = type.title),
                        needDivider = false,
                        rowClick = { onRowClicked.invoke(type) })
                    Divider(color = CCTheme.colors.grayThree)
                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = CCTheme.colors.grayThree)
                }
                SettingsType.DELETE_ACCOUNT -> {
                    SettingsRow(
                        title = stringResource(id = type.title),
                        titleColor = CCTheme.colors.primaryRed,
                        needDivider = false,
                        rowClick = { onRowClicked.invoke(type) })
                    Divider(color = CCTheme.colors.grayThree)
                }
                else -> {
                    SettingsRow(
                        title = stringResource(id = type.title),
                        rowClick = { onRowClicked.invoke(type) })
                }
            }
        }
    }
}


@Composable
fun SettingsRow(
    title: String,
    titleColor: Color = CCTheme.colors.black,
    needDivider: Boolean = true,
    rowClick: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .background(CCTheme.colors.white)
            .clickable { rowClick.invoke() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = title,
                color = titleColor,
                style = CCTheme.typography.common_medium_text_regular
            )
            Spacer(modifier = Modifier.weight(1f))
//                    Icon(painter = painterResource(id = R), contentDescription = )
        }
        if (needDivider) Divider(
            color = CCTheme.colors.grayThree,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}