package com.hilton.ipgeoapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilton.ipgeoapp.domain.usecase.GetGeoLocationUseCase
import com.hilton.ipgeoapp.model.domain.GeoLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.hilton.ipgeoapp.util.Result

class MainViewModel(
    private val getGeoLocationUseCase: GetGeoLocationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state

    fun search(ip: String) {

        if (!isValidIp(ip)) {
            setError("Invalid IP address")
            return
        }

        viewModelScope.launch {
            setLoading()

            when (val result = getGeoLocationUseCase(ip)) {

                is Result.Success -> {
                    setSuccess(result.data)
                }

                is Result.Error -> {
                    setError(result.message)
                }
            }
        }
    }

    private fun setLoading() {
        _state.value = UiState.Loading
    }

    private fun setError(message: String) {
        _state.value = UiState.Error(message)
    }

    private fun setSuccess(data: GeoLocation) {
        _state.value = UiState.Success(data)
    }

    private fun isValidIp(ip: String): Boolean {
        val regex = Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}$")
        return regex.matches(ip)
    }
}
