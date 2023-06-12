package com.onpu.domain.util

import java.text.SimpleDateFormat
import java.util.*

val dateFormat get() = arrayListOf(
    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
    "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
    "yyyy-MM-dd'T'HH:mm:ss.SSSz",
    "yyyy-MM-dd'T'HH:mm:ss.SSS",
    "yyyy-MM-dd HH:mm:ss.SSS",

    "yyyy-MM-dd'T'HH:mm:ss'Z'",
    "yyyy-MM-dd'T'HH:mm:ssZ",
    "yyyy-MM-dd'T'HH:mm:ssz",
    "yyyy-MM-dd'T'HH:mm:ss",
    "yyyy-MM-dd HH:mm:ss",

    "yyyy-MM-dd'T'HH:mm'Z'",
    "yyyy-MM-dd'T'HH:mmZ",
    "yyyy-MM-dd'T'HH:mmz",
    "yyyy-MM-dd'T'HH:mm",
    "yyyy-MM-dd HH:mm",

    "yyyy-MM-dd'T'HH",
    "yyyy-MM-dd HH",

    "yyyy-MM-dd",

    "yy-MM-dd",

    "dd.MM.yy"
)

val utcTimeZone get() = timeZone("UTC+0")
val defaultTimeZone get() = TimeZone.getDefault()

fun Calendar.formatDate(format: String, timeZone: TimeZone? = null): String = dateFormat(format, timeZone)
    .format(time)

@Suppress("SimpleDateFormat")
fun dateFormat(pattern: String, timeZone: TimeZone? = null) = SimpleDateFormat(pattern)
    .apply {  timeZone?.let { this.timeZone = it } }

fun timeZone(id: String) = TimeZone.getTimeZone(id)

val Calendar?.hasPassed get() = this?.timeInMillis.orZero < Calendar.getInstance().timeInMillis

val calendar: Calendar get() = Calendar.getInstance()

val calendarUTC: Calendar get() = Calendar.getInstance(utcTimeZone)

val calendarStartTime get() = calendarUTC.apply { timeInMillis = 0.toLong() }

val Calendar?.orCalendarStartTime get() = this ?: calendarStartTime

fun Calendar.toAge(): String {
    val currentDate = Calendar.getInstance()
    val age = if (currentDate.dayOfYear > this.dayOfYear) {
        currentDate.year - this.year
    } else (currentDate.year - this.year) - 1
    return when {
        (age % 10 == 1) && (age in 21..100) -> "$age год"
        (age % 10 == 2) && (age in 21..100) -> "$age года"
        (age % 10 == 3) && (age in 21..100) -> "$age года"
        (age % 10 == 4) && (age in 21..100) -> "$age года"
        else -> "$age лет"
    }
}

fun calendar(year: Int = 0,
             month: Int = 0,
             dayOfMonth: Int = 0,
             hourOfDay: Int = 0,
             minute: Int = 0,
             second: Int = 0): Calendar = Calendar
    .getInstance(TimeZone.getTimeZone("UTC+0"))
    .apply {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
        this.hourOfDay = hourOfDay
        this.minute = minute
        this.second = second
    }

var Calendar.year
    get() = this[Calendar.YEAR]
    set(value) { this[Calendar.YEAR] = value }
var Calendar.month
    get() = this[Calendar.MONTH]
    set(value) { this[Calendar.MONTH] = value }
var Calendar.dayOfMonth
    get() = this[Calendar.DAY_OF_MONTH]
    set(value) { this[Calendar.DAY_OF_MONTH] = value }
var Calendar.dayOfWeek
    get() = this[Calendar.DAY_OF_WEEK]
    set(value) { this[Calendar.DAY_OF_WEEK] = value }
var Calendar.dayOfYear
    get() = this[Calendar.DAY_OF_YEAR]
    set(value) { this[Calendar.DAY_OF_YEAR] = value }
var Calendar.hourOfDay
    get() = this[Calendar.HOUR_OF_DAY]
    set(value) { this[Calendar.HOUR_OF_DAY] = value }
var Calendar.hour
    get() = this[Calendar.HOUR]
    set(value) { this[Calendar.HOUR] = value }
var Calendar.minute
    get() = this[Calendar.MINUTE]
    set(value) { this[Calendar.MINUTE] = value }
var Calendar.second
    get() = this[Calendar.SECOND]
    set(value) { this[Calendar.SECOND] = value }
