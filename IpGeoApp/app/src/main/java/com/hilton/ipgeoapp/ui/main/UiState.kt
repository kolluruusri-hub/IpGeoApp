package com.hilton.ipgeoapp.ui.main

import com.hilton.ipgeoapp.model.domain.GeoLocation

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val data: GeoLocation) : UiState()
    data class Error(val message: String) : UiState()
}