package com.premiumweather.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premiumweather.app.domain.mapper.*
import com.premiumweather.app.domain.model.AnimationLevel
import com.premiumweather.app.domain.repository.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settingsFlow = settingsRepository.observeSettings()

    fun setTempUnit(unit: TempUnit) = update { it.copy(tempUnit = unit) }
    fun setSpeedUnit(unit: SpeedUnit) = update { it.copy(speedUnit = unit) }
    fun setPrecipUnit(unit: PrecipUnit) = update { it.copy(precipUnit = unit) }
    fun setPressureUnit(unit: PressureUnit) = update { it.copy(pressureUnit = unit) }
    fun setDistanceUnit(unit: DistanceUnit) = update { it.copy(distanceUnit = unit) }
    fun setTheme(theme: String) = update { it.copy(theme = theme) }
    fun setAnimation(level: AnimationLevel) = update { it.copy(animationLevel = level) }
    fun setLanguage(lang: String) = update { it.copy(language = lang) }
    fun setHighContrast(v: Boolean) = update { it.copy(highContrast = v) }
    fun setReducedMotion(v: Boolean) = update { it.copy(reducedMotion = v) }

    private fun update(transform: (com.premiumweather.app.domain.repository.AppSettings) -> com.premiumweather.app.domain.repository.AppSettings) {
        viewModelScope.launch {
            settingsRepository.update(transform)
        }
    }
}
