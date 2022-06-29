package com.civilcam.common.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.civilcam.R
import com.civilcam.ui.common.NavigationDirection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun NavController.navigateToRoot(
    @IdRes rootScreen: Int,
    @IdRes vararg backStack: Int = intArrayOf()
) {
    backStack.forEachIndexed { index, screen ->
        if (index == 0) {
            navigate(
                screen, null,
                NavOptions
                    .Builder()
                    .setPopUpTo(R.id.nav_graph, false)
                    .build()
            )
        } else {
            navigate(screen)
        }
    }
    navigate(
        rootScreen, null,
        NavOptions.Builder()
            .setPopUpTo(backStack.lastOrNull() ?: R.id.nav_graph, false)
            .build()
    )
}

fun NavController.navigateByDirection(
    direction: NavigationDirection
) {
    when (direction) {
        is NavigationDirection.SignInSuccess -> {
            navigate(R.id.emergency_root)
        }
        is NavigationDirection.EmailVerification -> {}
        is NavigationDirection.ProfileSetup -> {}
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun EditText.textChangedFlow(): Flow<String> {
    return callbackFlow {
        val textChangedListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySendBlocking(s?.toString().orEmpty().trim())
            }

        }
        this@textChangedFlow.addTextChangedListener(textChangedListener)
        awaitClose {
            this@textChangedFlow.removeTextChangedListener(textChangedListener)
        }
    }
}