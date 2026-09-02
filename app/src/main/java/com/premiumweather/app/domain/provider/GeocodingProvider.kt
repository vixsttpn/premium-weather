package com.premiumweather.app.domain.provider

import com.premiumweather.app.domain.model.GeocodingResult

interface GeocodingProvider {
    suspend fun search(query: String): List<GeocodingResult>
    suspend fun reverse(lat: Double, lon: Double): GeocodingResult?
}
