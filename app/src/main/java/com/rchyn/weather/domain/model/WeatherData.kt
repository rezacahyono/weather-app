package com.rchyn.weather.domain.model

import java.time.LocalDateTime

data class WeatherData(
    val latitude: Double,
    val longitude: Double,
    val temparatureCelsius: Double,
    val feelsLikeCelsius: Double,
    val windSpeed: Double,
    val humidity: Double,
    val time: LocalDateTime,
    val weatherType: WeatherType,
)
