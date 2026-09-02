package com.premiumweather.app.data.remote.provider

import com.premiumweather.app.data.remote.dto.OpenMeteoGeocodingResponse
import com.premiumweather.app.domain.model.GeocodingResult
import com.premiumweather.app.domain.provider.GeocodingProvider
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class OpenMeteoGeocodingProvider(
    private val client: OkHttpClient,
    private val json: Json
) : GeocodingProvider {

    private val cache = mutableMapOf<String, List<GeocodingResult>>()
    private val cacheTime = mutableMapOf<String, Long>()
    private val CACHE_TTL = 5 * 60 * 1000L

    override suspend fun search(query: String): List<GeocodingResult> {
        val q = query.trim()
        if (q.length < 2) return emptyList()
        val now = System.currentTimeMillis()
        cache[q]?.let { list ->
            if (now - (cacheTime[q] ?: 0) < CACHE_TTL) return list
        }
        val url = "https://geocoding-api.open-meteo.com/v1/search".toHttpUrl().newBuilder()
            .addQueryParameter("name", q)
            .addQueryParameter("count", "8")
            .addQueryParameter("language", "en")
            .addQueryParameter("format", "json")
            .build()
        val req = Request.Builder().url(url).get().build()
        val resp = client.newCall(req).execute()
        if (!resp.isSuccessful) {
            if (resp.code == 429) throw RateLimitedException()
            throw java.io.IOException("Geocoding HTTP ${resp.code}")
        }
        val body = resp.body?.string() ?: return emptyList()
        val dto = json.decodeFromString<OpenMeteoGeocodingResponse>(body)
        val result = dto.results?.map {
            GeocodingResult(
                name = it.name,
                country = it.country,
                latitude = it.latitude,
                longitude = it.longitude,
                timezone = it.timezone,
                admin1 = it.admin1
            )
        } ?: emptyList()
        cache[q] = result
        cacheTime[q] = now
        return result
    }

    override suspend fun reverse(lat: Double, lon: Double): GeocodingResult? {
        // Open-Meteo geocoding has no direct reverse; use search with lat/lon? We'll fake via nearest search API: use reverse endpoint
        val url = "https://geocoding-api.open-meteo.com/v1/reverse".toHttpUrl().newBuilder()
            .addQueryParameter("latitude", lat.toString())
            .addQueryParameter("longitude", lon.toString())
            .addQueryParameter("language", "en")
            .build()
        return try {
            val req = Request.Builder().url(url).get().build()
            val resp = client.newCall(req).execute()
            if (!resp.isSuccessful) return null
            val body = resp.body?.string() ?: return null
            val dto = json.decodeFromString<OpenMeteoGeocodingResponse>(body)
            dto.results?.firstOrNull()?.let {
                GeocodingResult(
                    name = it.name,
                    country = it.country,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    timezone = it.timezone,
                    admin1 = it.admin1
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    class RateLimitedException : java.io.IOException("Rate limited")
}
