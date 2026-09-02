package com.premiumweather.app.presentation.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.premiumweather.app.domain.mapper.*
import com.premiumweather.app.domain.model.*
import com.premiumweather.app.presentation.theme.DesignTokens
import com.premiumweather.app.presentation.ui.components.*
import com.premiumweather.app.presentation.util.WeatherIconResolver
import com.premiumweather.app.presentation.viewmodel.WeatherViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WeatherViewModel,
    onNavigateSearch: () -> Unit,
    onNavigateFavorites: () -> Unit,
    onNavigateSettings: () -> Unit,
    onShare: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current
    val scroll = rememberScrollState()

    val visual = remember(uiState.snapshot?.condition) {
        val cond = uiState.snapshot?.condition
        com.premiumweather.app.domain.mapper.WeatherVisualMapper.map(cond)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        WeatherBackground(visualState = visual, animationLevel = settings.animationLevel, modifier = Modifier.fillMaxSize())

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Weather") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
                    actions = {
                        IconButton(onClick = onNavigateSearch) { Icon(Icons.Filled.Search, contentDescription = "Search city") }
                        IconButton(onClick = onNavigateFavorites) { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") }
                        IconButton(onClick = { viewModel.pullToRefresh() }) { Icon(Icons.Filled.Refresh, contentDescription = "Refresh") }
                        IconButton(onClick = onNavigateSettings) { Icon(Icons.Filled.Settings, contentDescription = "Settings") }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(scroll)
                    .padding(DesignTokens.SpacingM)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(DesignTokens.SpacingM)
            ) {
                if (uiState.isOffline) {
                    OfflineBanner()
                }
                uiState.error?.let { msg ->
                    WeatherErrorCard(message = msg, onRetry = { viewModel.clearError(); viewModel.pullToRefresh() })
                }

                val snap = uiState.snapshot
                if (snap != null) {
                    val zoneId = runCatching { ZoneId.of(snap.timezone ?: "UTC") }.getOrDefault(ZoneId.of("UTC"))
                    val fetchedText = remember(snap.fetchedAt) {
                        DateTimeFormatter.ofPattern("HH:mm, dd MMM").withZone(zoneId).format(snap.fetchedAt)
                    }
                    LocationHeader(location = snap.location, fetchedAtText = fetchedText)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = DesignTokens.ElevationMedium)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            WeatherIcon(condition = snap.condition, size = DesignTokens.IconSizeXL)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = UnitConverter.formatTemp(snap.temperature, settings.tempUnit),
                                style = MaterialTheme.typography.displayLarge
                            )
                            Text(
                                text = WeatherIconResolver.description(snap.condition),
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Feels like ${UnitConverter.formatTemp(snap.apparentTemperature, settings.tempUnit)} • ${UnitConverter.formatTemp(snap.high, settings.tempUnit)} / ${UnitConverter.formatTemp(snap.low, settings.tempUnit)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    TemperatureGraph(hourly = snap.hourly)

                    HourlyForecastRow(hourly = snap.hourly, tempUnit = settings.tempUnit, zoneId = zoneId)

                    DailyForecastColumn(daily = snap.daily, tempUnit = settings.tempUnit)

                    // Metrics grid
                    val metrics = listOfNotNull(
                        snap.humidity?.let { Triple(Icons.Filled.WaterDrop, "Humidity", "${it}%") },
                        snap.windSpeed?.let {
                            val speed = UnitConverter.speed(it, settings.speedUnit)?.let { v -> "%.1f".format(v) } ?: "--"
                            val unitLabel = when(settings.speedUnit){ SpeedUnit.KMH->"km/h"; SpeedUnit.MS->"m/s"; SpeedUnit.MPH->"mph"}
                            Triple(Icons.Filled.Air, "Wind", "$speed $unitLabel")
                        },
                        snap.pressure?.let {
                            val p = UnitConverter.pressure(it, settings.pressureUnit)?.let { v -> "%.0f".format(v) } ?: "--"
                            val unitLabel = when(settings.pressureUnit){ PressureUnit.HPA->"hPa"; PressureUnit.INHG->"inHg"}
                            Triple(Icons.Filled.Compress, "Pressure", "$p $unitLabel")
                        },
                        snap.visibility?.let {
                            val vis = UnitConverter.distance(it/1000.0, settings.distanceUnit)?.let { v -> "%.1f".format(v) } ?: "--"
                            val unitLabel = when(settings.distanceUnit){ DistanceUnit.KM->"km"; DistanceUnit.MILES->"mi"}
                            Triple(Icons.Filled.Visibility, "Visibility", "$vis $unitLabel")
                        },
                        snap.cloudCover?.let { Triple(Icons.Filled.Cloud, "Cloud cover", "${it}%") },
                        snap.uvIndex?.let { Triple(Icons.Filled.WbSunny, "UV Index", "${it}") },
                        snap.precipitation?.let {
                            val prec = UnitConverter.precipitation(it, settings.precipUnit)?.let { v -> "%.1f".format(v) } ?: "--"
                            val unitLabel = when(settings.precipUnit){ PrecipUnit.MM->"mm"; PrecipUnit.INCH->"in"}
                            Triple(Icons.Filled.Grain, "Precipitation", "$prec $unitLabel")
                        }
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            if (metrics.isNotEmpty()) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    metrics.filterIndexed { i,_ -> i%2==0 }.forEach { (icon,label,value) ->
                                        MetricCard(icon=icon, label=label, value=value, contentDesc="$label $value")
                                    }
                                }
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    metrics.filterIndexed { i,_ -> i%2==1 }.forEach { (icon,label,value) ->
                                        MetricCard(icon=icon, label=label, value=value, contentDesc="$label $value")
                                    }
                                }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            WindCompass(directionDegrees = snap.windDirection)
                            Spacer(modifier = Modifier.width(16.dp))
                            SunProgress(sunrise = snap.sunrise, sunset = snap.sunset, zoneId = zoneId, modifier = Modifier.weight(1f))
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val text = buildString {
                                appendLine("${snap.location?.name ?: "Weather"}: ${UnitConverter.formatTemp(snap.temperature, settings.tempUnit)} ${WeatherIconResolver.description(snap.condition)}")
                                appendLine("Feels like ${UnitConverter.formatTemp(snap.apparentTemperature, settings.tempUnit)}")
                                appendLine("High ${UnitConverter.formatTemp(snap.high, settings.tempUnit)} Low ${UnitConverter.formatTemp(snap.low, settings.tempUnit)}")
                                appendLine("Humidity ${snap.humidity}% Wind ${snap.windSpeed} km/h")
                            }
                            onShare(text)
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Share")
                        }
                        Button(onClick = { viewModel.refreshCurrentLocation(context) }) {
                            Icon(Icons.Filled.MyLocation, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("My location")
                        }
                    }

                } else if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text("No data. Search for a city or enable location.", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { viewModel.refreshCurrentLocation(context) }) { Text("Use current location") }
                    Button(onClick = onNavigateSearch) { Text("Search city") }
                }
            }
        }

        if (uiState.isRefreshing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
        }
    }
}
