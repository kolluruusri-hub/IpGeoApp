package com.hilton.ipgeoapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hilton.ipgeoapp.model.entity.GeoEntity

@Database(entities = [GeoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun geoDao(): GeoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "geo_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}