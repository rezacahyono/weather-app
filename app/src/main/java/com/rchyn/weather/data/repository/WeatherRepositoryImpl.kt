package com.rchyn.weather.data.repository

import com.rchyn.weather.data.mappers.toWeatherInfo
import com.rchyn.weather.data.remote.service.WeatherApi
import com.rchyn.weather.domain.model.WeatherInfo
import com.rchyn.weather.domain.repository.IWeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : IWeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Result<WeatherInfo> {
        return try {
            Result.success(weatherApi.getWeatherData(lat, long).toWeatherInfo())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}