package com.civilcam.settings_feature.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.model.user.SettingsNotificationType
import com.civilcam.ext_features.compose.elements.RowDividerGrayThree
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.settings_feature.R
import com.civilcam.settings_feature.model.SettingsAlertsSectionData

@Composable
fun AlertsSettingsContent(
    data: SettingsAlertsSectionData,
    onSwitchChanged: (Boolean, SettingsNotificationType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white)
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))
        RowDividerGrayThree(0)


        SwitcherRowContent(
            title = stringResource(id = R.string.settings_alerts_sms),
            _isSwitched = data.isSMS,
            onCheckedChange = { onSwitchChanged.invoke(it, SettingsNotificationType.SMS) },
        )
        RowDividerGrayThree()


        SwitcherRowContent(
            title = stringResource(id = R.string.settings_alerts_email),
            _isSwitched = data.isEmail,
            onCheckedChange = { onSwitchChanged.invoke(it, SettingsNotificationType.EMAIL) },
        )
        RowDividerGrayThree(0)

    }
}

@Composable
fun SwitcherRowContent(
    title: String,
    _isSwitched: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    var isSwitched by remember { mutableStateOf(_isSwitched) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = CCTheme.typography.common_medium_text_regular
        )

        Switch(
            colors = SwitchDefaults.colors(
                checkedThumbColor = CCTheme.colors.primaryRed,
                checkedTrackColor = CCTheme.colors.primaryRed,
                checkedTrackAlpha = 0.5f,
                uncheckedThumbColor = CCTheme.colors.white,
                uncheckedTrackColor = CCTheme.colors.grayThree,
                uncheckedTrackAlpha = 1f,
            ),

            checked = isSwitched,
            onCheckedChange = {
                isSwitched = it
                onCheckedChange.invoke(it)
            })
    }
}
