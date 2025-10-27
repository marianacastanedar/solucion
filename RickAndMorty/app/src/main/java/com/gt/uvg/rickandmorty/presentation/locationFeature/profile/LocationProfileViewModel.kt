package com.gt.uvg.rickandmorty.presentation.locationFeature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.repository.LocationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationProfileViewModel(
    application: Application,
): AndroidViewModel(application) {
    private val _state = MutableStateFlow(LocationProfileScreenState())
    val state = _state.asStateFlow()
    private val repository = LocationRepository(application)

    fun fetchData(locationId: Int) {
        viewModelScope.launch {
            _state.update { LocationProfileScreenState() }
            val numRandom = (0..10).random()
            if (numRandom % 2 == 0) {
                val location = repository.getLocation(locationId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = location
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