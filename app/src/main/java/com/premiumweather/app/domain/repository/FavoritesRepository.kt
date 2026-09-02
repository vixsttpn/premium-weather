package com.premiumweather.app.domain.repository

import com.premiumweather.app.domain.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoriteLocation>>
    suspend fun add(location: FavoriteLocation)
    suspend fun remove(id: Long)
    suspend fun setDefault(id: Long)
    suspend fun getDefault(): FavoriteLocation?
    suspend fun getAll(): List<FavoriteLocation>
}
