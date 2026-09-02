package com.premiumweather.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.mapper.*
import com.premiumweather.app.domain.model.AnimationLevel
import com.premiumweather.app.presentation.ui.components.SettingRow
import com.premiumweather.app.presentation.ui.components.SettingSection
import com.premiumweather.app.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onAbout: () -> Unit
) {
    val settings by viewModel.settingsFlow.collectAsState(initial = com.premiumweather.app.domain.repository.AppSettings())

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            SettingSection(title = "Appearance") {
                SettingRow(title = "Theme", subtitle = settings.theme) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.theme) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("system","light","dark","weather").forEach { t ->
                                DropdownMenuItem(text = { Text(t) }, onClick = { viewModel.setTheme(t); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "High contrast") {
                    Switch(checked = settings.highContrast, onCheckedChange = { viewModel.setHighContrast(it) })
                }
                SettingRow(title = "Animation level", subtitle = settings.animationLevel.name) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.animationLevel.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            AnimationLevel.values().forEach { lvl ->
                                DropdownMenuItem(text = { Text(lvl.name) }, onClick = { viewModel.setAnimation(lvl); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "Reduced motion") {
                    Switch(checked = settings.reducedMotion, onCheckedChange = { viewModel.setReducedMotion(it) })
                }
            }

            SettingSection(title = "Units") {
                SettingRow(title = "Temperature") {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.tempUnit.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            TempUnit.values().forEach { u ->
                                DropdownMenuItem(text = { Text(u.name) }, onClick = { viewModel.setTempUnit(u); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "Wind speed") {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.speedUnit.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            SpeedUnit.values().forEach { u ->
                                DropdownMenuItem(text = { Text(u.name) }, onClick = { viewModel.setSpeedUnit(u); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "Precipitation") {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.precipUnit.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            PrecipUnit.values().forEach { u ->
                                DropdownMenuItem(text = { Text(u.name) }, onClick = { viewModel.setPrecipUnit(u); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "Pressure") {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.pressureUnit.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            PressureUnit.values().forEach { u ->
                                DropdownMenuItem(text = { Text(u.name) }, onClick = { viewModel.setPressureUnit(u); expanded = false })
                            }
                        }
                    }
                }
                SettingRow(title = "Distance") {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.distanceUnit.name) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DistanceUnit.values().forEach { u ->
                                DropdownMenuItem(text = { Text(u.name) }, onClick = { viewModel.setDistanceUnit(u); expanded = false })
                            }
                        }
                    }
                }
            }

            SettingSection(title = "Language") {
                SettingRow(title = "App language", subtitle = settings.language) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { expanded = true }) { Text(settings.language) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            listOf("system","en","ru","az").forEach { lang ->
                                DropdownMenuItem(text = { Text(lang) }, onClick = { viewModel.setLanguage(lang); expanded = false })
                            }
                        }
                    }
                }
            }

            SettingSection(title = "About") {
                Button(onClick = onAbout) { Text("About & Privacy") }
            }
        }
    }
}
