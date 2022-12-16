package com.rchyn.weather.ui.home

import com.rchyn.weather.domain.model.WeatherInfo

data class WeatherUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val weatherInfo: WeatherInfo? = null
)
