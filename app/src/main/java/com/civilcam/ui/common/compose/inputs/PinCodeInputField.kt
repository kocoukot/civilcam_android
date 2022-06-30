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
fun PinCodeInputField() {
	
	val inputPin = remember { mutableStateListOf<Int>() }
	val error = remember { mutableStateOf("") }
	val success = remember { mutableStateOf(false) }
	val password = "1234"
	val pinSize = 4

	if (inputPin.size == 4) {
		LaunchedEffect(key1 = true) {
			delay(300)

			if (inputPin.joinToString("") == password) {
				success.value = true
				error.value = ""
			} else {
				success.value = false
				error.value = "Wrong PIN"
			}
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(CCTheme.colors.white),
		horizontalAlignment = Alignment.CenterHorizontally
	) {

		Box(modifier = Modifier.fillMaxSize()) {
			Column(
				modifier = Modifier.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Spacer(modifier = Modifier.height(50.dp))

				Row {
					(0 until pinSize).forEach {
						Icon(
							imageVector = if (inputPin.size > it) Icons.Default.Circle else Icons.Outlined.Circle,
							contentDescription = null,
							modifier = Modifier.padding(24.dp)
								.size(8.dp)
						)
					}
				}
			}
		}

	}
}
