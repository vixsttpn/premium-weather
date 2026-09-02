package com.premiumweather.app

import com.premiumweather.app.domain.mapper.*
import org.junit.Assert.*
import org.junit.Test

class UnitConverterTest {
    @Test fun tempConversion() {
        assertEquals(32.0, UnitConverter.temp(0.0, TempUnit.FAHRENHEIT)!!, 0.01)
        assertEquals(0.0, UnitConverter.temp(0.0, TempUnit.CELSIUS)!!, 0.01)
    }
    @Test fun speedConversion() {
        assertEquals(10.0, UnitConverter.speed(10.0, SpeedUnit.KMH)!!, 0.01)
        assertEquals(2.777, UnitConverter.speed(10.0, SpeedUnit.MS)!!, 0.01)
    }
    @Test fun nullHandling() {
        assertNull(UnitConverter.temp(null, TempUnit.CELSIUS))
        assertEquals("--", UnitConverter.formatTemp(null, TempUnit.CELSIUS))
    }
}
