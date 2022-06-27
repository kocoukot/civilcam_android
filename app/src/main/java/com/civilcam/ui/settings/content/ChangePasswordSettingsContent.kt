package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.PasswordField

@Composable
fun ChangePasswordSettingsContent(
    text: String,
    currentPassword: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(CCTheme.colors.white)
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))

        PasswordField(
            text = text,
            name = stringResource(id = R.string.settings_password_label),
            placeholder = stringResource(id = R.string.settings_password_placeholder),
            isReversed = true,
            onValueChanged = {
                currentPassword.invoke(it)
            }
        )
    }
}
