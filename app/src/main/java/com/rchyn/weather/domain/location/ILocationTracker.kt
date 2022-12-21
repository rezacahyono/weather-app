package com.rchyn.weather.domain.location

import android.location.Location

interface ILocationTracker {
    suspend fun getCurrentLocation(): Location?
}