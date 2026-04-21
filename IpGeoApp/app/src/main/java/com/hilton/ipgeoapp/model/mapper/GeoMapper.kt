package com.hilton.ipgeoapp.model.mapper

import com.hilton.ipgeoapp.model.dto.GeoDto
import com.hilton.ipgeoapp.model.entity.GeoEntity
import com.hilton.ipgeoapp.model.domain.GeoLocation

fun GeoDto.toEntity(ip: String) = GeoEntity(
    ip = ip,
    country = country ?: "",
    region = regionName ?: "",
    city = city ?: "",
    lat = lat ?: 0.0,
    lon = lon ?: 0.0,
    isp = isp ?: "",
    timestamp = System.currentTimeMillis()
)

fun GeoEntity.toDomain() = GeoLocation(
    ip = ip,
    country = country,
    region = region,
    city = city,
    lat = lat,
    lon = lon,
    isp = isp
)