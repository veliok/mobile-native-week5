package com.example.week5.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.week5.viewmodel.WeatherViewModel

@Composable
fun WeatherResultSection(uiState: WeatherViewModel.WeatherUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            // Latausindikaattori
            uiState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            }
            // Virheilmoitukset
            uiState.errorMessage != null -> {
                Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
            }
            // Haun onnistuessa
            uiState.weather != null -> {
                val data = uiState.weather

                // Pääinfo
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${data.name}, ${data.sys.country}",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // Sääikoni
                    val iconCode = data.weather.firstOrNull()?.icon
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${iconCode}@2x.png",
                        contentDescription = data.weather.firstOrNull()?.description,
                        modifier = Modifier.size(100.dp)
                    )

                    // Lämpötila
                    Text(
                        text = "${data.main.temp.toInt()}°C",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold
                    )
                    // Kuvaus
                    Text(
                        text = data.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() }
                            ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Min/Max lämpötila
                    Text(
                        text = "${data.main.tempMin.toInt()}°/${data.main.tempMax.toInt()}°",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Lisätiedot
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        WeatherItem(
                            label = "Tuntuu kuin",
                            value = "${data.main.feelsLike.toInt()}°C",
                            modifier = Modifier.weight(1f)
                        )
                        WeatherItem(
                            label = "Kosteus",
                            value = "${data.main.humidity}%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        WeatherItem(
                            label = "Tuuli",
                            value = "${data.wind.speed.toInt()} m/s",
                            modifier = Modifier.weight(1f)
                        )
                        WeatherItem(
                            label = "Ilmanpaine",
                            value = "${data.main.pressure} hPa",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// Composable-funktio lisätietojen näyttämiseen, tuuli, kosteus, jne.
@Composable
fun WeatherItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}