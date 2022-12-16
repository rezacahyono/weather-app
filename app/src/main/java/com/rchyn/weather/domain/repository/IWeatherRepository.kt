package com.rchyn.weather.domain.repository

import com.rchyn.weather.domain.model.WeatherInfo

interface IWeatherRepository {

    suspend fun getWeatherData(lat: Double, long: Double): Result<WeatherInfo>

}