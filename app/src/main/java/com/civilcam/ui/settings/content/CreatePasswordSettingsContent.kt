package com.civilcam.ui.settings.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.create.content.PasswordCreateContent
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.create.model.PasswordModel

@Composable
fun CreatePasswordSettingsContent(
    passwordData: PasswordModel,
    passwordEntered: (PasswordInputDataType, Boolean, String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(CCTheme.colors.white)
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))

        PasswordCreateContent(
            passwordData,
            isBackgroundReversed = true
        ) { type, meetCriteria, password ->
            passwordEntered.invoke(type, meetCriteria, password)
        }
    }
}