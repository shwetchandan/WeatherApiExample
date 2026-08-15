package com.example.sm.weatherapiexample.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(

    @PrimaryKey
    val cityName: String,

    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val sunrise: Long,
    val sunset: Long,
    val visibility: Int,
    val country: String
)