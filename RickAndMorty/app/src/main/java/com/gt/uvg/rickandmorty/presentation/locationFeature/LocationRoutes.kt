package com.gt.uvg.rickandmorty.presentation.locationFeature

import kotlinx.serialization.Serializable

sealed interface LocationRoutes {
    @Serializable
    data object LocationList : LocationRoutes

    @Serializable
    data class LocationDetail(val id: Int) : LocationRoutes
}