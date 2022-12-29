package com.rchyn.weather.data.mappers.location

import com.rchyn.weather.data.local.entity.RecentLocationEntity
import com.rchyn.weather.data.remote.dto.location.LocationDto
import com.rchyn.weather.domain.model.location.LocationData
import com.rchyn.weather.domain.model.location.RecentLocation
import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocationDto.toLocationData(): List<LocationData> {
    return this.results?.map {
        LocationData(
            longitude = it.longitude,
            latitude = it.latitude,
            name = it.name,
            admin = it.admin1 ?: "",
            country = it.country,
            countryCode = it.countryCode
        )
    } ?: emptyList()
}

fun RecentLocationEntity.toRecentLocation(): RecentLocation {
    return RecentLocation(name)
}

fun RecentLocation.toRecentLocationEntity(): RecentLocationEntity {
    return RecentLocationEntity(
        name = name,
        time = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    )
}