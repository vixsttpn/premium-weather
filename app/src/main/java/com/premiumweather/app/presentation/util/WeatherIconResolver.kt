package com.premiumweather.app.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.premiumweather.app.domain.model.WeatherCondition

object WeatherIconResolver {
    fun resolve(condition: WeatherCondition?): ImageVector {
        return when(condition) {
            WeatherCondition.CLEAR_DAY -> Icons.Filled.WbSunny
            WeatherCondition.CLEAR_NIGHT -> Icons.Filled.NightsStay
            WeatherCondition.PARTLY_CLOUDY_DAY -> Icons.Filled.WbCloudy
            WeatherCondition.PARTLY_CLOUDY_NIGHT -> Icons.Filled.Cloud
            WeatherCondition.CLOUDY -> Icons.Filled.Cloud
            WeatherCondition.FOG -> Icons.Filled.Cloud
            WeatherCondition.DRIZZLE -> Icons.Filled.Grain
            WeatherCondition.RAIN -> Icons.Filled.WaterDrop
            WeatherCondition.HEAVY_RAIN -> Icons.Filled.Thunderstorm
            WeatherCondition.FREEZING_RAIN -> Icons.Filled.AcUnit
            WeatherCondition.SNOW -> Icons.Filled.AcUnit
            WeatherCondition.HEAVY_SNOW -> Icons.Filled.AcUnit
            WeatherCondition.SLEET -> Icons.Filled.AcUnit
            WeatherCondition.THUNDERSTORM -> Icons.Filled.Thunderstorm
            WeatherCondition.THUNDERSTORM_RAIN -> Icons.Filled.Thunderstorm
            WeatherCondition.WINDY -> Icons.Filled.Air
            else -> Icons.Filled.HelpOutline
        }
    }

    fun description(condition: WeatherCondition?): String {
        return when(condition) {
            WeatherCondition.CLEAR_DAY -> "Clear day"
            WeatherCondition.CLEAR_NIGHT -> "Clear night"
            WeatherCondition.PARTLY_CLOUDY_DAY -> "Partly cloudy"
            WeatherCondition.PARTLY_CLOUDY_NIGHT -> "Partly cloudy night"
            WeatherCondition.CLOUDY -> "Cloudy"
            WeatherCondition.FOG -> "Fog"
            WeatherCondition.DRIZZLE -> "Drizzle"
            WeatherCondition.RAIN -> "Rain"
            WeatherCondition.HEAVY_RAIN -> "Heavy rain"
            WeatherCondition.FREEZING_RAIN -> "Freezing rain"
            WeatherCondition.SNOW -> "Snow"
            WeatherCondition.HEAVY_SNOW -> "Heavy snow"
            WeatherCondition.SLEET -> "Sleet"
            WeatherCondition.THUNDERSTORM -> "Thunderstorm"
            WeatherCondition.THUNDERSTORM_RAIN -> "Thunderstorm with rain"
            WeatherCondition.WINDY -> "Windy"
            else -> "Unknown"
        }
    }
}
