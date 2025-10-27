package com.gt.uvg.rickandmorty.presentation.characterFeature.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application): AndroidViewModel(application) {
    private val _state = MutableStateFlow(CharactersScreenState())
    val state = _state.asStateFlow()
    private val repository = CharacterRepository(application)

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            // Reiniciamos porque ya sabemos que vamos a mostrar el loading, poner el error false y quitar data
            _state.update { CharactersScreenState() }
            val numRandom = (0..10).random()
            if (numRandom % 2 == 0) {
                val characters = repository.getCharacters()
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = characters
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