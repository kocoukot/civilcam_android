package com.civilcam.ui.common.compose.inputs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinCodeInputField(
	pinCodeValue: (String) -> Unit
) {
	
	val inputPin = remember { mutableStateListOf<Int>() }
	val pinValue = remember { mutableStateOf("") }
	val pinSize = 4
	
	if (inputPin.size == 4) {
		LaunchedEffect(true) {
			delay(300)
			pinCodeValue.invoke(pinValue.value)
			inputPin.clear()
			pinValue.value = ""
		}
	}
	
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.background(CCTheme.colors.white),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		
		Box(
			modifier = Modifier.fillMaxWidth()
		) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				
				PinInputField(
					onValueChanged = {
						if (it.length > pinValue.value.length) {
							inputPin.add(it.toInt())
						}
						pinValue.value = it
					},
					onBackSpace = {
						if ((pinValue.value.isNotEmpty() || pinValue.value == "")
							&& inputPin.size > 0
						) {
							inputPin.removeLast()
						}
					}
				)
				
				Row(
					modifier = Modifier.offset(y = (-52).dp)
				) {
					(0 until pinSize).forEach {
						Icon(
							imageVector = if (inputPin.size > it) Icons.Default.Circle else Icons.Outlined.Circle,
							contentDescription = null,
							modifier = Modifier
								.padding(12.dp)
								.size(8.dp),
							tint = CCTheme.colors.black
						)
					}
				}
			}
		}
	}
}
