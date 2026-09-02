package com.premiumweather.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoForecastResponse(
    val latitude: Double,
    val longitude: Double,
    @SerialName("generationtime_ms") val generationTime: Double? = null,
    @SerialName("utc_offset_seconds") val utcOffset: Int? = null,
    val timezone: String? = null,
    @SerialName("timezone_abbreviation") val timezoneAbbr: String? = null,
    val current: CurrentWeatherDto? = null,
    @SerialName("current_units") val currentUnits: Map<String, String>? = null,
    val hourly: HourlyDto? = null,
    @SerialName("hourly_units") val hourlyUnits: Map<String, String>? = null,
    val daily: DailyDto? = null,
    @SerialName("daily_units") val dailyUnits: Map<String, String>? = null
)

@Serializable
data class CurrentWeatherDto(
    val time: String? = null,
    val interval: Int? = null,
    @SerialName("temperature_2m") val temperature: Double? = null,
    @SerialName("apparent_temperature") val apparentTemperature: Double? = null,
    @SerialName("relative_humidity_2m") val humidity: Int? = null,
    @SerialName("is_day") val isDay: Int? = null,
    @SerialName("precipitation") val precipitation: Double? = null,
    val rain: Double? = null,
    val showers: Double? = null,
    val snowfall: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("cloud_cover") val cloudCover: Int? = null,
    @SerialName("wind_speed_10m") val windSpeed: Double? = null,
    @SerialName("wind_direction_10m") val windDirection: Double? = null,
    @SerialName("wind_gusts_10m") val windGusts: Double? = null,
    @SerialName("precipitation_probability") val precipitationProbability: Int? = null,
    @SerialName("pressure_msl") val pressure: Double? = null,
    @SerialName("visibility") val visibility: Double? = null,
    @SerialName("uv_index") val uvIndex: Double? = null
)

@Serializable
data class HourlyDto(
    val time: List<String> = emptyList(),
    @SerialName("temperature_2m") val temperature: List<Double?> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
    @SerialName("is_day") val isDay: List<Int?> = emptyList(),
    @SerialName("precipitation_probability") val precipitationProbability: List<Int?> = emptyList(),
    @SerialName("wind_speed_10m") val windSpeed: List<Double?> = emptyList()
)

@Serializable
data class DailyDto(
    val time: List<String> = emptyList(),
    @SerialName("weather_code") val weatherCode: List<Int?> = emptyList(),
    @SerialName("temperature_2m_max") val tempMax: List<Double?> = emptyList(),
    @SerialName("temperature_2m_min") val tempMin: List<Double?> = emptyList(),
    @SerialName("sunrise") val sunrise: List<String?> = emptyList(),
    @SerialName("sunset") val sunset: List<String?> = emptyList(),
    @SerialName("precipitation_probability_max") val precipProbMax: List<Int?> = emptyList(),
    @SerialName("precipitation_sum") val precipSum: List<Double?> = emptyList()
)

@Serializable
data class OpenMeteoGeocodingResponse(
    val results: List<GeocodingPlaceDto>? = null
)

@Serializable
data class GeocodingPlaceDto(
    val id: Long? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null,
    @SerialName("admin1") val admin1: String? = null,
    val timezone: String? = null
)
