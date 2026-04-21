package com.hilton.ipgeoapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geo")
data class GeoEntity(
    @PrimaryKey val ip: String,
    val country: String,
    val region: String,
    val city: String,
    val lat: Double,
    val lon: Double,
    val isp: String,
    val timestamp: Long
)