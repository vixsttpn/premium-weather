package com.premiumweather.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.premiumweather.app.domain.model.GeocodingResult
import com.premiumweather.app.domain.provider.GeocodingProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val geocodingProvider: GeocodingProvider
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _results = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val results: StateFlow<List<GeocodingResult>> = _results.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        viewModelScope.launch {
            _query.debounce(400).distinctUntilChanged().collect { q ->
                if (q.length < 2) {
                    _results.value = emptyList()
                    return@collect
                }
                _isLoading.value = true
                try {
                    val list = geocodingProvider.search(q)
                    _results.value = list
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun onQueryChange(q: String) {
        _query.value = q
    }
}
