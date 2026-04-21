package com.hilton.ipgeoapp.domain.usecase

import com.hilton.ipgeoapp.data.repository.GeoRepository
import com.hilton.ipgeoapp.model.domain.GeoLocation
import com.hilton.ipgeoapp.util.Result

class GetGeoLocationUseCase(
    private val repository: GeoRepository
) {

    suspend operator fun invoke(ip: String): Result<GeoLocation> {
        return repository.getGeo(ip)
    }
}
