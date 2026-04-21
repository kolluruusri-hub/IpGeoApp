package com.hilton.ipgeoapp.data.remote

import com.hilton.ipgeoapp.model.dto.GeoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GeoApi {

    @GET("json/{ip}")
    suspend fun getGeo(@Path("ip") ip: String): GeoDto
}