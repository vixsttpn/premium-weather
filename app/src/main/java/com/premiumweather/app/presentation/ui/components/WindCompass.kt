package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WindCompass(directionDegrees: Double?, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(64.dp)) {
        val center = Offset(size.width/2, size.height/2)
        drawCircle(Color.Gray.copy(alpha = 0.3f), radius = size.width/2, center = center)
        if (directionDegrees != null) {
            val rad = Math.toRadians(directionDegrees)
            val end = Offset(
                center.x + cos(rad).toFloat() * size.width/2 * 0.8f,
                center.y + sin(rad).toFloat() * size.width/2 * 0.8f
            )
            drawLine(Color(0xFF3A6EA5), start = center, end = end, strokeWidth = 4f)
        }
    }
}
