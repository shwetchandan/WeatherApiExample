package com.example.sm.weatherapiexample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sm.weatherapiexample.R
import com.example.sm.weatherapiexample.data.WeatherResponse
import com.example.sm.weatherapiexample.databinding.ActivityMainBinding
import com.example.sm.weatherapiexample.ui.base.BaseActivity
import com.example.sm.weatherapiexample.utility.toReadableTime
import com.example.sm.weatherapiexample.viewmodel.UiState
import com.example.sm.weatherapiexample.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val METERS_IN_KILOMETER = 1000
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: WeatherViewModel by viewModels()


    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            updateVisibility(state)
                        }

                        is UiState.Success -> {
                            updateVisibility(state)
                            val weather = state.weather
                            setData(weather)
                        }

                        is UiState.Error -> {
                            updateVisibility(state)
                            binding.tvError.text = state.message
                        }
                    }
                }
            }
        }
    }

    private fun setData(weather: WeatherResponse) {
        with(binding) {
            tvCityName.text = weather.name
            tvTemp.text =
                getString(
                    R.string.temperature_format,
                    weather.main.temp,
                )
            tvDescription.text =
                weather.weather
                    .firstOrNull()
                    ?.description
                    .orEmpty()
            tvFeelsLike.text =
                getString(
                    R.string.feels_like_format,
                    weather.main.feelsLike,
                )
            tvHumidity.text =
                getString(
                    R.string.humidity_format,
                    weather.main.humidity,
                )
            tvWind.text =
                getString(
                    R.string.wind_format,
                    weather.wind.speed,
                )
            tvPressure.text =
                getString(
                    R.string.pressure_format,
                    weather.main.pressure,
                )
            tvVisibility.text =
                getString(
                    R.string.visibility_format,
                    weather.visibility / METERS_IN_KILOMETER,
                )
            tvSunrise.text = weather.sys.sunrise.toReadableTime()
            tvSunset.text = weather.sys.sunset.toReadableTime()
        }
    }

    private fun updateVisibility(state: UiState) {
        binding.progressBar.isVisible = state is UiState.Loading
        binding.tvError.isVisible = state is UiState.Error
    }
}
