package com.premiumweather.app

import com.premiumweather.app.domain.mapper.WeatherVisualMapper
import com.premiumweather.app.domain.model.BackgroundType
import com.premiumweather.app.domain.model.WeatherCondition
import org.junit.Assert.*
import org.junit.Test

class WeatherVisualMapperTest {
    @Test fun clearDayMaps() {
        val state = WeatherVisualMapper.map(WeatherCondition.CLEAR_DAY)
        assertEquals(BackgroundType.CLEAR_DAY, state.backgroundType)
    }
    @Test fun thunderstormLightning() {
        val state = WeatherVisualMapper.map(WeatherCondition.THUNDERSTORM)
        assertTrue(state.lightningEnabled)
    }
    @Test fun sunriseMaps() {
        val state = WeatherVisualMapper.map(WeatherCondition.CLEAR_DAY, isSunrise = true)
        assertEquals(BackgroundType.SUNRISE, state.backgroundType)
    }
}
