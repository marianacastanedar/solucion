package com.gt.uvg.rickandmorty.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gt.uvg.rickandmorty.data.database.dao.CharacterDao
import com.gt.uvg.rickandmorty.data.database.dao.LocationDao
import com.gt.uvg.rickandmorty.data.database.entities.CharacterEntity
import com.gt.uvg.rickandmorty.data.database.entities.LocationEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "places_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
