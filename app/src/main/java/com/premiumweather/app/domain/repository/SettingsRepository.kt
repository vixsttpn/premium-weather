package com.premiumweather.app.domain.repository

import com.premiumweather.app.domain.mapper.*
import com.premiumweather.app.domain.model.AnimationLevel
import kotlinx.coroutines.flow.Flow

data class AppSettings(
    val tempUnit: TempUnit = TempUnit.CELSIUS,
    val speedUnit: SpeedUnit = SpeedUnit.KMH,
    val precipUnit: PrecipUnit = PrecipUnit.MM,
    val pressureUnit: PressureUnit = PressureUnit.HPA,
    val distanceUnit: DistanceUnit = DistanceUnit.KM,
    val theme: String = "system", // system, light, dark, weather
    val animationLevel: AnimationLevel = AnimationLevel.BALANCED,
    val language: String = "system",
    val highContrast: Boolean = false,
    val reducedMotion: Boolean = false,
    val defaultLocationId: Long? = null
)

interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun update(transform: (AppSettings) -> AppSettings)
}
