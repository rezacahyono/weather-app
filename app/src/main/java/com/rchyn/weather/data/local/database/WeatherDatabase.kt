package com.rchyn.weather.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rchyn.weather.data.local.dao.RecentLocationDao
import com.rchyn.weather.data.local.entity.RecentLocationEntity

@Database(entities = [RecentLocationEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun recentLocationDao(): RecentLocationDao
}