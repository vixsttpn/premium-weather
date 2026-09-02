package com.premiumweather.app

import com.premiumweather.app.domain.mapper.WeatherCodeMapper
import com.premiumweather.app.domain.model.WeatherCondition
import org.junit.Assert.*
import org.junit.Test

class WeatherCodeMapperTest {
    @Test fun testClearDay() { assertEquals(WeatherCondition.CLEAR_DAY, WeatherCodeMapper.map(0, true)) }
    @Test fun testClearNight() { assertEquals(WeatherCondition.CLEAR_NIGHT, WeatherCodeMapper.map(0, false)) }
    @Test fun testCloudy() { assertEquals(WeatherCondition.CLOUDY, WeatherCodeMapper.map(3, true)) }
    @Test fun testFog() { assertEquals(WeatherCondition.FOG, WeatherCodeMapper.map(45, true)) }
    @Test fun testRain() { assertEquals(WeatherCondition.RAIN, WeatherCodeMapper.map(61, true)) }
    @Test fun testHeavyRain() { assertEquals(WeatherCondition.HEAVY_RAIN, WeatherCodeMapper.map(65, true)) }
    @Test fun testSnow() { assertEquals(WeatherCondition.SNOW, WeatherCodeMapper.map(71, true)) }
    @Test fun testThunder() { assertEquals(WeatherCondition.THUNDERSTORM, WeatherCodeMapper.map(95, true)) }
    @Test fun testThunderRain() { assertEquals(WeatherCondition.THUNDERSTORM_RAIN, WeatherCodeMapper.map(99, true)) }
    @Test fun testUnknown() { assertEquals(WeatherCondition.UNKNOWN, WeatherCodeMapper.map(999, true)) }
    @Test fun testNull() { assertEquals(WeatherCondition.UNKNOWN, WeatherCodeMapper.map(null, true)) }
}
