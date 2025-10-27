package com.gt.uvg.rickandmorty.data.repository

import android.app.Application
import com.gt.uvg.rickandmorty.data.database.AppDatabase
import com.gt.uvg.rickandmorty.data.mappers.toModel
import com.gt.uvg.rickandmorty.presentation.model.CharacterUi

class CharacterRepository(
    application: Application
) {
    private val database = AppDatabase.getDatabase(application)

    suspend fun getCharacters(): List<CharacterUi> {
        val characterEntities = database.characterDao().getAllCharacters()
        return characterEntities.map { it.toModel() }
    }

    suspend fun getCharacter(id: Int): CharacterUi {
        val characterEntity = database.characterDao().getCharacterById(id)
        return characterEntity.toModel()
    }
}