package com.hilton.ipgeoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.hilton.ipgeoapp.data.local.AppDatabase
import com.hilton.ipgeoapp.data.remote.RetrofitClient
import com.hilton.ipgeoapp.data.repository.GeoRepository
import com.hilton.ipgeoapp.domain.usecase.GetGeoLocationUseCase
import com.hilton.ipgeoapp.ui.main.MainViewModel
import com.hilton.ipgeoapp.ui.main.UiState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.get(applicationContext)
        val repo = GeoRepository(RetrofitClient.api, db.geoDao())
        val useCase = GetGeoLocationUseCase(repo)

        setContent {
            val viewModel: MainViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(useCase) as T
                    }
                }
            )

            MainScreen(viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {

    val state by viewModel.state.collectAsState()
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter IP") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.search(input) },
            enabled = input.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Success -> {
                val data = (state as UiState.Success).data
                Text("${data.city}, ${data.country}")
                Text("ISP: ${data.isp}")
                Text("Lat: ${data.lat}, Lon: ${data.lon}")
            }

            is UiState.Error -> {
                Text(
                    text = "Error: ${(state as UiState.Error).message}",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {}
        }
    }
}