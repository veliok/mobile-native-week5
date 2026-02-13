package com.example.week5.viewmodel

import com.example.week5.data.model.WeatherResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week5.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.week5.BuildConfig

class WeatherViewModel: ViewModel() {
    // UI-tilan data class
    data class WeatherUiState(
        val weather: WeatherResponse? = null,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    // Ui-tila composelle
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState = _uiState.asStateFlow()

    // Käyttäjän syöttämä kaupunki
    private val _cityInput = MutableStateFlow("")
    val cityInput = _cityInput.asStateFlow()

    // Funktio kaupungin päivittämiseen
    fun onSearchQueryChange(newCity: String) {
        _cityInput.value = newCity
    }

    private val apiService = RetrofitInstance.weatherApiService
    private val apiKey = BuildConfig.OPENWEATHER_API_KEY

    // Funktio sään hakemiseen
    fun fetchWeather() {
        val currentCity = _cityInput.value
        if (currentCity.isBlank()) return

        // API-kutsu coroutineilla
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val response = apiService.getWeather(currentCity, apiKey)
                _uiState.update { it.copy(weather = response, isLoading = false) }
            } catch (e: Exception) {
                // Virheenkäsittely
                _uiState.update { it.copy(errorMessage = "Haku epäonnistui: ${e.localizedMessage}",
                    isLoading = false) }
            }
        }
    }
}