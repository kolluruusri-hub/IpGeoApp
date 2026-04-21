package com.hilton.ipgeoapp.model.domain

data class GeoLocation(
    val ip: String,
    val country: String,
    val region: String,
    val city: String,
    val lat: Double,
    val lon: Double,
    val isp: String
)