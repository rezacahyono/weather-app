package com.rchyn.weather.data.remote.dto.location

import com.google.gson.annotations.SerializedName

data class LocationDto(
	@SerializedName("generationtime_ms")
	val generationtimeMs: Any,

	@SerializedName("results")
	val results: List<LocationDataDto>
)