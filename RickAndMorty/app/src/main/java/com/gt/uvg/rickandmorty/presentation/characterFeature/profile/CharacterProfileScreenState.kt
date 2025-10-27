package com.gt.uvg.rickandmorty.presentation.characterFeature.profile

import com.gt.uvg.rickandmorty.presentation.model.CharacterUi

data class CharacterProfileScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: CharacterUi? = null
)