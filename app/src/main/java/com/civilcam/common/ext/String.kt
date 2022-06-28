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

fun String.formatToPhoneNumber(): String = String.format(
    "%s (%s) %s %s", this.substring(0, 2), this.substring(2, 5),
    this.substring(5, 8), this.substring(8, 12)
)
