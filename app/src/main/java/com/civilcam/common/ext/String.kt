package com.civilcam.common.ext

import android.util.Patterns


fun String.letters() = filter { it.isLetter() || it == ' ' }

fun String.digits() = filter { it.isDigit() }

fun CharSequence?.isEmail() =
	!this.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String.clearPhone() = this
	.replace("+", "")
	.replace("(", "")
	.replace(")", "")
	.replace(" ", "")
	.replace("-", "")
