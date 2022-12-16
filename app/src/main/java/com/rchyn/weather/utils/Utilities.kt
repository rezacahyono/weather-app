package com.rchyn.weather.utils

import android.icu.util.Calendar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun checkNight(): Boolean {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..4 -> true
        in 5..17 -> false
        in 18..23 -> true
        else -> false
    }
}

fun LocalDateTime.formatedTime(formated: String = "HH MMM yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(formated))
}