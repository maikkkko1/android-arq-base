package com.maikkkko1.android_base_arq.arq.functions

import com.maikkkko1.android_base_arq.arq.extensions.pluralize
import java.util.*
import kotlin.collections.HashMap

fun addMinutesToDate(date: Date, minutes: Int): Date {
    return Calendar.getInstance().apply {
        time = date
        add(Calendar.MINUTE, minutes)
    }.time
}

fun formatTimeTo12HourFormat(hour: Int): HashMap<String, Any> {
    var dayHour = hour
    var isPm = false

    if (dayHour in 12..23) {
        isPm = true
        if (dayHour > 12) dayHour -= 12
    }

    return HashMap<String, Any>().apply {
        put("dayHour", if (dayHour == 0) 12 else dayHour)
        put("dayTime", if (isPm) "pm" else "am")
    }
}

fun getDaysList(): List<String> {
    return listOf(
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    )
}

fun getMonthList(): List<String> {
    return getMonthFullNameList().map { it.substring(0, 3) }
}

fun getMonthFullNameList(): List<String> {
    return listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    )
}

fun getDayOrdinalSuffix(dayNum: Int): String {
    if (dayNum !in 1..31) {
        throw Exception("Invalid day of month")
    }

    var sufix = ""

    if (dayNum in 11..13) {
        sufix = "th"
    } else when (dayNum % 10) {
        1 -> sufix = "st"
        2 -> sufix = "nd"
        3 -> sufix = "rd"
        else -> sufix = "th"
    }

    return "${dayNum}${sufix}"
}

fun formatTimeToShow(hour: Any, minute: Any, dayTime: Any? = null): String {
    return String.format("%d:%02d", hour, minute) + dayTime
}

fun getDifferenceBetweenDates(startDate: Date, endDate: Date): DifferenceBetweenDates {
    var different: Long = endDate.time - startDate.time

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli
    different %= daysInMilli;

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    return DifferenceBetweenDates(
        days = elapsedDays.toInt(), hours = elapsedHours.toInt(), minutes = elapsedMinutes.toInt()
    )
}

/*
Deprecated, use getDifferenceBetweenDates instead.
 */
@Deprecated(message = "Deprecated", level = DeprecationLevel.WARNING)
fun getDiffBetweenTwoDates(startDate: Date, endDate: Date): HashMap<String, Long> {
    var different: Long = endDate.time - startDate.time

    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli
    different %= daysInMilli;

    val elapsedHours = different / hoursInMilli
    different %= hoursInMilli

    val elapsedMinutes = different / minutesInMilli
    different %= minutesInMilli

    return HashMap<String, Long>().apply {
        put("days", elapsedDays)
        put("hours", elapsedHours)
        put("minutes", elapsedMinutes)
    }
}

data class DifferenceBetweenDates(
    val days: Int, val hours: Int, val minutes: Int
) {
    fun getDaysAsString(emptyIfZero: Boolean = false): String = if (emptyIfZero && days == 0) "" else "$days" + " day".pluralize(days)
    fun getHoursAsString(emptyIfZero: Boolean = false): String = if (emptyIfZero && hours == 0) "" else "$hours" + " hour".pluralize(days)
    fun getMinutesAsString(emptyIfZero: Boolean = false): String = if (emptyIfZero && minutes == 0) "" else "$minutes" + " minute".pluralize(days)
}