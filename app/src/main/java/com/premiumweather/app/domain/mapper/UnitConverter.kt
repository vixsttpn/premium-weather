package com.premiumweather.app.domain.mapper

import kotlin.math.roundToInt

enum class TempUnit { CELSIUS, FAHRENHEIT }
enum class SpeedUnit { KMH, MS, MPH }
enum class PrecipUnit { MM, INCH }
enum class PressureUnit { HPA, INHG }
enum class DistanceUnit { KM, MILES }

object UnitConverter {
    fun temp(celsius: Double?, unit: TempUnit): Double? {
        if (celsius == null) return null
        return when(unit) {
            TempUnit.CELSIUS -> celsius
            TempUnit.FAHRENHEIT -> celsius * 9/5 + 32
        }
    }
    fun speed(kmh: Double?, unit: SpeedUnit): Double? {
        if (kmh == null) return null
        return when(unit) {
            SpeedUnit.KMH -> kmh
            SpeedUnit.MS -> kmh / 3.6
            SpeedUnit.MPH -> kmh * 0.621371 / 1.0 // kmh to mph approx 0.621
        }
    }
    fun precipitation(mm: Double?, unit: PrecipUnit): Double? {
        if (mm == null) return null
        return when(unit) {
            PrecipUnit.MM -> mm
            PrecipUnit.INCH -> mm / 25.4
        }
    }
    fun pressure(hpa: Double?, unit: PressureUnit): Double? {
        if (hpa == null) return null
        return when(unit) {
            PressureUnit.HPA -> hpa
            PressureUnit.INHG -> hpa * 0.02953
        }
    }
    fun distance(km: Double?, unit: DistanceUnit): Double? {
        if (km == null) return null
        return when(unit) {
            DistanceUnit.KM -> km
            DistanceUnit.MILES -> km * 0.621371
        }
    }
    fun formatTemp(value: Double?, unit: TempUnit): String {
        if (value == null) return "--"
        val converted = temp(value, unit) ?: return "--"
        return "${converted.roundToInt()}°"
    }
}
