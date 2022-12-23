package com.rchyn.weather.domain.repository.location

import com.rchyn.weather.data.local.entity.RecentLocationEntity
import com.rchyn.weather.domain.model.location.LocationData
import com.rchyn.weather.domain.model.location.RecentLocation
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun getLocation(name: String): Flow<Result<List<LocationData>>>

    fun getAllRecentLocation(): Flow<Result<List<RecentLocation>>>

    suspend fun insertRecentLocation(recentLocation: RecentLocationEntity)

    suspend fun deleteRecentLocation()
}