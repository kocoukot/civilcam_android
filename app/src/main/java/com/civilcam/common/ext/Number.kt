package com.civilcam.common.ext

import android.content.res.Resources
import java.util.concurrent.TimeUnit

fun Int.toDp(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Float.toDp(): Float = (this * Resources.getSystem().displayMetrics.density)

fun Long.formatTime(): String = String.format(
	"%02d",
	TimeUnit.MILLISECONDS.toSeconds(this) -
			TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
)

