package com.rchyn.weather.data.repository.weather

import com.rchyn.weather.data.mappers.weather.toWeatherInfo
import com.rchyn.weather.data.remote.service.WeatherApi
import com.rchyn.weather.domain.model.weather.WeatherInfo
import com.rchyn.weather.domain.repository.weather.IWeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : IWeatherRepository {

    override suspend fun getWeatherData(day: Int,lat: Double, long: Double): Result<WeatherInfo> {
        return try {
            Result.success(weatherApi.getWeatherData(lat, long).toWeatherInfo(day))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}