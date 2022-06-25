package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.settings.NotificationsType
import com.civilcam.ui.common.compose.RowDividerGrayThree
import com.civilcam.ui.settings.model.SettingsAlertsSectionData
import timber.log.Timber

@Composable
fun AlertsSettingsContent(
    data: SettingsAlertsSectionData,
    onSwitchChanged: (Boolean, NotificationsType) -> Unit
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
            isSwitched = data.isSMS,
            onCheckedChange = { onSwitchChanged.invoke(it, NotificationsType.SMS) },
        )
        RowDividerGrayThree()


        SwitcherRowContent(
            title = stringResource(id = R.string.settings_alerts_email),
            isSwitched = data.isEmail,
            onCheckedChange = { onSwitchChanged.invoke(it, NotificationsType.EMAIL) },
        )
        RowDividerGrayThree(0)

    }
}

@Composable
fun SwitcherRowContent(
    title: String,
    isSwitched: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isSwitched,
            onCheckedChange = {
                Timber.d("updateSettingsModel switcher $it")
                onCheckedChange.invoke(it)
            })
    }
}
