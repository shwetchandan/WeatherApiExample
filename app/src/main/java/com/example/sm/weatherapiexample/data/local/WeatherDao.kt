package com.example.sm.weatherapiexample.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather WHERE cityName = :city")
    fun getWeather(city: String): Flow<WeatherEntity?>

    @Query("DELETE FROM weather")
    suspend fun clearWeather()
}