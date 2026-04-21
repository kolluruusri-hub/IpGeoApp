package com.hilton.ipgeoapp.data.repository

import android.util.Log
import com.hilton.ipgeoapp.data.local.GeoDao
import com.hilton.ipgeoapp.data.remote.GeoApi
import com.hilton.ipgeoapp.model.domain.GeoLocation
import com.hilton.ipgeoapp.model.entity.GeoEntity
import com.hilton.ipgeoapp.model.mapper.toDomain
import com.hilton.ipgeoapp.model.mapper.toEntity
import com.hilton.ipgeoapp.util.Result

class GeoRepository(
    private val api: GeoApi,
    private val dao: GeoDao
) {

    companion object {
        private const val TAG = "GeoRepository"
    }

    private val CACHE_TIMEOUT = 5 * 60 * 1000

    suspend fun getGeo(ip: String): Result<GeoLocation> {
        return try {
            val cached = dao.getByIp(ip)

            if (shouldUseCache(cached)) {
                Log.d(TAG, "[CACHE HIT] IP=$ip")
                Result.Success(cached!!.toDomain())
            } else {
                Log.d(TAG, "[API FETCH] IP=$ip")

                val response = api.getGeo(ip)

                if (response.status != "success") {
                    return Result.Error("API failed")
                }

                val entity = response.toEntity(ip)
                dao.insert(entity)

                Result.Success(entity.toDomain())
            }

        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error")
        }
    }

    private fun shouldUseCache(cached: GeoEntity?): Boolean {
        return cached != null && !isExpired(cached.timestamp)
    }

    private fun isExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > CACHE_TIMEOUT
    }
}