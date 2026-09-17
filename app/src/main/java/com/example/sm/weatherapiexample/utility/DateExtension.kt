package com.example.sm.weatherapiexample.utility

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MILLIS_PER_SECOND = 1000L

fun Long.toReadableTime(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(this * MILLIS_PER_SECOND))
}
