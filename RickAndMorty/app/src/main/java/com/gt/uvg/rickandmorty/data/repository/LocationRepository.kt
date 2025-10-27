package com.gt.uvg.rickandmorty.data.repository

import android.app.Application
import com.gt.uvg.rickandmorty.data.database.AppDatabase
import com.gt.uvg.rickandmorty.data.mappers.toModel
import com.gt.uvg.rickandmorty.presentation.model.Location

class LocationRepository(
    application: Application
) {
    private val database = AppDatabase.getDatabase(application)

    suspend fun getLocations(): List<Location> {
        val locationEntities = database.locationDao().getAllLocations()
        return locationEntities.map { it.toModel() }
    }

    suspend fun getLocation(id: Int): Location? {
        val locationEntity = database.locationDao().getLocationById(id)
        return locationEntity?.toModel()
    }
}
