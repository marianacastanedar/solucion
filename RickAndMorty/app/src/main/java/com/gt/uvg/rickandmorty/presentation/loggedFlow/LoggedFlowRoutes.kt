package com.gt.uvg.rickandmorty.presentation.loggedFlow

import kotlinx.serialization.Serializable

sealed interface LoggedFlowRoutes {

    @Serializable
    data object CharactersGraph : LoggedFlowRoutes

    @Serializable
    data object LocationsGraph : LoggedFlowRoutes

    @Serializable
    data object Profile: LoggedFlowRoutes
}