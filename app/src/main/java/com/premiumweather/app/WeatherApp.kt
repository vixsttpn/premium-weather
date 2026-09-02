package com.premiumweather.app

import android.app.Application
import androidx.room.Room
import com.premiumweather.app.data.local.datastore.SettingsDataStore
import com.premiumweather.app.data.local.room.AppDatabase
import com.premiumweather.app.data.remote.provider.OpenMeteoGeocodingProvider
import com.premiumweather.app.data.remote.provider.OpenMeteoWeatherProvider
import com.premiumweather.app.data.repository.FavoritesRepositoryImpl
import com.premiumweather.app.data.repository.SettingsRepositoryImpl
import com.premiumweather.app.data.repository.WeatherRepositoryImpl
import com.premiumweather.app.domain.repository.FavoritesRepository
import com.premiumweather.app.domain.repository.SettingsRepository
import com.premiumweather.app.domain.repository.WeatherRepository
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class WeatherApp : Application() {

    val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }
    }

    val okHttp by lazy {
        OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "weather.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val settingsDataStore by lazy { SettingsDataStore(this) }

    val weatherProvider by lazy { OpenMeteoWeatherProvider(okHttp, json) }
    val geocodingProvider by lazy { OpenMeteoGeocodingProvider(okHttp, json) }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(weatherProvider, database, json)
    }
    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(settingsDataStore)
    }
    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepositoryImpl(database)
    }
}
