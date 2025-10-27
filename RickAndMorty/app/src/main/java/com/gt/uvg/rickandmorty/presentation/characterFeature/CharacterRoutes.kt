package com.gt.uvg.rickandmorty.presentation.characterFeature

import kotlinx.serialization.Serializable

sealed interface CharacterRoutes {

    @Serializable
    data object CharacterList : CharacterRoutes

    @Serializable
    data class CharacterProfile(val id: Int) : CharacterRoutes
}