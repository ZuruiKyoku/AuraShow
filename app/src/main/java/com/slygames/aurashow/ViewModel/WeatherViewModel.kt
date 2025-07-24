package com.slygames.aurashow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slygames.aurashow.data.WeatherService
import com.slygames.aurashow.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val service = WeatherService.create()
    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    fun loadWeather(lat: Double, lon: Double, apiKey: String, useImperial: Boolean) {
        viewModelScope.launch {
            try {
                val units = if (useImperial) "imperial" else "metric"
                val response = service.getCurrentWeather(lat, lon, apiKey, units)
                _weather.value = response
            } catch (e: Exception) {
                _weather.value = null
            }
        }
    }
}
