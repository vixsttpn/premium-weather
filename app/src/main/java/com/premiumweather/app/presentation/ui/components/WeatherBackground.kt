package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.premiumweather.app.domain.model.AnimationLevel
import com.premiumweather.app.domain.model.ParticleType
import com.premiumweather.app.domain.model.WeatherVisualState
import com.premiumweather.app.presentation.util.WeatherBackgroundResolver
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun WeatherBackground(
    visualState: WeatherVisualState,
    animationLevel: AnimationLevel,
    modifier: Modifier = Modifier
) {
    val colors = WeatherBackgroundResolver.gradient(visualState)
    val brush = Brush.verticalGradient(colors)

    Box(modifier = modifier.background(brush)) {
        if (animationLevel != AnimationLevel.OFF) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                when(visualState.particleType) {
                    ParticleType.RAIN -> {
                        val count = (visualState.particleDensity * 80).toInt()
                        repeat(count) { i ->
                            val x = Random.nextFloat() * w
                            val y = (System.currentTimeMillis() % 2000) / 2000f * h + Random.nextFloat()*h % h
                            drawLine(Color.White.copy(alpha = 0.5f), Offset(x, y % h), Offset(x-4, y % h + 16), strokeWidth = 2f)
                        }
                    }
                    ParticleType.SNOW -> {
                        val count = (visualState.particleDensity * 60).toInt()
                        repeat(count) {
                            val x = Random.nextFloat()*w
                            val y = (System.currentTimeMillis() % 5000) / 5000f * h + Random.nextFloat()*h % h
                            drawCircle(Color.White.copy(alpha = 0.8f), radius = 3f, center = Offset(x + sin(y/50f)*10, y % h))
                        }
                    }
                    ParticleType.CLOUDS -> {
                        val count = (visualState.cloudDensity * 5).toInt()
                        repeat(count) {
                            val x = (System.currentTimeMillis() % 20000)/20000f * w + it*100 % w
                            drawCircle(Color.White.copy(alpha = 0.25f), radius = 80f, center = Offset(x % w, h*0.2f + it*30))
                            drawCircle(Color.White.copy(alpha = 0.2f), radius = 60f, center = Offset((x+40) % w, h*0.2f + it*30 + 10))
                        }
                    }
                    else -> {}
                }
                // sun glow
                if (visualState.showSun) {
                    drawCircle(Color.Yellow.copy(alpha = 0.25f), radius = 120f, center = Offset(w*0.8f, h*0.15f))
                }
                if (visualState.showMoon) {
                    drawCircle(Color.White.copy(alpha = 0.3f), radius = 50f, center = Offset(w*0.8f, h*0.2f))
                }
            }
        }
    }
}
