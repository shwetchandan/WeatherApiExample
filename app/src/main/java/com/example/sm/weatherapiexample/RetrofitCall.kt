package com.example.sm.weatherapiexample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCall {
    private const val BASE_URL =
        "https://api.openweathermap.org/"

    val apiCall: ApiCall by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiCall::class.java)
    }
}
