package com.example.sm.weatherapiexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.example.sm.weatherapiexample.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViemodel by viewModels()

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
                            binding.tvHumidity.text = "Humidity: ${weather.main.humidity}%"
                            binding.tvSunrise.text =
                                "Sunrise: ${weather.sys.sunrise.toReadableTime()}"
                            binding.tvSunset.text = "Sunset: ${weather.sys.sunset.toReadableTime()}"
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