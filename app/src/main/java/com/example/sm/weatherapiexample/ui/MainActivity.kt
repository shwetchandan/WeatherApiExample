package com.example.sm.weatherapiexample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sm.weatherapiexample.databinding.ActivityMainBinding
import com.example.sm.weatherapiexample.viewmodel.UiState
import com.example.sm.weatherapiexample.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFetch.setOnClickListener {
            viewModel.loadWeather()
        }

        observeUiState()
        viewModel.loadWeather()
    }

    @SuppressLint("SetTextI18n")
    private fun observeUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isUistate.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.GONE

                            val weather = state.weather
                            binding.tvCityName.text = weather.name
                            binding.tvTemp.text = "${weather.main.temp}°C"
                            binding.tvDescription.text = weather.weather[0].description
                            binding.tvFeelsLike.text = "Feels like ${weather.main.feels_like}°C"
                            binding.tvHumidity.text = "${weather.main.humidity}%"
                            binding.tvWind.text = "${weather.wind.speed} m/s"
                            binding.tvPressure.text = "${weather.main.pressure} hPa"
                            binding.tvVisibility.text = "${weather.visibility / 1000} km"
                            binding.tvSunrise.text = weather.sys.sunrise.toReadableTime()
                            binding.tvSunset.text = weather.sys.sunset.toReadableTime()
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = state.message
                        }
                    }
                }
            }
        }
    }

    fun Long.toReadableTime(): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format(Date(this * 1000))
    }
}