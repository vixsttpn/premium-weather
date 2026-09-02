package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.model.LocationModel

@Composable
fun LocationHeader(location: LocationModel?, fetchedAtText: String?, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = location?.name ?: "Unknown location", style = MaterialTheme.typography.headlineMedium)
        if (location?.country != null) {
            Text(text = location.country, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (fetchedAtText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Updated: $fetchedAtText", style = MaterialTheme.typography.labelSmall)
        }
    }
}
