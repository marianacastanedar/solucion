package com.gt.uvg.rickandmorty.presentation.profileFeature

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.preferences.UserPreferences
import com.gt.uvg.rickandmorty.data.preferences.dataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application.dataStore)

    val state: StateFlow<ProfileScreenState> = userPreferences.username
        .map { username ->
            ProfileScreenState(username = username)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileScreenState()
        )

    fun clearUsername() {
        viewModelScope.launch {
            userPreferences.updateUsername("")
        }
    }
}
