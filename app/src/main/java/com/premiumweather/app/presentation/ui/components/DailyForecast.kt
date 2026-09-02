package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.mapper.TempUnit
import com.premiumweather.app.domain.mapper.UnitConverter
import com.premiumweather.app.domain.model.DailyForecast
import java.time.format.DateTimeFormatter

@Composable
fun DailyForecastColumn(
    daily: List<DailyForecast>,
    tempUnit: TempUnit,
    modifier: Modifier = Modifier
) {
    if (daily.isEmpty()) return
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(text = "7-day forecast", style = MaterialTheme.typography.titleMedium)
            daily.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.date.format(DateTimeFormatter.ofPattern("EEE, MMM d")), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    WeatherIcon(condition = item.condition, size = 24.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${UnitConverter.formatTemp(item.low, tempUnit)} / ${UnitConverter.formatTemp(item.high, tempUnit)}", style = MaterialTheme.typography.bodyMedium)
                }
                Divider()
            }
        }
    }
}
