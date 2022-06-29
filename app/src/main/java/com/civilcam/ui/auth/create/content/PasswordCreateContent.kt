package com.civilcam.ui.auth.create.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.LengthCheckStrategy
import com.civilcam.ui.auth.LowerCaseCheckStrategy
import com.civilcam.ui.auth.OneDigitCheckStrategy
import com.civilcam.ui.auth.UpperCaseCheckStrategy
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.create.model.PasswordModel
import com.civilcam.ui.common.compose.PasswordField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PasswordCreateContent(
    model: PasswordModel,
    isBackgroundReversed: Boolean,
    passwordEntered: (PasswordInputDataType, Boolean, String) -> Unit
) {
    val checkedStrategies = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var passwordInput by remember { mutableStateOf(model.password) }
    Column(
        modifier = Modifier
            .background((if (isBackgroundReversed) CCTheme.colors.lightGray else CCTheme.colors.white))
    ) {
        PasswordField(
            name = stringResource(id = R.string.password),
            text = passwordInput,
            isReversed = isBackgroundReversed,
            placeholder = stringResource(id = R.string.create_password),
            hasError = checkedStrategies.value != 4,
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
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordStrategyBlocks(
            input = passwordInput,
            strategyUpdate = {
                checkedStrategies.value = it
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordField(
            isReversed = isBackgroundReversed,
            name = stringResource(id = R.string.confirm_password),
            text = model.confirmPassword,
            placeholder = stringResource(id = R.string.re_enter_password),
            onValueChanged = {
                passwordEntered.invoke(PasswordInputDataType.PASSWORD_REPEAT, true, it)
            },
            noMatch = model.noMatch,
            isReEnter = model.noMatch
        )
    }
}


@OptIn(ExperimentalUnitApi::class)
@Composable
private fun PasswordChip(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        modifier = Modifier
            .padding(top = 8.dp),
        fontSize = TextUnit(13f, TextUnitType.Sp)
    )
}

@Composable
private fun colorGet(isError: Boolean) =
    animateColorAsState(targetValue = if (isError) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)

@OptIn(ExperimentalUnitApi::class)
@Composable
private fun PasswordStrategyBlocks(
    input: String,
    strategyUpdate: (Int) -> Unit
) {
    val numberStateColor by colorGet(OneDigitCheckStrategy(input))
    val capitalLetterStateColor by colorGet(UpperCaseCheckStrategy(input))
    val lowerLetterStateColor by colorGet(LowerCaseCheckStrategy(input))
    val lengthStateColor by colorGet(LengthCheckStrategy(input))

    val checkChips = mutableMapOf(
        OneDigitCheckStrategy to false,
        UpperCaseCheckStrategy to false,
        LowerCaseCheckStrategy to false,
        LengthCheckStrategy to false
    )

    checkChips[OneDigitCheckStrategy] = OneDigitCheckStrategy(input)
    checkChips[UpperCaseCheckStrategy] = UpperCaseCheckStrategy(input)
    checkChips[LowerCaseCheckStrategy] = LowerCaseCheckStrategy(input)
    checkChips[LengthCheckStrategy] = LengthCheckStrategy(input)

    val checkedCount = checkChips.count { it.value }
    strategyUpdate.invoke(checkedCount)

    AnimatedVisibility(visible = checkedCount != composeChipsWithStrategiesList().size) {
        Column {
            Text(
                text = stringResource(id = R.string.password_should_contain),
                color = if (checkedCount > 0) CCTheme.colors.primaryRed else CCTheme.colors.grayOne,
                fontSize = TextUnit(13f, TextUnitType.Sp)
            )

            PasswordChip(
                text = stringResource(id = R.string.password_should_contain_number),
                color = numberStateColor
            )

            PasswordChip(
                text = stringResource(id = R.string.password_should_contain_letter),
                color = capitalLetterStateColor
            )

            PasswordChip(
                text = stringResource(id = R.string.password_should_contain_lowercase),
                color = lowerLetterStateColor
            )

            PasswordChip(
                text = stringResource(id = R.string.password_should_contain_char),
                color = lengthStateColor
            )

            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
    }
}

private fun composeChipsWithStrategiesList() =
    listOf(
        UpperCaseCheckStrategy,
        OneDigitCheckStrategy,
        LengthCheckStrategy,
        LowerCaseCheckStrategy,
    )