package com.premiumweather.app.data.repository

import com.premiumweather.app.data.local.datastore.SettingsDataStore
import com.premiumweather.app.domain.repository.AppSettings
import com.premiumweather.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class SettingsRepositoryImpl(
    private val dataStore: SettingsDataStore
) : SettingsRepository {
    override fun observeSettings(): Flow<AppSettings> = dataStore.settingsFlow
    override suspend fun update(transform: (AppSettings) -> AppSettings) = dataStore.update(transform)
}
