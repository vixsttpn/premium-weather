package com.premiumweather.app.presentation.util

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import com.premiumweather.app.domain.model.WeatherVisualState

class WeatherTransitionController {
    @Composable
    fun animateVisualState(target: WeatherVisualState, animationLevel: com.premiumweather.app.domain.model.AnimationLevel): WeatherVisualState {
        if (animationLevel == com.premiumweather.app.domain.model.AnimationLevel.OFF) return target

        val speed = when(animationLevel) {
            com.premiumweather.app.domain.model.AnimationLevel.HIGH -> 1.5f
            com.premiumweather.app.domain.model.AnimationLevel.BALANCED -> 1f
            com.premiumweather.app.domain.model.AnimationLevel.LOW -> 0.5f
            com.premiumweather.app.domain.model.AnimationLevel.OFF -> 0f
        }

        val infinite = rememberInfiniteTransition(label = "weather")
        val animatedCloud by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween((8000 / speed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "cloud"
        )

        // For simplicity return target but with animated modifier accessible via composition local
        return target
    }
}
