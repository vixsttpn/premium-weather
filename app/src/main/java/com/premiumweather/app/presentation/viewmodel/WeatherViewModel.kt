package com.premiumweather.app.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.premiumweather.app.domain.mapper.ErrorMapper
import com.premiumweather.app.domain.model.*
import com.premiumweather.app.domain.repository.FavoritesRepository
import com.premiumweather.app.domain.repository.SettingsRepository
import com.premiumweather.app.domain.repository.WeatherRepository
import com.premiumweather.app.domain.provider.GeocodingProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

data class WeatherUiState(
    val isLoading: Boolean = true,
    val snapshot: WeatherSnapshot? = null,
    val isOffline: Boolean = false,
    val error: String? = null,
    val locationPermissionDenied: Boolean = false,
    val isRefreshing: Boolean = false
)

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val favoritesRepository: FavoritesRepository,
    private val settingsRepository: SettingsRepository,
    private val geocodingProvider: GeocodingProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    val settings = settingsRepository.observeSettings().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.premiumweather.app.domain.repository.AppSettings())

    init {
        viewModelScope.launch {
            val cached = weatherRepository.getCached()
            if (cached != null) {
                _uiState.value = WeatherUiState(isLoading = false, snapshot = cached, isOffline = cached.freshness != CacheFreshness.FRESH)
            }
            // try default favorite
            val def = favoritesRepository.getDefault()
            if (def != null) {
                refreshWithLocation(LocationModel(def.name, def.country, def.latitude, def.longitude, def.timezone))
            }
        }
    }

    fun refreshCurrentLocation(context: Context) {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) {
            _uiState.update { it.copy(locationPermissionDenied = true, error = "Location permission required") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            try {
                val fused = LocationServices.getFusedLocationProviderClient(context)
                val loc: Location? = fused.lastLocation.await()
                if (loc == null) {
                    _uiState.update { it.copy(isRefreshing = false, error = "Location unavailable, try city search") }
                    return@launch
                }
                val reverse = geocodingProvider.reverse(loc.latitude, loc.longitude)
                val locModel = LocationModel(
                    name = reverse?.name ?: "Current location",
                    country = reverse?.country,
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    timezone = reverse?.timezone
                )
                refreshWithLocation(locModel)
            } catch (e: Exception) {
                _uiState.update { it.copy(isRefreshing = false, error = ErrorMapper.message(ErrorMapper.map(e)), isOffline = true) }
            }
        }
    }

    fun refreshWithLocation(location: LocationModel) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            val result = weatherRepository.refresh(location, force = true)
            result.onSuccess { snap ->
                _uiState.update { it.copy(isLoading = false, snapshot = snap, isRefreshing = false, isOffline = false, error = null) }
            }.onFailure { err ->
                val cached = weatherRepository.getCached()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        snapshot = cached ?: it.snapshot,
                        isRefreshing = false,
                        isOffline = true,
                        error = ErrorMapper.message(ErrorMapper.map(err))
                    )
                }
            }
        }
    }

    fun pullToRefresh() {
        val currentLoc = _uiState.value.snapshot?.location ?: return
        refreshWithLocation(currentLoc)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, locationPermissionDenied = false) }
    }
}
