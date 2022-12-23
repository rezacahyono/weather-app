package com.rchyn.weather.data.remote.service

import com.rchyn.weather.data.remote.dto.location.LocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("search")
    suspend fun getLocation(
        @Query("name") name: String
    ): LocationDto
}