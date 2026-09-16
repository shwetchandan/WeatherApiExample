package com.example.sm.weatherapiexample.viewmodel

import com.example.sm.weatherapiexample.data.Clouds
import com.example.sm.weatherapiexample.data.Coord
import com.example.sm.weatherapiexample.data.Main
import com.example.sm.weatherapiexample.data.Sys
import com.example.sm.weatherapiexample.data.Weather
import com.example.sm.weatherapiexample.data.WeatherResponse
import com.example.sm.weatherapiexample.data.Wind
import com.example.sm.weatherapiexample.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: WeatherRepository
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mock()
        viewModel = WeatherViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isLoading() {
        val currentState = viewModel.isUistate.value

        assertTrue(
            "Initial state should be Loading, but got: $currentState",
            currentState is UiState.Loading
        )
    }

    @Test
    fun loadWeather_whenApiSucceeds_emitsSuccessState() = runTest {
        val fakeWeather = createFakeWeatherResponse()

        whenever(mockRepository.fetchWeather(eq("Ahmedabad"), any()))
            .thenReturn(Result.success(fakeWeather))

        viewModel.loadWeather()
        advanceUntilIdle()

        val state = viewModel.isUistate.value

        assertTrue(
            "Success state expected, but got: $state",
            state is UiState.Success
        )

        val successState = state as UiState.Success
        assertEquals("Ahmedabad", successState.weather.name)
        assertEquals(298.15, successState.weather.main.temp, 0.01)
    }

    @Test
    fun loadWeather_whenApiFails_emitsErrorState() = runTest {
        whenever(mockRepository.fetchWeather(eq("Ahmedabad"), any()))
            .thenReturn(Result.failure(Exception("Network error")))

        viewModel.loadWeather()
        advanceUntilIdle()

        val state = viewModel.isUistate.value

        assertTrue(
            "Error state expected, but got: $state",
            state is UiState.Error
        )

        val errorState = state as UiState.Error
        assertTrue(
            "Error message should be 'Network error', but got: ${errorState.message}",
            errorState.message.contains("Network error")
        )
    }

    @Test
    fun loadWeather_emitsLoadingFirst_thenSuccess() = runTest {
        val fakeWeather = createFakeWeatherResponse()
        whenever(mockRepository.fetchWeather(eq("Ahmedabad"), any()))
            .thenReturn(Result.success(fakeWeather))

        viewModel.loadWeather()

        val stateBeforeComplete = viewModel.isUistate.value

        assertTrue(
            "Loading state expected, but got: $stateBeforeComplete",
            stateBeforeComplete is UiState.Loading
        )

        advanceUntilIdle()

        val stateAfterComplete = viewModel.isUistate.value
        assertTrue(
            "Success state expected but got: $stateAfterComplete",
            stateAfterComplete is UiState.Success
        )
    }

    private fun createFakeWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            coord = Coord(longitude = 72.85, latitude = 23.02),
            weather = listOf(
                Weather(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            base = "stations",
            main = Main(
                temp = 298.15,
                feels_like = 300.0,
                temp_min = 297.0,
                temp_max = 299.0,
                pressure = 1013,
                humidity = 60,
                sea_level = 1013,
                grnd_level = 1013
            ),
            visibility = 10000,
            wind = Wind(speed = 5.5, deg = 180, gust = 7.0),
            clouds = Clouds(all = 0),
            dt = 1700000000L,
            sys = Sys(
                country = "IN",
                sunrise = 1695000000L,
                sunset = 1695040000L
            ),
            timezone = 19800,
            id = 1279233,
            name = "Ahmedabad",
            cod = 200
        )
    }
}