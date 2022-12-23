package com.rchyn.weather.data.remote.dto.weather

import com.google.gson.annotations.SerializedName

data class WeatherDataDto(
    @SerializedName("time")
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperatures: List<Double>,

    @SerializedName("weathercode")
    val weatherCodes: List<Int>,

    @SerializedName("windspeed_10m")
    val windSpeeds: List<Double>,

    @SerializedName("relativehumidity_2m")
    val humidities: List<Double>,

    @SerializedName("apparent_temperature")
    val apparentTemperatures: List<Double>
)
