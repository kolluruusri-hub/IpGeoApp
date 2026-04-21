package com.hilton.ipgeoapp.ui.main

import com.hilton.ipgeoapp.domain.usecase.GetGeoLocationUseCase
import com.hilton.ipgeoapp.model.domain.GeoLocation
import com.hilton.ipgeoapp.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var getGeoLocationUseCase: GetGeoLocationUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        getGeoLocationUseCase = mockk()
        viewModel = MainViewModel(getGeoLocationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search success updates state to Success`() = runTest(dispatcher) {

        val geo = GeoLocation(
            ip = "8.8.8.8",
            country = "USA",
            region = "CA",
            city = "Mountain View",
            lat = 1.0,
            lon = 2.0,
            isp = "Google"
        )

        coEvery { getGeoLocationUseCase(any()) } returns Result.Success(geo)

        viewModel.search("8.8.8.8")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is UiState.Success)
    }

    @Test
    fun `search error updates state to Error`() = runTest(dispatcher) {

        coEvery { getGeoLocationUseCase(any()) } returns Result.Error("Failed")

        viewModel.search("8.8.8.8")
        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is UiState.Error)
    }

    @Test
    fun `invalid ip returns error immediately`() = runTest(dispatcher) {

        viewModel.search("invalid-ip")

        val state = viewModel.state.value

        assertTrue(state is UiState.Error)
    }
}
