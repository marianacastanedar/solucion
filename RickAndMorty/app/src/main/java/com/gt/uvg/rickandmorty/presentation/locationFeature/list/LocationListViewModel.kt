package com.gt.uvg.rickandmorty.presentation.locationFeature.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationListViewModel(application: Application): AndroidViewModel(application) {
    private val _state = MutableStateFlow(LocationListScreenState())
    val state = _state.asStateFlow()
    private val repository = LocationRepository(application)

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _state.update { LocationListScreenState() }
            val numRandom = (0..10).random()
            if (numRandom % 2 == 0) {
                val locations = repository.getLocations()
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = locations
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
        }
    }
}