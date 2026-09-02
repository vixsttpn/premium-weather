package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SunProgress(sunrise: Instant?, sunset: Instant?, zoneId: ZoneId, modifier: Modifier = Modifier) {
    if (sunrise == null || sunset == null) return
    val now = Instant.now()
    val total = sunset.epochSecond - sunrise.epochSecond
    val elapsed = now.epochSecond - sunrise.epochSecond
    val progress = (elapsed.toFloat() / total).coerceIn(0f,1f)
    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
            val w = size.width
            val h = size.height
            drawLine(Color.Gray.copy(alpha=0.3f), Offset(0f, h/2), Offset(w, h/2), strokeWidth = 4f)
            drawCircle(Color.Yellow, radius = 8f, center = Offset(w*progress, h/2))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val fmt = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId)
            Text(text = "↑ ${fmt.format(sunrise)}")
            Text(text = "↓ ${fmt.format(sunset)}")
        }
    }
}
