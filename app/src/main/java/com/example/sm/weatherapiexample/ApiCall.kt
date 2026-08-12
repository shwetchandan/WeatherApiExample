package com.example.sm.weatherapiexample

import com.example.sm.weatherapiexample.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCall {

    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}