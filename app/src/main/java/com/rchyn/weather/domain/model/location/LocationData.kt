package com.rchyn.weather.domain.model.location

data class LocationData(
    val longitude: Double,
    val latitude: Double,
    val name: String,
    val admin: String,
    val country: String,
    val countryCode: String
)