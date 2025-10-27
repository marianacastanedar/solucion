package com.gt.uvg.rickandmorty.presentation.loginFeature

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.CharacterDb
import com.gt.uvg.rickandmorty.data.LocationDb
import com.gt.uvg.rickandmorty.data.database.AppDatabase
import com.gt.uvg.rickandmorty.data.mappers.toEntity
import com.gt.uvg.rickandmorty.data.preferences.UserPreferences
import com.gt.uvg.rickandmorty.data.preferences.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    private val userPreferences = UserPreferences(application.dataStore)
    private val database = AppDatabase.getDatabase(application)
    private val characterDb = CharacterDb()
    private val locationDb = LocationDb()

    private suspend fun persistData() {
        // Obtener datos de las fuentes actuales
        val characters = characterDb.getAllCharacters()
        val locations = locationDb.getAllLocations()

        // Convertir a entities
        val characterEntities = characters.map { it.toEntity() }
        val locationEntities = locations.map { it.toEntity() }

        // Guardar en Room
        database.characterDao().insertCharacters(characterEntities)
        database.locationDao().insertLocations(locationEntities)
    }

    fun onEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.UpdateName -> {
                _state.update { it.copy(name = event.name) }
            }
            LoginScreenEvent.LogInClick -> {
                viewModelScope.launch {
                    _state.update { it.copy(syncing = true) }
                    userPreferences.updateUsername(_state.value.name)
                    persistData()
                    delay(1000)
                    _state.update {
                        it.copy(
                            syncing = false,
                            finishedSync = true
                        )
                    }
                }
            }
        }
    }
}
