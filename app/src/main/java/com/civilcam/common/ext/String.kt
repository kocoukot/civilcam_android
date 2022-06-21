package com.civilcam.common.ext


fun String.letters() = filter { it.isLetter() || it == ' ' }


