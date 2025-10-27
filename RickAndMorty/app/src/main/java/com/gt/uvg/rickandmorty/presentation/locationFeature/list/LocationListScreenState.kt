package com.gt.uvg.rickandmorty.presentation.locationFeature.list

import com.gt.uvg.rickandmorty.presentation.model.Location

data class LocationListScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: List<Location> = listOf()
)