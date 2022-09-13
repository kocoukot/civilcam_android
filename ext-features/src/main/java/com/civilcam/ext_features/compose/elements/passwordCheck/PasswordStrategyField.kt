package com.civilcam.ext_features.compose.elements.passwordCheck

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.civilcam.ext_features.R
import com.civilcam.ext_features.theme.CCTheme

@OptIn(ExperimentalUnitApi::class)
@Composable
fun PasswordStrategyBlocks(
    input: String,
    onLooseFocus: PasswordStrategyState = PasswordStrategyState.NONE,
    strategyUpdate: (Int) -> Unit
) {
	val numberStateColor by colorGet(
		OneDigitCheckStrategy(input),
		onLooseFocus == PasswordStrategyState.LOOSE_FOCUS
	)
	val capitalLetterStateColor by colorGet(
		UpperCaseCheckStrategy(input),
		onLooseFocus == PasswordStrategyState.LOOSE_FOCUS
	)
	val specialCharacterStateColor by colorGet(
		SpecialSymbolCheckStrategy(input),
		onLooseFocus == PasswordStrategyState.LOOSE_FOCUS
	)
	val lengthStateColor by colorGet(
		LengthCheckStrategy(input),
		onLooseFocus == PasswordStrategyState.LOOSE_FOCUS
	)

	val checkChips = mutableMapOf(
		OneDigitCheckStrategy to false,
		UpperCaseCheckStrategy to false,
		SpecialSymbolCheckStrategy to false,
		LengthCheckStrategy to false
	)

	checkChips[OneDigitCheckStrategy] = OneDigitCheckStrategy(input)
	checkChips[UpperCaseCheckStrategy] = UpperCaseCheckStrategy(input)
	checkChips[SpecialSymbolCheckStrategy] = SpecialSymbolCheckStrategy(input)
	checkChips[LengthCheckStrategy] = LengthCheckStrategy(input)
	
	val checkedCount = checkChips.count { it.value }
	strategyUpdate.invoke(checkedCount)
	
	AnimatedVisibility(visible = checkedCount != composeChipsWithStrategiesList().size) {
		Box {
			Column {
				Text(
					text = stringResource(id = R.string.password_should_contain),
					color = CCTheme.colors.grayOne, //if (checkedCount > 0) CCTheme.colors.primaryRed else ,
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
					text = stringResource(id = R.string.password_should_contain_special_char),
					color = specialCharacterStateColor
				)
				
				PasswordChip(
					text = stringResource(id = R.string.password_should_contain_char),
					color = lengthStateColor
				)

				Spacer(modifier = Modifier.padding(bottom = 8.dp))
			}
		}
	}
}

@Composable
private fun colorGet(
	isMatch: Boolean,
	isError: Boolean = false
) =
	animateColorAsState(targetValue = if (isMatch) CCTheme.colors.primaryGreen else if (isError) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)


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

private fun composeChipsWithStrategiesList() =
	listOf(
		UpperCaseCheckStrategy,
		OneDigitCheckStrategy,
		LengthCheckStrategy,
		SpecialSymbolCheckStrategy,
	)