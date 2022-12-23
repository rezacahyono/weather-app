package com.rchyn.weather.ui.search

import com.rchyn.weather.domain.model.location.LocationData
import com.rchyn.weather.domain.model.location.RecentLocation

data class SearchLocationUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val locationDatas: List<LocationData> = emptyList()
)


data class RecentLocationUiState(
    val isError: Boolean = false,
    val recentLocation: List<RecentLocation> = emptyList()
)