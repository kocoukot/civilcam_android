package com.civilcam.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val isoDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    private val dateOfBirthFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    fun dateOfBirthFormat(date: Long): String = dateOfBirthFormatter.format(date)

}