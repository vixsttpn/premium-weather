package com.premiumweather.app.domain.model

import java.time.Instant
import java.time.LocalDate

data class WeatherSnapshot(
    val location: LocationModel?,
    val timezone: String?,
    val temperature: Double?,
    val apparentTemperature: Double?,
    val condition: WeatherCondition?,
    val wmoCode: Int?,
    val isDay: Boolean?,
    val humidity: Int?,
    val precipitationProbability: Int?,
    val precipitation: Double?,
    val rain: Double?,
    val snowfall: Double?,
    val windSpeed: Double?,
    val windDirection: Double?,
    val windGusts: Double?,
    val pressure: Double?,
    val visibility: Double?,
    val cloudCover: Int?,
    val uvIndex: Double?,
    val sunrise: Instant?,
    val sunset: Instant?,
    val high: Double?,
    val low: Double?,
    val fetchedAt: Instant,
    val freshness: CacheFreshness = CacheFreshness.FRESH,
    val hourly: List<HourlyForecast> = emptyList(),
    val daily: List<DailyForecast> = emptyList()
)

data class HourlyForecast(
    val time: Instant,
    val temperature: Double?,
    val condition: WeatherCondition?,
    val wmoCode: Int?,
    val isDay: Boolean?,
    val precipitationProbability: Int?,
    val windSpeed: Double?
)

data class DailyForecast(
    val date: LocalDate,
    val high: Double?,
    val low: Double?,
    val condition: WeatherCondition?,
    val wmoCode: Int?,
    val sunrise: Instant?,
    val sunset: Instant?,
    val precipitationProbability: Int?,
    val precipitation: Double?
)
