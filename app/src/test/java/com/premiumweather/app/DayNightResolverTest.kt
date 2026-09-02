package com.premiumweather.app

import com.premiumweather.app.domain.mapper.DayNightResolver
import org.junit.Assert.*
import org.junit.Test
import java.time.Instant

class DayNightResolverTest {
    @Test fun isDayTrue() {
        val sunrise = Instant.parse("2024-01-01T06:00:00Z")
        val sunset = Instant.parse("2024-01-01T18:00:00Z")
        val now = Instant.parse("2024-01-01T12:00:00Z")
        assertTrue(DayNightResolver.isDay(now, sunrise, sunset))
    }
    @Test fun isDayFalse() {
        val sunrise = Instant.parse("2024-01-01T06:00:00Z")
        val sunset = Instant.parse("2024-01-01T18:00:00Z")
        val now = Instant.parse("2024-01-01T20:00:00Z")
        assertFalse(DayNightResolver.isDay(now, sunrise, sunset))
    }
    @Test fun flagResolver() {
        assertEquals(true, DayNightResolver.isDayFromFlag(1))
        assertEquals(false, DayNightResolver.isDayFromFlag(0))
        assertNull(DayNightResolver.isDayFromFlag(null))
    }
}
