package com.hilton.ipgeoapp.model.dto

data class GeoDto(
    val status: String,
    val country: String?,
    val regionName: String?,
    val city: String?,
    val lat: Double?,
    val lon: Double?,
    val isp: String?
)