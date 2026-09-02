package com.premiumweather.app.domain.provider

import com.premiumweather.app.domain.model.LocationModel
import com.premiumweather.app.domain.model.WeatherSnapshot

interface WeatherProvider {
    suspend fun fetchWeather(location: LocationModel): WeatherSnapshot
}
