package com.gt.uvg.rickandmorty.presentation.locationFeature.profile

import com.gt.uvg.rickandmorty.presentation.model.Location

data class LocationProfileScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: Location? = null
)