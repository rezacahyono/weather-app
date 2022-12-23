package com.rchyn.weather.data.remote.dto.location

import com.google.gson.annotations.SerializedName

data class LocationDataDto(
    @SerializedName("elevation")
    val elevation: Int,

    @SerializedName("country_code")
    val countryCode: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("admin1_id")
    val admin1Id: Int,

    @SerializedName("timezone")
    val timezone: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("name")
    val name: String,

    @SerializedName("admin1")
    val admin1: String?,

    @SerializedName("id")
    val id: Int,

    @SerializedName("feature_code")
    val featureCode: String,

    @SerializedName("country_id")
    val countryId: Int,

    @SerializedName("longitude")
    val longitude: Double
)