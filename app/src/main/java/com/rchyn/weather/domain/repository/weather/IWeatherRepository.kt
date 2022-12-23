package com.rchyn.weather.domain.repository.weather

import com.rchyn.weather.domain.model.weather.WeatherInfo

interface IWeatherRepository {

    suspend fun getWeatherData(day: Int, lat: Double, long: Double): Result<WeatherInfo>

}