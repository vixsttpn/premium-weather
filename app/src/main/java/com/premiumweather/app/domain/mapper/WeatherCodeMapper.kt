package com.premiumweather.app.domain.mapper

import com.premiumweather.app.domain.model.WeatherCondition

object WeatherCodeMapper {
    fun map(wmoCode: Int?, isDay: Boolean?): WeatherCondition {
        if (wmoCode == null) return WeatherCondition.UNKNOWN
        return when (wmoCode) {
            0 -> if (isDay == false) WeatherCondition.CLEAR_NIGHT else WeatherCondition.CLEAR_DAY
            1 -> if (isDay == false) WeatherCondition.PARTLY_CLOUDY_NIGHT else WeatherCondition.PARTLY_CLOUDY_DAY
            2 -> if (isDay == false) WeatherCondition.PARTLY_CLOUDY_NIGHT else WeatherCondition.PARTLY_CLOUDY_DAY
            3 -> WeatherCondition.CLOUDY
            45, 48 -> WeatherCondition.FOG
            51, 53, 55 -> WeatherCondition.DRIZZLE
            56, 57 -> WeatherCondition.FREEZING_RAIN
            61 -> WeatherCondition.RAIN
            63 -> WeatherCondition.RAIN
            65 -> WeatherCondition.HEAVY_RAIN
            66, 67 -> WeatherCondition.FREEZING_RAIN
            71 -> WeatherCondition.SNOW
            73 -> WeatherCondition.SNOW
            75 -> WeatherCondition.HEAVY_SNOW
            77 -> WeatherCondition.SNOW
            80 -> WeatherCondition.RAIN
            81 -> WeatherCondition.RAIN
            82 -> WeatherCondition.HEAVY_RAIN
            85, 86 -> WeatherCondition.HEAVY_SNOW
            95 -> WeatherCondition.THUNDERSTORM
            96, 99 -> WeatherCondition.THUNDERSTORM_RAIN
            else -> WeatherCondition.UNKNOWN
        }
    }
}
