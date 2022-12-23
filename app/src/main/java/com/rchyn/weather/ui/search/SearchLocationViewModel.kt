package com.rchyn.weather.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rchyn.weather.data.mappers.location.toRecentLocationEntity
import com.rchyn.weather.domain.model.location.RecentLocation
import com.rchyn.weather.domain.repository.location.ILocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val iLocationRepository: ILocationRepository
) : ViewModel() {

    private val _searchLocationState: MutableStateFlow<SearchLocationUiState> = MutableStateFlow(
        SearchLocationUiState()
    )
    val searchLocationState: StateFlow<SearchLocationUiState> = _searchLocationState

    private val _recentLocationState: MutableStateFlow<RecentLocationUiState> = MutableStateFlow(
        RecentLocationUiState()
    )
    val recentLocationState: StateFlow<RecentLocationUiState> = _recentLocationState

    init {
        loadRecentLocation()
    }

    fun loadLocationByName(name: String) {
        viewModelScope.launch {
            _searchLocationState.update {
                it.copy(
                    isLoading = true,
                    isError = false,
                    locationDatas = emptyList()
                )
            }
            iLocationRepository.getLocation(name).collect { result ->
                result
                    .onSuccess { locations ->
                        _searchLocationState.update {
                            it.copy(
                                isLoading = false,
                                isError = false,
                                locationDatas = locations
                            )
                        }
                    }
                    .onFailure {
                        _searchLocationState.update {
                            it.copy(
                                isLoading = false,
                                isError = true,
                                locationDatas = emptyList()
                            )
                        }
                    }
            }
        }
    }

    private fun loadRecentLocation() {
        viewModelScope.launch {
            iLocationRepository.getAllRecentLocation().collect { result ->
                result
                    .onSuccess { recent ->
                        _recentLocationState.update {
                            it.copy(recentLocation = recent, isError = false)
                        }
                    }
                    .onFailure {
                        _recentLocationState.update {
                            it.copy(recentLocation = emptyList(), isError = true)
                        }
                    }
            }
        }
    }

    fun insertRecentLocation(recentLocation: RecentLocation) {
        viewModelScope.launch {
            val recentLocationEntity = recentLocation.toRecentLocationEntity()
            iLocationRepository.insertRecentLocation(recentLocationEntity)
        }
    }

    fun deleteRecentLocation() {
        viewModelScope.launch {
            iLocationRepository.deleteRecentLocation()
        }
    }

    fun clearSearchLocationState() {
        _searchLocationState.update { SearchLocationUiState() }
    }
}