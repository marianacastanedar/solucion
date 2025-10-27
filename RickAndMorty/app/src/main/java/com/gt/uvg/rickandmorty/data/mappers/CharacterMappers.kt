package com.gt.uvg.rickandmorty.data.mappers

import com.gt.uvg.rickandmorty.data.database.entities.CharacterEntity
import com.gt.uvg.rickandmorty.data.database.entities.LocationEntity
import com.gt.uvg.rickandmorty.presentation.model.CharacterUi
import com.gt.uvg.rickandmorty.presentation.model.Location

fun CharacterUi.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}

fun CharacterEntity.toModel(): CharacterUi {
    return CharacterUi(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}