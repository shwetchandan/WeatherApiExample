package com.example.sm.weatherapiexample.utility

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeUtilsTest {

    @Test
    fun toReadableTime_returnsCorrectFormat() {
        val epochSeconds = 1700000000L
        val result = epochSeconds.toReadableTime()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(
            "Format should be HH:mm AM/PM, got: $result",
            result.matches(Regex("\\d{2}:\\d{2} (AM|PM)"))
        )
    }

    @Test
    fun toReadableTime_returnsNonEmptyForZero() {
        val result = 0L.toReadableTime()

        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun toReadableTime_handlesLargeTimestamps() {
        val futureTimestamp = 2000000000L
        val result = futureTimestamp.toReadableTime()

        assertTrue(result.matches(Regex("\\d{2}:\\d{2} (AM|PM)")))
    }

    @Test
    fun toReadableTime_matchesManualCalculation() {
        val epochSeconds = 1700000000L
        val expected = SimpleDateFormat("hh:mm a", Locale.getDefault())
            .format(Date(epochSeconds * 1000))

        val result = epochSeconds.toReadableTime()

        assertEquals(expected, result)
    }
}