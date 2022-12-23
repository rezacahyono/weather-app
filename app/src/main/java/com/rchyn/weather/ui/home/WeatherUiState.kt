package com.rchyn.weather.ui.home

import com.rchyn.weather.domain.model.weather.WeatherInfo

data class WeatherUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val weatherInfo: WeatherInfo? = null
)
