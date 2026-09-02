package com.premiumweather.app.domain.model

enum class BackgroundType {
    CLEAR_DAY, CLEAR_NIGHT, PARTLY_CLOUDY, CLOUDY, RAIN, HEAVY_RAIN, THUNDERSTORM, SNOW, FOG, SUNRISE, SUNSET
}

enum class ParticleType {
    NONE, RAIN, HEAVY_RAIN, SNOW, FOG, STARS, CLOUDS
}

enum class AnimationLevel {
    HIGH, BALANCED, LOW, OFF
}

data class WeatherVisualState(
    val backgroundType: BackgroundType,
    val gradientColors: List<Long> = emptyList(), // ARGB longs for serialization-safe
    val particleType: ParticleType,
    val particleDensity: Float,
    val cloudDensity: Float,
    val animationSpeed: Float,
    val overlayOpacity: Float,
    val textMode: TextMode,
    val showSun: Boolean,
    val showMoon: Boolean,
    val lightningEnabled: Boolean
)

enum class TextMode { LIGHT, DARK }
