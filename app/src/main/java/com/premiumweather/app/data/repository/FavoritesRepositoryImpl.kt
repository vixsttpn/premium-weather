package com.premiumweather.app.data.repository

import com.premiumweather.app.data.local.room.AppDatabase
import com.premiumweather.app.data.local.room.FavoriteEntity
import com.premiumweather.app.domain.model.FavoriteLocation
import com.premiumweather.app.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepositoryImpl(
    private val db: AppDatabase
) : FavoritesRepository {

    private val _flow = MutableStateFlow<List<FavoriteLocation>>(emptyList())

    override fun observeFavorites(): Flow<List<FavoriteLocation>> = _flow.asStateFlow()

    override suspend fun getAll(): List<FavoriteLocation> {
        val list = db.favoriteDao().getAll().map { it.toDomain() }
        _flow.value = list
        return list
    }

    override suspend fun add(location: FavoriteLocation) {
        db.favoriteDao().insert(
            FavoriteEntity(
                name = location.name,
                country = location.country,
                latitude = location.latitude,
                longitude = location.longitude,
                timezone = location.timezone,
                isDefault = location.isDefault
            )
        )
        getAll()
    }

    override suspend fun remove(id: Long) {
        db.favoriteDao().delete(id)
        getAll()
    }

    override suspend fun setDefault(id: Long) {
        db.favoriteDao().setDefault(id)
        getAll()
    }

    override suspend fun getDefault(): FavoriteLocation? {
        return db.favoriteDao().getDefault()?.toDomain()
    }

    private fun FavoriteEntity.toDomain() = FavoriteLocation(
        id = id,
        name = name,
        country = country,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        isDefault = isDefault
    )
}
