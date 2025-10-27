package com.gt.uvg.rickandmorty.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    private val usernameKey = stringPreferencesKey("username")

    val username = dataStore.data.map { preferences ->
        preferences[usernameKey] ?: ""
    }

    suspend fun getUsername(): String {
        return dataStore.data.map { preferences ->
            preferences[usernameKey] ?: ""
        }.first()
    }

    suspend fun updateUsername(name: String) {
        dataStore.edit { preferences ->
            preferences[usernameKey] = name
        }
    }
}
