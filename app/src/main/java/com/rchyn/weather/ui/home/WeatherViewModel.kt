package com.rchyn.weather.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rchyn.weather.domain.location.ILocationTracker
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
    private val iWeatherRepository: IWeatherRepository,
    private val iLocationTracker: ILocationTracker
) : ViewModel() {

    private val _weatherState: MutableStateFlow<WeatherUiState> = MutableStateFlow(WeatherUiState())
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    fun loadWeatherByCurrentLocation() {
        viewModelScope.launch {
            _weatherState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    weatherInfo = null
                )
            }
            iLocationTracker.getCurrentLocation()?.let { location ->
                iWeatherRepository.getWeatherData(location.latitude, location.longitude)
                    .onSuccess { weatherInfo ->
                        _weatherState.update {
                            it.copy(
                                isLoading = false,
                                isError = false,
                                weatherInfo = weatherInfo
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

            } ?: kotlin.run {
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