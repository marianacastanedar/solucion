package com.gt.uvg.rickandmorty.presentation.characterFeature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterProfileViewModel(
    application: Application,
): AndroidViewModel(application) {
    private val _state = MutableStateFlow(CharacterProfileScreenState())
    val state = _state.asStateFlow()
    private val repository = CharacterRepository(application)

    fun fetchData(characterId: Int) {
        viewModelScope.launch {
            _state.update { CharacterProfileScreenState() }
            val numRandom = (0..10).random()
            if (numRandom % 2 == 0) {
                val character = repository.getCharacter(characterId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = character
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