package com.rchyn.weather.data.mappers.weather

import com.rchyn.weather.data.remote.dto.weather.WeatherDataDto
import com.rchyn.weather.data.remote.dto.weather.WeatherDto
import com.rchyn.weather.domain.model.weather.WeatherData
import com.rchyn.weather.domain.model.weather.WeatherInfo
import com.rchyn.weather.domain.model.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(lat: Double, lon: Double): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val feelsLike = apparentTemperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temparatureCelsius = temperature,
                feelsLikeCelsius = feelsLike,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode),
                latitude = lat,
                longitude = lon
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues { weathers -> weathers.value.map { it.data } }
}

fun WeatherDto.toWeatherInfo(day: Int): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap(latitude, longitude)
    val forecastWeather = weatherDataMap.mapValues { it.value[0] }
    val weatherDataByDay = weatherDataMap.filterKeys { it == day }
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[day]?.find {
        val hour = if (now.minute < 30) now.hour else now.hour + 1
        it.time.hour == if (hour == 24) now.hour else hour
    }

    return WeatherInfo(
        weatherDataByDay = weatherDataByDay,
        forecastWeatherData = forecastWeather,
        currentWeatherData = currentWeatherData
    )
}