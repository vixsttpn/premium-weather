package com.premiumweather.app.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.model.FavoriteLocation
import com.premiumweather.app.domain.repository.FavoritesRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    favoritesRepository: FavoritesRepository,
    onBack: () -> Unit,
    onSelect: (FavoriteLocation) -> Unit
) {
    val scope = rememberCoroutineScope()
    var favorites by remember { mutableStateOf<List<FavoriteLocation>>(emptyList()) }

    LaunchedEffect(Unit) {
        favorites = favoritesRepository.getAll()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Favorites") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (favorites.isEmpty()) {
                Text("No favorites yet. Add from search.", style = MaterialTheme.typography.bodyLarge)
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favorites) { fav ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = fav.name, style = MaterialTheme.typography.titleMedium)
                                Text(text = fav.country ?: "", style = MaterialTheme.typography.bodySmall)
                            }
                            Row {
                                IconButton(onClick = {
                                    scope.launch {
                                        favoritesRepository.setDefault(fav.id)
                                        favorites = favoritesRepository.getAll()
                                    }
                                }) { Icon(Icons.Filled.Star, contentDescription = "Set default") }
                                IconButton(onClick = { onSelect(fav) }) { Icon(Icons.Filled.OpenInNew, contentDescription = "Open") }
                                IconButton(onClick = {
                                    scope.launch {
                                        favoritesRepository.remove(fav.id)
                                        favorites = favoritesRepository.getAll()
                                    }
                                }) { Icon(Icons.Filled.Delete, contentDescription = "Remove") }
                            }
                        }
                    }
                }
            }
        }
    }
}
