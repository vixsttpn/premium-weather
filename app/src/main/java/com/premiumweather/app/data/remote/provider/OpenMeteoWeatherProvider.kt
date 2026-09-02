package com.premiumweather.app.data.remote.provider

import com.premiumweather.app.data.remote.dto.OpenMeteoForecastResponse
import com.premiumweather.app.domain.mapper.DayNightResolver
import com.premiumweather.app.domain.mapper.WeatherCodeMapper
import com.premiumweather.app.domain.model.*
import com.premiumweather.app.domain.provider.WeatherProvider
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class OpenMeteoWeatherProvider(
    private val client: OkHttpClient,
    private val json: Json
) : WeatherProvider {

    override suspend fun fetchWeather(location: LocationModel): WeatherSnapshot {
        val url = "https://api.open-meteo.com/v1/forecast".toHttpUrl().newBuilder()
            .addQueryParameter("latitude", location.latitude.toString())
            .addQueryParameter("longitude", location.longitude.toString())
            .addQueryParameter("current", "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,rain,showers,snowfall,weather_code,cloud_cover,wind_speed_10m,wind_direction_10m,wind_gusts_10m,precipitation_probability,pressure_msl,visibility,uv_index")
            .addQueryParameter("hourly", "temperature_2m,weather_code,is_day,precipitation_probability,wind_speed_10m")
            .addQueryParameter("daily", "weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max,precipitation_sum")
            .addQueryParameter("timezone", "auto")
            .addQueryParameter("forecast_days", "7")
            .build()

        val request = Request.Builder().url(url).get().header("Accept", "application/json").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOExceptionWithCode(response.code, response.message)
        }
        val body = response.body?.string() ?: throw IllegalStateException("Empty body")
        val dto = json.decodeFromString<OpenMeteoForecastResponse>(body)

        return dto.toDomain(location)
    }

    private fun OpenMeteoForecastResponse.toDomain(requestLoc: LocationModel): WeatherSnapshot {
        val tzId = this.timezone ?: requestLoc.timezone ?: "UTC"
        val zone = runCatching { ZoneId.of(tzId) }.getOrDefault(ZoneId.of("UTC"))

        val current = this.current
        val daily = this.daily

        // Parse sunrise/sunset for today to resolve day/night
        val todaySunrise = daily?.sunrise?.firstOrNull()?.let { parseInstant(it, zone) }
        val todaySunset = daily?.sunset?.firstOrNull()?.let { parseInstant(it, zone) }
        val nowInstant = current?.time?.let { parseInstant(it, zone) } ?: Instant.now()
        val isDayFlag = DayNightResolver.isDayFromFlag(current?.isDay)
        val isDayResolved = isDayFlag ?: DayNightResolver.isDay(nowInstant, todaySunrise, todaySunset)

        val high = daily?.tempMax?.firstOrNull()
        val low = daily?.tempMin?.firstOrNull()

        val condition = WeatherCodeMapper.map(current?.weatherCode, isDayResolved)

        // Hourly parsing - next 24h
        val hourlyList = mutableListOf<HourlyForecast>()
        val hourlyDto = this.hourly
        if (hourlyDto != null) {
            val count = minOf(24, hourlyDto.time.size)
            for (i in 0 until count) {
                val timeStr = hourlyDto.time.getOrNull(i) ?: continue
                val instant = parseInstant(timeStr, zone) ?: continue
                hourlyList.add(
                    HourlyForecast(
                        time = instant,
                        temperature = hourlyDto.temperature.getOrNull(i),
                        wmoCode = hourlyDto.weatherCode.getOrNull(i),
                        isDay = hourlyDto.isDay.getOrNull(i)?.let { it == 1 },
                        condition = WeatherCodeMapper.map(hourlyDto.weatherCode.getOrNull(i), hourlyDto.isDay.getOrNull(i)?.let { it == 1 }),
                        precipitationProbability = hourlyDto.precipitationProbability.getOrNull(i),
                        windSpeed = hourlyDto.windSpeed.getOrNull(i)
                    )
                )
            }
        }

        val dailyList = mutableListOf<DailyForecast>()
        if (daily != null) {
            val days = minOf(7, daily.time.size)
            for (i in 0 until days) {
                val dateStr = daily.time[i]
                val date = runCatching { LocalDate.parse(dateStr) }.getOrNull() ?: continue
                dailyList.add(
                    DailyForecast(
                        date = date,
                        high = daily.tempMax.getOrNull(i),
                        low = daily.tempMin.getOrNull(i),
                        wmoCode = daily.weatherCode.getOrNull(i),
                        condition = WeatherCodeMapper.map(daily.weatherCode.getOrNull(i), true),
                        sunrise = daily.sunrise.getOrNull(i)?.let { parseInstant(it, zone) },
                        sunset = daily.sunset.getOrNull(i)?.let { parseInstant(it, zone) },
                        precipitationProbability = daily.precipProbMax.getOrNull(i),
                        precipitation = daily.precipSum.getOrNull(i)
                    )
                )
            }
        }

        return WeatherSnapshot(
            location = requestLoc,
            timezone = tzId,
            temperature = current?.temperature,
            apparentTemperature = current?.apparentTemperature,
            condition = condition,
            wmoCode = current?.weatherCode,
            isDay = isDayResolved,
            humidity = current?.humidity,
            precipitationProbability = current?.precipitationProbability,
            precipitation = current?.precipitation,
            rain = current?.rain,
            snowfall = current?.snowfall,
            windSpeed = current?.windSpeed,
            windDirection = current?.windDirection,
            windGusts = current?.windGusts,
            pressure = current?.pressure,
            visibility = current?.visibility,
            cloudCover = current?.cloudCover,
            uvIndex = current?.uvIndex,
            sunrise = todaySunrise,
            sunset = todaySunset,
            high = high,
            low = low,
            fetchedAt = Instant.now(),
            hourly = hourlyList,
            daily = dailyList
        )
    }

    private fun parseInstant(str: String, zone: ZoneId): Instant? {
        return try {
            // Open-Meteo returns ISO like 2024-...T... without Z, with timezone auto - parse as LocalDateTime then atZone
            if (str.endsWith("Z") || str.contains("+")) {
                Instant.parse(str)
            } else {
                val ldt = LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ldt.atZone(zone).toInstant()
            }
        } catch (e: Exception) {
            null
        }
    }

    class IOExceptionWithCode(val code: Int, message: String) : java.io.IOException("HTTP $code $message")
}
