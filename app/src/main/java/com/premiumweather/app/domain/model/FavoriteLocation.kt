package com.premiumweather.app.domain.model

data class FavoriteLocation(
    val id: Long = 0,
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val isDefault: Boolean = false
)
