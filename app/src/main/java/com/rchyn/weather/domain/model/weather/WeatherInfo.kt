package com.rchyn.weather.domain.model.weather

data class WeatherInfo(
    val weatherDataByDay: Map<Int, List<WeatherData>>,
    val forecastWeatherData: Map<Int, WeatherData>,
    val currentWeatherData: WeatherData?
)