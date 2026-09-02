package com.premiumweather.app.domain.repository

import com.premiumweather.app.domain.model.LocationModel
import com.premiumweather.app.domain.model.WeatherSnapshot
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<WeatherSnapshot?>
    suspend fun refresh(location: LocationModel, force: Boolean = false): Result<WeatherSnapshot>
    suspend fun getCached(): WeatherSnapshot?
    suspend fun save(snapshot: WeatherSnapshot)
}
