package com.rchyn.weather.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rchyn.weather.domain.repository.IWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val iWeatherRepository: IWeatherRepository
) : ViewModel() {

    private val _weatherState: MutableStateFlow<WeatherUiState> = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    init {
        getWeather()
    }

    private fun getWeather(lat: Double = -6.2761985, lon: Double = 106.7178803) {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    weatherInfo = null
                )
            }
            iWeatherRepository.getWeatherData(lat, lon)
                .onSuccess { data ->
                    _weatherState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            weatherInfo = data
                        )
                    }
                }
                .onFailure {
                    _weatherState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            weatherInfo = null
                        )
                    }
                }
        }
    }
}