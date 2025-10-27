package com.gt.uvg.rickandmorty.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gt.uvg.rickandmorty.data.model.AuthStatus
import com.gt.uvg.rickandmorty.data.preferences.UserPreferences
import com.gt.uvg.rickandmorty.data.preferences.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application.dataStore)

    private val _authStatus: MutableStateFlow<AuthStatus> = MutableStateFlow(AuthStatus.Loading)
    val authStatus = _authStatus.asStateFlow()

    init {
        viewModelScope.launch {
            val username = userPreferences.getUsername()
            if (username.isEmpty()) {
                _authStatus.update { AuthStatus.LoggedOut }
            } else {
                _authStatus.update { AuthStatus.LoggedIn }
            }
        }
    }

    val loggedStatus: StateFlow<AuthStatus> = userPreferences.username
        .map { if (it.isEmpty()) AuthStatus.LoggedOut else AuthStatus.LoggedIn }
        .catch { emit(AuthStatus.Error) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthStatus.Loading
        )
}
