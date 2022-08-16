package com.civilcam.common.ext

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.civilcam.R
import com.civilcam.ui.common.NavigationDirection
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

suspend fun <T> Task<T>.awaitResult() = suspendCoroutine<T?> { continuation ->
    if (isComplete) {
        if (isSuccessful) continuation.resume(this.result)
        else continuation.resume(null)
        return@suspendCoroutine
    }
    addOnSuccessListener { continuation.resume(this.result) }
    addOnFailureListener { continuation.resume(null) }
    addOnCanceledListener { continuation.resume(null) }
}