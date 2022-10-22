package com.civilcam.ext_features

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.fragment.app.Fragment


fun Fragment.hideKeyboard() {
	view?.let {
		val inputMethodManager =
			activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
	}
}

private fun Context.hideKeyboard(view: View) {
	val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showKeyboard() {
	val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.showKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}


enum class Keyboard {
	Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
	val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
	val view = LocalView.current
	DisposableEffect(view) {
		val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
			val rect = Rect()
			view.getWindowVisibleDisplayFrame(rect)
			val screenHeight = view.rootView.height
			val keypadHeight = screenHeight - rect.bottom
			keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
				Keyboard.Opened
			} else {
				Keyboard.Closed
			}
		}
		view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
		
		onDispose {
			view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
		}
	}
	return keyboardState
}
