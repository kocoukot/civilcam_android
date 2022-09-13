package com.civilcam.settings_feature.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.domainLayer.model.profile.PasswordModel
import com.civilcam.ext_features.compose.elements.passwordCheck.PasswordStrategyBlocks
import com.civilcam.ext_features.compose.elements.passwordCheck.PasswordStrategyState
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.settings_feature.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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


@Composable
fun PasswordCreateContent(
    model: PasswordModel,
    isBackgroundReversed: Boolean,
    passwordEntered: (PasswordInputDataType, Boolean, String) -> Unit
) {
    val checkedStrategies = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var passwordInput by remember { mutableStateOf(model.password) }
    var passwordFocusState by remember { mutableStateOf(PasswordStrategyState.NONE) }
    var passwordHadFocus by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background((if (isBackgroundReversed) CCTheme.colors.lightGray else CCTheme.colors.white))
    ) {
        com.civilcam.ext_features.compose.elements.PasswordField(
            name = stringResource(id = R.string.password),
            text = passwordInput,
            isReversed = isBackgroundReversed,
            placeholder = stringResource(id = R.string.create_password),
            //hasError = checkedStrategies.value != 4,
            noMatch = model.noMatch,
            onValueChanged = {
                passwordInput = it
                coroutineScope.launch {
                    delay(100)
                    passwordEntered.invoke(
                        PasswordInputDataType.PASSWORD,
                        checkedStrategies.value == 4,
                        it
                    )
                }
            },
            onFocusChanged = {
                if (it) passwordHadFocus = true
                if (passwordHadFocus) {
                    passwordFocusState =
                        if (!it) PasswordStrategyState.LOOSE_FOCUS else PasswordStrategyState.NONE
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordStrategyBlocks(
            input = passwordInput,
            strategyUpdate = {
                checkedStrategies.value = it
            },
            onLooseFocus = passwordFocusState
        )

        Spacer(modifier = Modifier.height(8.dp))

        com.civilcam.ext_features.compose.elements.PasswordField(
            isReversed = isBackgroundReversed,
            name = stringResource(id = R.string.confirm_password),
            text = model.confirmPassword,
            placeholder = stringResource(id = R.string.re_enter_password),
            onValueChanged = {
                passwordEntered.invoke(PasswordInputDataType.PASSWORD_REPEAT, true, it)
            },
            noMatch = model.noMatch,
            isReEnter = model.noMatch,
            onFocusChanged = {}
        )
    }
}