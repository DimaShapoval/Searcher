package com.onpu.domain.util

import java.util.*
import java.util.concurrent.TimeUnit

// TODO Util Boolean

val Boolean?.orFalse get() = this ?: false
val Boolean?.orTrue get() = this ?: true


// TODO Char

val Char?.toIntOrZero get() = tryOr(0) { Integer.parseInt(toString()) }
val Char?.toIntOrMinusOne get() = tryOr(-1) { Integer.parseInt(toString()) }


// TODO Util String

fun String?.toInt(default: Int) = tryOr(default) {
    Integer.parseInt(orEmpty())
}
val String?.toIntOrZero get() = toInt(0)
val String?.toIntOrMinusOne get() = toInt(-1)
inline fun String?.toIntOrError(crossinline onError: (Exception) -> Unit) = try { Integer.parseInt(orEmpty()) } catch (e: Exception) { onError(e) }

fun String?.toLong(default: Long) = tryOr(default) {
    java.lang.Long.parseLong(orEmpty())
}
val String?.toLongOrZero get() = toLong(0L)
val String?.toLongOrMinusOne get() = toLong(-1L)
inline fun String?.toLongOrError(crossinline onError: (Exception) -> Unit) = try { java.lang.Long.parseLong(orEmpty()) } catch (e: Exception) { onError(e) }

fun String?.toFloat(default: Float) = tryOr(default) {
    java.lang.Float.parseFloat(orEmpty().replace(',', '.'))
}
val String?.toFloatOrZero get() = toFloat(0F)
val String?.toFloatOrMinusOne get() = toFloat(-1F)

fun String.indexBeforeFirst(char: Char): Int {
    val index = indexOfFirst { it == char }
    return when {
        index == -1 -> 0
        index > 0 -> index - 1
        else -> index
    }
}

inline fun String.clearAll(predicate: (Char) -> Boolean = Char::isWhitespace): String {
    var text = ""
    toCharArray().forEach { text += if (predicate(it)) "" else it }
    return text
}

inline fun CharSequence.clearAll(predicate: (Char) -> Boolean = Char::isWhitespace): CharSequence {
    var text = ""
    toString().toCharArray().forEach { text += if (predicate(it)) "" else it }
    return text
}

val String.clearMarks
    get() = replace("[-\\[\\]^/,'*:.!><~@#$%+=?|\"\\\\()]+".toRegex(), "_")
        .replace(" ", "_")

val String.fileName get() = replaceBeforeLast(this[lastIndexOf("/") + 1], "")

fun String.toCalendar(timeZone: TimeZone? = null): Calendar = toCalendarOrNull(timeZone) ?: calendar

fun String.toCalendarOrNull(timeZone: TimeZone? = null): Calendar? {
    var cal: Calendar? = null
    for (it in dateFormat) {
        cal = toCalendar(it, timeZone)
        if (cal != null) break
    }
    return cal
}

fun String.toCalendar(format: String, timeZone: TimeZone? = null): Calendar?  = tryOrNull {
    dateFormat(format, timeZone).parse(this)?.let { Calendar.getInstance().apply { timeInMillis = it.time } }
}

fun String.validateLatinSymbols() = this
    .matches("^[a-zA-Z0-9\\p{Punct}~\$^|<>+]+".toRegex())

fun String.validateSize() = length in 8..72

fun String.validateUppercase() : Boolean {
    val isLowerCase = contains("[a-z]+".toRegex())
    val isUpperCase = contains("[A-Z]+".toRegex())
    return isLowerCase && isUpperCase
}

fun String.validateNumber() = contains("[0-9]+".toRegex())

fun String.validateSymbols() = contains("[\\p{Punct}~\$^|<>+=]+".toRegex())

fun String.validatePhone() = length >= 19

fun String.validateName() = length !in 2..32 || validateSymbols() || isEmpty()

fun String.validateEmail() = matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())
fun String.validateEmailWithSymbols() = matches("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])".toRegex())

val String.checkPassAll get() = validateLatinSymbols()
        && validateSize()
        && validateUppercase()
        && validateNumber()
        && validateSymbols()

fun String.parseAsUAPhone() = replace(" ", "")
    .replace("(", "")
    .replace(")", "")
    .replace("+", "")
    .replace("-", "")

// TODO Util Image

// TODO Util Int, Long, Float, Double

fun Int?.or(default: Int) = this ?: default
fun Long?.or(default: Long) = this ?: default
fun Float?.or(default: Float) = this ?: default
fun Double?.or(default: Double) = this ?: default

val Int?.orZero get() = this ?: 0
val Long?.orZero get() = this ?: 0
val Float?.orZero get() = this ?: 0F
val Double?.orZero get() = this ?: 0.0

val Int?.isZero get() = orZero == 0
val Long?.isZero get() = orZero == 0L
val Float?.isZero get() = orZero == 0F
val Double?.isZero get() = orZero == 0.0

val Int?.orMinusOne get() = this ?: -1
val Long?.orMinusOne get() = this ?: -1
val Float?.orMinusOne get() = this ?: -1F
val Double?.orMinusOne get() = this ?: -1.0

val Int?.isMinusOne get() = orMinusOne == -1
val Long?.isMinusOne get() = orMinusOne == -1L
val Float?.isMinusOne get() = orMinusOne == -1F
val Double?.isMinusOne get() = orMinusOne == -1.0

val Int?.toString get() = String.format("%d", this ?: "")
val Long?.toString get() = String.format("%d", this ?: "")
val Float?.toString get() = String.format("%d", this ?: "")
val Double?.toString get() = String.format("%d", this ?: "")

fun Long.timeFormat(format: String, timeZone: TimeZone? = null) = toCalendar.formatDate(format, timeZone)

val Long.millisToSeconds get() = TimeUnit.MILLISECONDS.toSeconds(this)
val Long.millisToMinutes get() = TimeUnit.MILLISECONDS.toMinutes(this)
val Long.millisToHour get() = TimeUnit.MILLISECONDS.toHours(this)

val Long.secondsToMillis get() = TimeUnit.SECONDS.toMillis(this)
val Long.secondsToMinutes get() = TimeUnit.SECONDS.toMinutes(this)
val Long.secondsToHour get() = TimeUnit.SECONDS.toHours(this)

val Long.minutesToMillis get() = TimeUnit.MINUTES.toMillis(this)
val Long.minutesToSeconds get() = TimeUnit.MINUTES.toSeconds(this)
val Long.minutesToHour get() = TimeUnit.MINUTES.toHours(this)

val Long.hourToMillis get() = TimeUnit.HOURS.toMillis(this)
val Long.hourToSeconds get() = TimeUnit.HOURS.toSeconds(this)
val Long.hourToMinutes get() = TimeUnit.HOURS.toMinutes(this)

val Int.millisToSeconds get() = TimeUnit.MILLISECONDS.toSeconds(toLong())
val Int.millisToMinutes get() = TimeUnit.MILLISECONDS.toMinutes(toLong())
val Int.millisToHour get() = TimeUnit.MILLISECONDS.toHours(toLong())
//
val Int.secondsToMillis get() = TimeUnit.SECONDS.toMillis(toLong())
val Int.secondsToMinutes get() = TimeUnit.SECONDS.toMinutes(toLong())
val Int.secondsToHour get() = TimeUnit.SECONDS.toHours(toLong())

val Int.minutesToMillis get() = TimeUnit.MINUTES.toMillis(toLong())
val Int.minutesToSeconds get() = TimeUnit.MINUTES.toSeconds(toLong())
val Int.minutesToHour get() = TimeUnit.MINUTES.toHours(toLong())

val Int.hourToMillis get() = TimeUnit.HOURS.toMillis(toLong())
val Int.hourToSeconds get() = TimeUnit.HOURS.toSeconds(toLong())
val Int.hourToMinutes get() = TimeUnit.HOURS.toMinutes(toLong())

val Long.toCalendar: Calendar get() = calendar.apply { timeInMillis = this@toCalendar }

val Long.toCalendarUtc: Calendar get() = calendarUTC.apply { timeInMillis = this@toCalendarUtc }

fun Long.formatDate(format: String, timeZone: TimeZone? = null) = toCalendar.formatDate(format, timeZone)

fun timeToMillis(hour: Int, minute: Int) = hour.hourToMillis + minute.minutesToMillis

fun timeToCalendar(year: Int = 0, month: Int = 0, day: Int = 0, hour: Int = 0, minute: Int = 0, second: Int = 0): Calendar = calendar.apply {
    timeInMillis = 0
    this.year = year
    this.month = month
    dayOfMonth = day
    hourOfDay = hour
    this.minute = minute
    this.second = second
}
