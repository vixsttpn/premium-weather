package com.premiumweather.app.domain.model

data class LocationModel(
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?
)

data class GeocodingResult(
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val admin1: String? = null
)
