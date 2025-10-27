package com.gt.uvg.rickandmorty.data.mappers

import com.gt.uvg.rickandmorty.data.database.entities.LocationEntity
import com.gt.uvg.rickandmorty.presentation.model.Location

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        dimension = this.dimension
    )
}

fun LocationEntity.toModel(): Location {
    return Location(
        id = this.id,
        name = this.name,
        type = this.type,
        dimension = this.dimension
    )
}
