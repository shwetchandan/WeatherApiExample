package com.example.sm.weatherapiexample

import com.example.sm.weatherapiexample.data.WeatherResponse

class WeatherRepository(val apiCall: ApiCall) {

    suspend fun fetchWeather(city: String, apiKey: String): Result<WeatherResponse> {
        return try {

            val response = apiCall.getWeatherData(city, apiKey)
            Result.success(response)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}