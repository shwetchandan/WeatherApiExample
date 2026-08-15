package com.example.sm.weatherapiexample.data.repository

import com.example.sm.weatherapiexample.ApiDataClass
import com.example.sm.weatherapiexample.data.*
import com.example.sm.weatherapiexample.data.local.WeatherDao
import com.example.sm.weatherapiexample.data.local.WeatherEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiCall: ApiDataClass,
    private val weatherDao: WeatherDao
) {

    suspend fun fetchWeather(
        city: String,
        apiKey: String
    ): Result<WeatherResponse> {
        return try {
            val response = apiCall.getWeatherData(city, apiKey)

            val entity = WeatherEntity(
                cityName = response.name,
                temperature = response.main.temp,
                feelsLike = response.main.feels_like,
                description = response.weather.firstOrNull()?.description ?: "",
                humidity = response.main.humidity,
                pressure = response.main.pressure,
                windSpeed = response.wind.speed,
                sunrise = response.sys.sunrise,
                sunset = response.sys.sunset,
                visibility = response.visibility,
                country = response.sys.country
            )

            weatherDao.insertWeather(entity)
            Result.success(response)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCachedWeather(city: String): Flow<WeatherEntity?> {
        return weatherDao.getWeather(city)
    }

    suspend fun getCachedWeatherOnce(city: String): WeatherEntity? {
        return weatherDao.getWeather(city).firstOrNull()
    }
}

fun WeatherEntity.toWeatherResponse(): WeatherResponse {
    return WeatherResponse(
        coord = coord(0.0, 0.0),
        weather = listOf(Weather(id = 0, main = "", description = description, icon = "")),
        base = "",
        main = Main(
            temp = temperature,
            feels_like = feelsLike,
            temp_min = temperature,
            temp_max = temperature,
            pressure = pressure,
            humidity = humidity,
            sea_level = 0,
            grnd_level = 0
        ),
        visibility = visibility,
        wind = Wind(speed = windSpeed, deg = 0, gust = 0.0),
        clouds = Clouds(all = 0),
        dt = 0L,
        sys = Sys(country = country, sunrise = sunrise, sunset = sunset),
        timezone = 0,
        id = 0,
        name = cityName,
        cod = 200
    )
}