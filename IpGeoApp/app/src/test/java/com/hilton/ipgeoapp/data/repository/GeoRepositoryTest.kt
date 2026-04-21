package com.hilton.ipgeoapp.data.repository

import com.hilton.ipgeoapp.data.local.GeoDao
import com.hilton.ipgeoapp.data.remote.GeoApi
import com.hilton.ipgeoapp.model.dto.GeoDto
import com.hilton.ipgeoapp.model.entity.GeoEntity
import com.hilton.ipgeoapp.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class GeoRepositoryTest {

    private val api = mockk<GeoApi>()
    private val dao = mockk<GeoDao>()

    private val repository = GeoRepository(api, dao)

    @Test
    fun `returns cache when not expired`() = runTest {

        val cached = GeoEntity(
            ip = "8.8.8.8",
            country = "USA",
            region = "CA",
            city = "Mountain View",
            lat = 1.0,
            lon = 2.0,
            isp = "Google",
            timestamp = Long.MAX_VALUE
        )

        coEvery { dao.getByIp(any()) } returns cached

        val result = repository.getGeo("8.8.8.8")

        assertTrue(result is Result.Success)
    }

    @Test
    fun `calls API when cache missing`() = runTest {

        coEvery { dao.getByIp(any()) } returns null

        coEvery { api.getGeo(any()) } returns GeoDto(
            status = "success",
            country = "USA",
            regionName = "CA",
            city = "Mountain View",
            lat = 1.0,
            lon = 2.0,
            isp = "Google"
        )

        coEvery { dao.insert(any()) } returns Unit

        val result = repository.getGeo("8.8.8.8")

        assertTrue(result is Result.Success)
    }
}