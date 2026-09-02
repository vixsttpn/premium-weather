package com.premiumweather.app.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.premiumweather.app.domain.mapper.*
import com.premiumweather.app.domain.model.AnimationLevel
import com.premiumweather.app.domain.repository.AppSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val TEMP = stringPreferencesKey("temp_unit")
        val SPEED = stringPreferencesKey("speed_unit")
        val PRECIP = stringPreferencesKey("precip_unit")
        val PRESSURE = stringPreferencesKey("pressure_unit")
        val DISTANCE = stringPreferencesKey("distance_unit")
        val THEME = stringPreferencesKey("theme")
        val ANIM = stringPreferencesKey("animation_level")
        val LANG = stringPreferencesKey("language")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val DEFAULT_LOC_ID = longPreferencesKey("default_loc_id")
    }

    val settingsFlow: Flow<AppSettings> = context.dataStore.data.map { prefs ->
        AppSettings(
            tempUnit = runCatching { TempUnit.valueOf(prefs[Keys.TEMP] ?: "CELSIUS") }.getOrDefault(TempUnit.CELSIUS),
            speedUnit = runCatching { SpeedUnit.valueOf(prefs[Keys.SPEED] ?: "KMH") }.getOrDefault(SpeedUnit.KMH),
            precipUnit = runCatching { PrecipUnit.valueOf(prefs[Keys.PRECIP] ?: "MM") }.getOrDefault(PrecipUnit.MM),
            pressureUnit = runCatching { PressureUnit.valueOf(prefs[Keys.PRESSURE] ?: "HPA") }.getOrDefault(PressureUnit.HPA),
            distanceUnit = runCatching { DistanceUnit.valueOf(prefs[Keys.DISTANCE] ?: "KM") }.getOrDefault(DistanceUnit.KM),
            theme = prefs[Keys.THEME] ?: "system",
            animationLevel = runCatching { AnimationLevel.valueOf(prefs[Keys.ANIM] ?: "BALANCED") }.getOrDefault(AnimationLevel.BALANCED),
            language = prefs[Keys.LANG] ?: "system",
            highContrast = prefs[Keys.HIGH_CONTRAST] ?: false,
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
            defaultLocationId = prefs[Keys.DEFAULT_LOC_ID]
        )
    }

    suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.dataStore.edit { prefs ->
            val current = AppSettings(
                tempUnit = runCatching { TempUnit.valueOf(prefs[Keys.TEMP] ?: "CELSIUS") }.getOrDefault(TempUnit.CELSIUS),
                speedUnit = runCatching { SpeedUnit.valueOf(prefs[Keys.SPEED] ?: "KMH") }.getOrDefault(SpeedUnit.KMH),
                precipUnit = runCatching { PrecipUnit.valueOf(prefs[Keys.PRECIP] ?: "MM") }.getOrDefault(PrecipUnit.MM),
                pressureUnit = runCatching { PressureUnit.valueOf(prefs[Keys.PRESSURE] ?: "HPA") }.getOrDefault(PressureUnit.HPA),
                distanceUnit = runCatching { DistanceUnit.valueOf(prefs[Keys.DISTANCE] ?: "KM") }.getOrDefault(DistanceUnit.KM),
                theme = prefs[Keys.THEME] ?: "system",
                animationLevel = runCatching { AnimationLevel.valueOf(prefs[Keys.ANIM] ?: "BALANCED") }.getOrDefault(AnimationLevel.BALANCED),
                language = prefs[Keys.LANG] ?: "system",
                highContrast = prefs[Keys.HIGH_CONTRAST] ?: false,
                reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
                defaultLocationId = prefs[Keys.DEFAULT_LOC_ID]
            )
            val updated = transform(current)
            prefs[Keys.TEMP] = updated.tempUnit.name
            prefs[Keys.SPEED] = updated.speedUnit.name
            prefs[Keys.PRECIP] = updated.precipUnit.name
            prefs[Keys.PRESSURE] = updated.pressureUnit.name
            prefs[Keys.DISTANCE] = updated.distanceUnit.name
            prefs[Keys.THEME] = updated.theme
            prefs[Keys.ANIM] = updated.animationLevel.name
            prefs[Keys.LANG] = updated.language
            prefs[Keys.HIGH_CONTRAST] = updated.highContrast
            prefs[Keys.REDUCED_MOTION] = updated.reducedMotion
            if (updated.defaultLocationId != null) prefs[Keys.DEFAULT_LOC_ID] = updated.defaultLocationId else prefs.remove(Keys.DEFAULT_LOC_ID)
        }
    }
}
