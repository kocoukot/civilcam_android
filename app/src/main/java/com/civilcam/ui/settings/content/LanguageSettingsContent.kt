package com.civilcam.ui.settings.content

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme

@Composable
fun LanguageSettingsContent(
    currentLanguage: com.civilcam.domainLayer.model.LanguageType,
    onLanguageSelect: (com.civilcam.domainLayer.model.LanguageType) -> Unit
) {

//    var currentLanguage by remember { mutableStateOf(LocaleHelper.getSelectedLanguage()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Divider(color = CCTheme.colors.lightGray, modifier = Modifier.height(30.dp))

        LanguageSelectSection(
            com.civilcam.domainLayer.model.LanguageType.ENGLISH,
            currentLanguage = currentLanguage,
            onLanguageSelect = {
//                currentLanguage = LanguageType.ENGLISH
                onLanguageSelect.invoke(com.civilcam.domainLayer.model.LanguageType.ENGLISH)
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        LanguageSelectSection(
            com.civilcam.domainLayer.model.LanguageType.SPAIN,
            currentLanguage = currentLanguage,
            onLanguageSelect = {
//                currentLanguage = LanguageType.SPAIN
                onLanguageSelect.invoke(com.civilcam.domainLayer.model.LanguageType.SPAIN)
            }
        )
    }
}

@Composable
fun LanguageSelectSection(
    languageType: com.civilcam.domainLayer.model.LanguageType,
    currentLanguage: com.civilcam.domainLayer.model.LanguageType,
    onLanguageSelect: () -> Unit
) {

    val textColor by animateColorAsState(targetValue = if (currentLanguage == languageType) CCTheme.colors.black else CCTheme.colors.grayOne)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CCTheme.colors.white, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                onLanguageSelect.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = languageType.langTitle,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            color = textColor
        )
        Crossfade(targetState = currentLanguage == languageType) {
            RadioButton(
                selected = currentLanguage == languageType,
                colors = RadioButtonDefaults.colors(
                    selectedColor = CCTheme.colors.primaryRed,
                    unselectedColor = CCTheme.colors.grayThree,
                ),
                onClick = {
                    onLanguageSelect.invoke()
                })
        }
    }
}


