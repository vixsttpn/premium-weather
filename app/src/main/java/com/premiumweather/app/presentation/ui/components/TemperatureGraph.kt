package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.model.HourlyForecast

@Composable
fun TemperatureGraph(hourly: List<HourlyForecast>, modifier: Modifier = Modifier) {
    if (hourly.size < 2) return
    val temps = hourly.mapNotNull { it.temperature }
    if (temps.isEmpty()) return
    val min = temps.minOrNull() ?: 0.0
    val max = temps.maxOrNull() ?: 1.0
    val range = (max - min).takeIf { it != 0.0 } ?: 1.0
    Canvas(modifier = modifier.fillMaxWidth().height(80.dp)) {
        val w = size.width
        val h = size.height
        val path = Path()
        hourly.forEachIndexed { index, item ->
            val t = item.temperature ?: return@forEachIndexed
            val x = w * index / (hourly.size - 1)
            val y = h - ((t - min) / range * h).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = androidx.compose.ui.graphics.Color(0xFF4facfe), style = Stroke(width = 4f))
    }
}
