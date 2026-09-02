package com.premiumweather.app.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val id: Int = 1,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val locationName: String?,
    val country: String?,
    val json: String,
    val fetchedAtEpoch: Long,
    val high: Double?,
    val low: Double?,
    val temperature: Double?,
    val wmoCode: Int?
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String?,
    val latitude: Double,
    val longitude: Double,
    val timezone: String?,
    val isDefault: Boolean = false
)
