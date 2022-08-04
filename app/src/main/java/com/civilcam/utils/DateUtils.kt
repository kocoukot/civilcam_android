package com.civilcam.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    private val isoDateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    private val dateOfBirthFormatter = SimpleDateFormat("MM.dd.yyyy", Locale.US)
    private val fullDateWithTime = SimpleDateFormat("MM.dd.yyyy HH:mm:ss a", Locale.US)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy, HH:mm a", Locale.US)

    fun dateOfBirthFormat(date: Long): String = dateOfBirthFormatter.format(date)
    fun getFullDateAndTimeString(dateTime: Long): String = fullDateWithTime.format(dateTime)

//    fun formatServerDate(timeString: String): String {
//        val zonedDateTime = localDateToIsoFormat(timeString, inputFormatter)
//        return "${dayAndMonthDateTimeFormatter.format(zonedDateTime)}, at ${
//            fullTimeDateTimeFormatter.format(
//                zonedDateTime
//            )
//        }"
//
//    }
//
//
//    private fun localDateToIsoFormat(date: String, inputFormat: DateTimeFormatter): ZonedDateTime {
//        val dateTime = LocalDateTime.parse(date, inputFormat)
//        return ZonedDateTime.ofLocal(dateTime, ZoneId.of("UTC"), ZoneOffset.UTC)
//            .withZoneSameInstant(ZoneId.systemDefault())
//    }

}