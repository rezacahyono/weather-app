package com.rchyn.weather.domain.model.weather

data class WeatherInfo(
    val weatherDataByDay: List<WeatherData>?,
    val forecastWeatherData: Map<Int, WeatherData>,
    val currentWeatherData: WeatherData?
)