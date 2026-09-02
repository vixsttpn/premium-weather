package com.premiumweather.app.data.repository

import com.premiumweather.app.data.local.room.AppDatabase
import com.premiumweather.app.data.local.room.WeatherEntity
import com.premiumweather.app.domain.mapper.CacheFreshness
import com.premiumweather.app.domain.model.LocationModel
import com.premiumweather.app.domain.model.WeatherSnapshot
import com.premiumweather.app.domain.provider.WeatherProvider
import com.premiumweather.app.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.Instant

class WeatherRepositoryImpl(
    private val provider: WeatherProvider,
    private val db: AppDatabase,
    private val json: Json
) : WeatherRepository {

    private val flow = MutableStateFlow<WeatherSnapshot?>(null)
    private val mutex = Mutex()
    private var lastRequestTime = 0L
    private var lastLocation: LocationModel? = null
    private val MIN_INTERVAL_MS = 5 * 60 * 1000L // 5 min

    override fun observeWeather(): Flow<WeatherSnapshot?> = flow

    override suspend fun getCached(): WeatherSnapshot? {
        val entity = db.weatherDao().get() ?: return null
        return try {
            val snapshot = json.decodeFromString<WeatherSnapshotSerializer>(entity.json).toDomain()
            val freshness = computeFreshness(snapshot.fetchedAt)
            snapshot.copy(freshness = freshness)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun save(snapshot: WeatherSnapshot) {
        val entity = WeatherEntity(
            id = 1,
            latitude = snapshot.location?.latitude ?: 0.0,
            longitude = snapshot.location?.longitude ?: 0.0,
            timezone = snapshot.timezone,
            locationName = snapshot.location?.name,
            country = snapshot.location?.country,
            json = json.encodeToString(WeatherSnapshotSerializer.fromDomain(snapshot)),
            fetchedAtEpoch = snapshot.fetchedAt.epochSecond,
            high = snapshot.high,
            low = snapshot.low,
            temperature = snapshot.temperature,
            wmoCode = snapshot.wmoCode
        )
        db.weatherDao().insert(entity)
        flow.value = snapshot
    }

    override suspend fun refresh(location: LocationModel, force: Boolean): Result<WeatherSnapshot> {
        return mutex.withLock {
            val now = System.currentTimeMillis()
            val sameLocation = lastLocation?.let { it.latitude == location.latitude && it.longitude == location.longitude } ?: false
            if (!force && sameLocation && now - lastRequestTime < MIN_INTERVAL_MS) {
                val cached = getCached()
                if (cached != null) return@withLock Result.success(cached)
            }

            // deduplication: if same location recently fetched, return cached
            try {
                val fresh = provider.fetchWeather(location)
                val withFreshness = fresh.copy(freshness = CacheFreshness.FRESH)
                save(withFreshness)
                lastRequestTime = now
                lastLocation = location
                Result.success(withFreshness)
            } catch (e: Exception) {
                val cached = getCached()
                if (cached != null) {
                    val freshness = computeFreshness(cached.fetchedAt)
                    val staleCopy = cached.copy(freshness = freshness)
                    flow.value = staleCopy
                    Result.success(staleCopy)
                } else {
                    Result.failure(e)
                }
            }
        }
    }

    private fun computeFreshness(fetchedAt: Instant): CacheFreshness {
        val age = Duration.between(fetchedAt, Instant.now())
        return when {
            age.toMinutes() < 30 -> CacheFreshness.FRESH
            age.toHours() < 6 -> CacheFreshness.STALE
            else -> CacheFreshness.VERY_STALE
        }
    }
}

// Serializable surrogate for WeatherSnapshot to store as JSON (simple manual mapping using kotlinx datetime iso)
@kotlinx.serialization.Serializable
data class WeatherSnapshotSerializer(
    val locationName: String?,
    val country: String?,
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String?,
    val temperature: Double?,
    val apparentTemperature: Double?,
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
    val sunriseEpoch: Long?,
    val sunsetEpoch: Long?,
    val high: Double?,
    val low: Double?,
    val fetchedAtEpoch: Long
) {
    fun toDomain(): com.premiumweather.app.domain.model.WeatherSnapshot {
        val loc = if (latitude != null && longitude != null) {
            com.premiumweather.app.domain.model.LocationModel(
                name = locationName ?: "Unknown",
                country = country,
                latitude = latitude,
                longitude = longitude,
                timezone = timezone
            )
        } else null
        return com.premiumweather.app.domain.model.WeatherSnapshot(
            location = loc,
            timezone = timezone,
            temperature = temperature,
            apparentTemperature = apparentTemperature,
            condition = com.premiumweather.app.domain.mapper.WeatherCodeMapper.map(wmoCode, isDay),
            wmoCode = wmoCode,
            isDay = isDay,
            humidity = humidity,
            precipitationProbability = precipitationProbability,
            precipitation = precipitation,
            rain = rain,
            snowfall = snowfall,
            windSpeed = windSpeed,
            windDirection = windDirection,
            windGusts = windGusts,
            pressure = pressure,
            visibility = visibility,
            cloudCover = cloudCover,
            uvIndex = uvIndex,
            sunrise = sunriseEpoch?.let { Instant.ofEpochSecond(it) },
            sunset = sunsetEpoch?.let { Instant.ofEpochSecond(it) },
            high = high,
            low = low,
            fetchedAt = Instant.ofEpochSecond(fetchedAtEpoch),
            hourly = emptyList(),
            daily = emptyList()
        )
    }

    companion object {
        fun fromDomain(s: com.premiumweather.app.domain.model.WeatherSnapshot): WeatherSnapshotSerializer {
            return WeatherSnapshotSerializer(
                locationName = s.location?.name,
                country = s.location?.country,
                latitude = s.location?.latitude,
                longitude = s.location?.longitude,
                timezone = s.timezone,
                temperature = s.temperature,
                apparentTemperature = s.apparentTemperature,
                wmoCode = s.wmoCode,
                isDay = s.isDay,
                humidity = s.humidity,
                precipitationProbability = s.precipitationProbability,
                precipitation = s.precipitation,
                rain = s.rain,
                snowfall = s.snowfall,
                windSpeed = s.windSpeed,
                windDirection = s.windDirection,
                windGusts = s.windGusts,
                pressure = s.pressure,
                visibility = s.visibility,
                cloudCover = s.cloudCover,
                uvIndex = s.uvIndex,
                sunriseEpoch = s.sunrise?.epochSecond,
                sunsetEpoch = s.sunset?.epochSecond,
                high = s.high,
                low = s.low,
                fetchedAtEpoch = s.fetchedAt.epochSecond
            )
        }
    }
}
