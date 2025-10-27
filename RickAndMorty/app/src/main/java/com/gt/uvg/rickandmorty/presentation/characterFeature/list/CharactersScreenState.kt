package com.gt.uvg.rickandmorty.presentation.characterFeature.list

import com.gt.uvg.rickandmorty.presentation.model.CharacterUi

data class CharactersScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: List<CharacterUi> = listOf()
)
