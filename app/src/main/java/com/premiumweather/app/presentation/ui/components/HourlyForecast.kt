package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.mapper.TempUnit
import com.premiumweather.app.domain.mapper.UnitConverter
import com.premiumweather.app.domain.model.HourlyForecast
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HourlyForecastRow(
    hourly: List<HourlyForecast>,
    tempUnit: TempUnit,
    zoneId: ZoneId,
    modifier: Modifier = Modifier
) {
    if (hourly.isEmpty()) return
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hourly", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(hourly) { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val fmt = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId)
                        Text(text = fmt.format(item.time), style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        WeatherIcon(condition = item.condition, size = 28.dp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = UnitConverter.formatTemp(item.temperature, tempUnit), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
