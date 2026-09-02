package com.premiumweather.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.premiumweather.app.domain.model.FavoriteLocation
import com.premiumweather.app.domain.model.LocationModel
import com.premiumweather.app.presentation.theme.PremiumWeatherTheme
import com.premiumweather.app.presentation.ui.screens.*
import com.premiumweather.app.presentation.viewmodel.SearchViewModel
import com.premiumweather.app.presentation.viewmodel.SettingsViewModel
import com.premiumweather.app.presentation.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as WeatherApp

        setContent {
            val settings by app.settingsRepository.observeSettings().collectAsState(initial = com.premiumweather.app.domain.repository.AppSettings())
            val darkTheme = when(settings.theme) {
                "light" -> false
                "dark" -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            PremiumWeatherTheme(darkTheme = darkTheme, highContrast = settings.highContrast) {
                val navController = rememberNavController()
                val context = LocalContext.current

                val weatherViewModel: WeatherViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return WeatherViewModel(app.weatherRepository, app.favoritesRepository, app.settingsRepository, app.geocodingProvider) as T
                    }
                })
                val searchViewModel: SearchViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return SearchViewModel(app.geocodingProvider) as T
                    }
                })
                val settingsViewModel: SettingsViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return SettingsViewModel(app.settingsRepository) as T
                    }
                })

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = weatherViewModel,
                            onNavigateSearch = { navController.navigate("search") },
                            onNavigateFavorites = { navController.navigate("favorites") },
                            onNavigateSettings = { navController.navigate("settings") },
                            onShare = { text ->
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, text)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share weather"))
                            }
                        )
                    }
                    composable("search") {
                        SearchScreen(
                            viewModel = searchViewModel,
                            onBack = { navController.popBackStack() },
                            onSelect = { result ->
                                val loc = LocationModel(result.name, result.country, result.latitude, result.longitude, result.timezone)
                                weatherViewModel.refreshWithLocation(loc)
                                // optionally add to favorites prompt - auto add? We'll add via repo in background if user wants, but for now just navigate
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("favorites") {
                        FavoritesScreen(
                            favoritesRepository = app.favoritesRepository,
                            onBack = { navController.popBackStack() },
                            onSelect = { fav ->
                                val loc = LocationModel(fav.name, fav.country, fav.latitude, fav.longitude, fav.timezone)
                                weatherViewModel.refreshWithLocation(loc)
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(viewModel = settingsViewModel, onBack = { navController.popBackStack() }, onAbout = { navController.navigate("about") })
                    }
                    composable("about") {
                        AboutScreen(onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
