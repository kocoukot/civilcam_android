package com.civilcam.ext_features

import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
	 val dateOfBirthFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy", Locale.US)
	private val fullDateWithTime = SimpleDateFormat("MM.dd.yyyy HH:mm:ss a", Locale.US)
	private val dateTimeFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy, HH:mm a", Locale.US)
	private val dateOfBirthDomainFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
	
	private val dateOfBirthTimeFormatter = DateTimeFormatter.ofPattern("MM.dd.yyyy", Locale.US)
	private val dateOfBirthISOFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US)
	private val isoDateFormatter =
		DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
	var dateDeadlineFormatter: DateTimeFormatter =
		DateTimeFormatter.ofPattern("MM.dd.yyyy, h:mm a", Locale.US)
	
	//    fun dateOfBirthFormat(date: Long): String = dateOfBirthFormatter.format(date)
	fun dateOfBirthFormat(date: Long): String = dateOfBirthISOFormatter.format(
		Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
	)
	
	fun dateOfBirthFormat(date: String): String =
		dateOfBirthTimeFormatter.format(LocalDate.parse(date))
	
	fun dateOfBirthDomainFormat(date: Long): String = dateOfBirthDomainFormatter.format(date)
	fun dateOfBirthDomainFormat(date: String): String =
		dateOfBirthISOFormatter.format(LocalDate.parse(date))

	fun getFullDateAndTimeString(dateTime: Long): String = fullDateWithTime.format(dateTime)

	private fun localDateToIsoFormat(date: String): ZonedDateTime {
		val dateTime = LocalDateTime.parse(date, isoDateFormatter)
		return ZonedDateTime.ofLocal(dateTime, ZoneId.of("UTC"), ZoneOffset.UTC)
			.withZoneSameInstant(ZoneId.systemDefault())
	}

	private fun isoDateToLong(date: String): Long {
		val t = LocalDateTime.parse(date, isoDateFormatter)
		val zdt = ZonedDateTime.of(t, ZoneId.systemDefault())
		return zdt.toInstant().toEpochMilli()
	}

	fun isSubValid(date: String): Boolean {
		val current = LocalDateTime.now()
		return isoDateToLong(date) > ZonedDateTime.of(current, ZoneId.systemDefault()).toInstant()
			.toEpochMilli()
	}

	fun getDateFromIso(date: String): String {
		val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		val output = SimpleDateFormat("dd.MM.yyyy")
		val d = sdf.parse(date)
		val formattedTime = d?.let { output.format(it) }
		return formattedTime!!
	}

	fun alertDateFormat(date: String): String =
		dateDeadlineFormatter.format(localDateToIsoFormat(date))
	
	fun alertOnGuardDateFormat(date: String): String =
		dateOfBirthTimeFormatter.format(localDateToIsoFormat(date))
	
}