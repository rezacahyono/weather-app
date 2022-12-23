package com.rchyn.weather.data.remote.dto.weather

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("hourly")
    val weatherData: WeatherDataDto
)
