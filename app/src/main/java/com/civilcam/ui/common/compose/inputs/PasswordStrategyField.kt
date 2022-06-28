package com.civilcam.ui.common.compose.inputs

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
import com.civilcam.R
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.auth.LengthCheckStrategy
import com.civilcam.ui.auth.LowerCaseCheckStrategy
import com.civilcam.ui.auth.OneDigitCheckStrategy
import com.civilcam.ui.auth.UpperCaseCheckStrategy

@OptIn(ExperimentalUnitApi::class)
@Composable
fun PasswordStrategyBlocks(
	input: String,
	strategyUpdate: (Int) -> Unit
) {
	val numberStateColor by
	animateColorAsState(targetValue = if (OneDigitCheckStrategy(input)) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	val capitalLetterStateColor by
	animateColorAsState(targetValue = if (UpperCaseCheckStrategy(input)) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	val lowerLetterStateColor by
	animateColorAsState(targetValue = if (LowerCaseCheckStrategy(input)) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	val lengthStateColor by
	animateColorAsState(targetValue = if (LengthCheckStrategy(input)) CCTheme.colors.primaryRed else CCTheme.colors.grayOne)
	
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
		Box {
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

private fun composeChipsWithStrategiesList() =
	listOf(
		UpperCaseCheckStrategy,
		OneDigitCheckStrategy,
		LengthCheckStrategy,
		LowerCaseCheckStrategy,
	)