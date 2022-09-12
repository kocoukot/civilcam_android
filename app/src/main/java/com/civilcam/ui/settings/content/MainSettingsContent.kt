package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.ext_features.compose.elements.RowDivider
import com.civilcam.ext_features.compose.elements.RowDividerGrayThree
import com.civilcam.ext_features.theme.CCTheme
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
                    RowDivider()
                }
                SettingsType.CONTACT_SUPPORT, SettingsType.TERMS_AND_POLICY -> {
                    SettingsRow(
                        title = stringResource(id = type.rowTitle),
                        needDivider = false,
                        rowClick = { onRowClicked.invoke(type) })
                    RowDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    RowDivider()
                }
                SettingsType.DELETE_ACCOUNT -> {
                    SettingsRow(
                        title = stringResource(id = type.rowTitle),
                        titleColor = CCTheme.colors.primaryRed,
                        needDivider = false,
                        rowClick = { onRowClicked.invoke(type) })
                    RowDivider()
                }
                else -> {
                    if (type != SettingsType.CREATE_PASSWORD) {
                        SettingsRow(
                            title = stringResource(id = type.rowTitle),
                            rowClick = { onRowClicked.invoke(type) })
                    }
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
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = CCTheme.colors.black)
            ) { rowClick.invoke() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                color = titleColor,
                style = CCTheme.typography.common_medium_text_regular
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_row_arrow),
                contentDescription = null,
                tint = CCTheme.colors.grayOne
            )
        }
        if (needDivider) RowDividerGrayThree()
    }
}