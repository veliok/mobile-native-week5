package com.example.week5.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.week5.viewmodel.WeatherViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val input by viewModel.cityInput.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Kaupungin syöttö
                OutlinedTextField(
                    value = input,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    label = { Text("Kaupunki") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                // "Hae sää"-painike
                Button(onClick = {
                    if (input.isNotBlank()) {
                        viewModel.onSearchQueryChange(input)
                        viewModel.fetchWeather()
                        focusManager.clearFocus()
                    }
                },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hae Sää")
                }
            }
            // Composable hakutulosten näyttämiseen
            WeatherResultSection(uiState = uiState)
        }
    }
}
