package com.example.sm.weatherapiexample.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sm.weatherapiexample.BuildConfig
import com.example.sm.weatherapiexample.data.WeatherResponse
import com.example.sm.weatherapiexample.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val weather: WeatherResponse) : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private val uiState = MutableStateFlow<UiState>(UiState.Loading)

    val isUistate: StateFlow<UiState> = uiState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = UiState.Loading

            val city = "Ahmedabad"
            val result = repository.fetchWeather(city, BuildConfig.WEATHER_API_KEY)

            result.onSuccess { weather ->
                uiState.value = UiState.Success(weather)
            }.onFailure { error ->
                Log.e("WeatherApp", "Error: ${error.message}")

                uiState.value = UiState.Error(error.localizedMessage ?: "Unknown Error")
            }
        }
    }
}