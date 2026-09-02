package com.premiumweather.app.presentation.util

import androidx.compose.ui.graphics.Color
import com.premiumweather.app.domain.model.BackgroundType
import com.premiumweather.app.domain.model.WeatherVisualState

object WeatherBackgroundResolver {
    fun gradient(state: WeatherVisualState): List<Color> {
        return when(state.backgroundType) {
            BackgroundType.CLEAR_DAY -> listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
            BackgroundType.CLEAR_NIGHT -> listOf(Color(0xFF0f0c29), Color(0xFF302b63), Color(0xFF24243e))
            BackgroundType.PARTLY_CLOUDY -> listOf(Color(0xFF8e9eab), Color(0xFFeef2f3))
            BackgroundType.CLOUDY -> listOf(Color(0xFF757F9A), Color(0xFFD7DDE8))
            BackgroundType.RAIN -> listOf(Color(0xFF616161), Color(0xFF9bc5c3))
            BackgroundType.HEAVY_RAIN -> listOf(Color(0xFF232526), Color(0xFF414345))
            BackgroundType.THUNDERSTORM -> listOf(Color(0xFF0F0C29), Color(0xFF302B63), Color(0xFF24243E))
            BackgroundType.SNOW -> listOf(Color(0xFFe6dada), Color(0xFF274046))
            BackgroundType.FOG -> listOf(Color(0xFF757F9A), Color(0xFFD7DDE8))
            BackgroundType.SUNRISE -> listOf(Color(0xFFff7e5f), Color(0xFFfeb47b))
            BackgroundType.SUNSET -> listOf(Color(0xFFff512f), Color(0xFFdd2476))
        }
    }
}
