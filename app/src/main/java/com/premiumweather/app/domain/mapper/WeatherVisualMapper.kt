package com.premiumweather.app.domain.mapper

import com.premiumweather.app.domain.model.*

object WeatherVisualMapper {
    fun map(condition: WeatherCondition?, isSunrise: Boolean = false, isSunset: Boolean = false): WeatherVisualState {
        if (isSunrise) return WeatherVisualState(
            backgroundType = BackgroundType.SUNRISE,
            particleType = ParticleType.NONE,
            particleDensity = 0f,
            cloudDensity = 0.2f,
            animationSpeed = 0.5f,
            overlayOpacity = 0.15f,
            textMode = TextMode.LIGHT,
            showSun = true,
            showMoon = false,
            lightningEnabled = false
        )
        if (isSunset) return WeatherVisualState(
            backgroundType = BackgroundType.SUNSET,
            particleType = ParticleType.NONE,
            particleDensity = 0f,
            cloudDensity = 0.25f,
            animationSpeed = 0.5f,
            overlayOpacity = 0.2f,
            textMode = TextMode.LIGHT,
            showSun = true,
            showMoon = false,
            lightningEnabled = false
        )
        return when(condition) {
            WeatherCondition.CLEAR_DAY -> WeatherVisualState(
                BackgroundType.CLEAR_DAY, particleType = ParticleType.NONE, particleDensity = 0f,
                cloudDensity = 0f, animationSpeed = 0.3f, overlayOpacity = 0f,
                textMode = TextMode.DARK, showSun = true, showMoon = false, lightningEnabled = false
            )
            WeatherCondition.CLEAR_NIGHT -> WeatherVisualState(
                BackgroundType.CLEAR_NIGHT, particleType = ParticleType.STARS, particleDensity = 0.6f,
                cloudDensity = 0f, animationSpeed = 0.2f, overlayOpacity = 0.15f,
                textMode = TextMode.LIGHT, showSun = false, showMoon = true, lightningEnabled = false
            )
            WeatherCondition.PARTLY_CLOUDY_DAY, WeatherCondition.PARTLY_CLOUDY_NIGHT -> WeatherVisualState(
                BackgroundType.PARTLY_CLOUDY, particleType = ParticleType.CLOUDS, particleDensity = 0.4f,
                cloudDensity = 0.5f, animationSpeed = 0.6f, overlayOpacity = 0.08f,
                textMode = TextMode.DARK, showSun = condition == WeatherCondition.PARTLY_CLOUDY_DAY,
                showMoon = condition == WeatherCondition.PARTLY_CLOUDY_NIGHT, lightningEnabled = false
            )
            WeatherCondition.CLOUDY -> WeatherVisualState(
                BackgroundType.CLOUDY, particleType = ParticleType.CLOUDS, particleDensity = 0.5f,
                cloudDensity = 0.8f, animationSpeed = 0.5f, overlayOpacity = 0.1f,
                textMode = TextMode.DARK, showSun = false, showMoon = false, lightningEnabled = false
            )
            WeatherCondition.FOG -> WeatherVisualState(
                BackgroundType.FOG, particleType = ParticleType.FOG, particleDensity = 0.7f,
                cloudDensity = 0.9f, animationSpeed = 0.3f, overlayOpacity = 0.25f,
                textMode = TextMode.DARK, showSun = false, showMoon = false, lightningEnabled = false
            )
            WeatherCondition.DRIZZLE, WeatherCondition.RAIN -> WeatherVisualState(
                BackgroundType.RAIN, particleType = ParticleType.RAIN, particleDensity = 0.5f,
                cloudDensity = 0.7f, animationSpeed = 0.8f, overlayOpacity = 0.12f,
                textMode = TextMode.DARK, showSun = false, showMoon = false, lightningEnabled = false
            )
            WeatherCondition.HEAVY_RAIN, WeatherCondition.FREEZING_RAIN, WeatherCondition.SLEET -> WeatherVisualState(
                BackgroundType.HEAVY_RAIN, particleType = ParticleType.HEAVY_RAIN, particleDensity = 0.8f,
                cloudDensity = 0.9f, animationSpeed = 1f, overlayOpacity = 0.18f,
                textMode = TextMode.LIGHT, showSun = false, showMoon = false, lightningEnabled = false
            )
            WeatherCondition.THUNDERSTORM, WeatherCondition.THUNDERSTORM_RAIN -> WeatherVisualState(
                BackgroundType.THUNDERSTORM, particleType = ParticleType.HEAVY_RAIN, particleDensity = 0.9f,
                cloudDensity = 1f, animationSpeed = 1f, overlayOpacity = 0.3f,
                textMode = TextMode.LIGHT, showSun = false, showMoon = false, lightningEnabled = true
            )
            WeatherCondition.SNOW, WeatherCondition.HEAVY_SNOW -> WeatherVisualState(
                BackgroundType.SNOW, particleType = ParticleType.SNOW, particleDensity = 0.7f,
                cloudDensity = 0.6f, animationSpeed = 0.6f, overlayOpacity = 0.08f,
                textMode = TextMode.DARK, showSun = false, showMoon = false, lightningEnabled = false
            )
            else -> WeatherVisualState(
                BackgroundType.CLOUDY, particleType = ParticleType.NONE, particleDensity = 0f,
                cloudDensity = 0.5f, animationSpeed = 0.4f, overlayOpacity = 0.1f,
                textMode = TextMode.DARK, showSun = false, showMoon = false, lightningEnabled = false
            )
        }
    }
}
