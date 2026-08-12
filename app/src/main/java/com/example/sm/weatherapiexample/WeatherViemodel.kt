package com.example.sm.weatherapiexample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sm.weatherapiexample.data.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val weather: WeatherResponse) : UiState()
    data class Error(val message: String) : UiState()
}

class WeatherViemodel : ViewModel() {
    private val repository = WeatherRepository(RetrofitCall.apiCall)

    private val uiState = MutableStateFlow<UiState>(UiState.Loading)
    val isUistate: StateFlow<UiState> = uiState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            uiState.value = UiState.Loading

            val result = repository.fetchWeather("Ahmedabad", BuildConfig.WEATHER_API_KEY)
            result.onSuccess { weather ->
                uiState.value = UiState.Success(weather)
            }.onFailure { error ->
                Log.e("WeatherApp", "Error: ${error.message}")
                uiState.value = UiState.Error(error.localizedMessage ?: "Unknown Error")
            }
        }
    }
}