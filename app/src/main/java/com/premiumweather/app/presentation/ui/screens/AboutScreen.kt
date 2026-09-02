package com.premiumweather.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("About") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Premium Weather", style = MaterialTheme.typography.headlineSmall)
            Text("Version 1.0.0")
            Text("A premium, minimal, modern weather app built with Jetpack Compose and Material 3. Works offline, no API key required.")
            Divider()
            Text("Data provider: Open-Meteo (https://open-meteo.com) - Free weather API without API key. Uses public HTTPS endpoints.")
            Text("Attribution: Weather data by Open-Meteo.com, licensed under CC BY 4.0. Please respect their rate limits and terms.")
            Divider()
            Text("Privacy:", style = MaterialTheme.typography.titleMedium)
            Text("• No accounts, no ads, no analytics, no tracking, no Firebase.")
            Text("• No advertising ID, no secrets, no API keys.")
            Text("• Only permissions: INTERNET, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, optional POST_NOTIFICATIONS.")
            Text("• Location is used only to fetch weather and is not logged or transmitted elsewhere.")
            Text("• All network requests use HTTPS.")
            Text("• Data stays on device (Room + DataStore).")
            Divider()
            Text("Open Source & License: MIT")
        }
    }
}
