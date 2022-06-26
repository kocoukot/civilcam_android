package com.civilcam.common.ext

import android.util.Patterns
import java.util.concurrent.TimeUnit


fun String.letters() = filter { it.isLetter() || it == ' ' }

fun String.digits() = filter { it.isDigit() }

fun CharSequence?.isEmail() =
	!this.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
