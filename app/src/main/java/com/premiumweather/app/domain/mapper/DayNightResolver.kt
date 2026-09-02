package com.premiumweather.app.domain.mapper

import java.time.Instant

object DayNightResolver {
    fun isDay(now: Instant, sunrise: Instant?, sunset: Instant?): Boolean {
        if (sunrise == null || sunset == null) return true
        return !now.isBefore(sunrise) && now.isBefore(sunset)
    }

    fun isDayFromFlag(flag: Int?): Boolean? {
        return when(flag) {
            1 -> true
            0 -> false
            else -> null
        }
    }
}
