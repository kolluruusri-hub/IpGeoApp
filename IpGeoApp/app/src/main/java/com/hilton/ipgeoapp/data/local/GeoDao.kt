package com.hilton.ipgeoapp.data.local;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hilton.ipgeoapp.model.entity.GeoEntity

@Dao
interface GeoDao {

    @Query("SELECT * FROM geo WHERE ip = :ip")
    suspend fun getByIp(ip: String): GeoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GeoEntity)
}