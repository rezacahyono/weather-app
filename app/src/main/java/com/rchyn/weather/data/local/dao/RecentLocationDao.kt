package com.rchyn.weather.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rchyn.weather.data.local.entity.RecentLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentLocationDao {

    @Query("SELECT * FROM recent_location ORDER by time DESC")
    fun getAllRecentLocation(): Flow<List<RecentLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentLocation(recentLocation: RecentLocationEntity)

    @Query("DELETE FROM recent_location")
    suspend fun deleteRecentLocation()
}