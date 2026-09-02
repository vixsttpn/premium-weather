package com.premiumweather.app.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import com.premiumweather.app.domain.model.WeatherCondition
import com.premiumweather.app.presentation.theme.DesignTokens
import com.premiumweather.app.presentation.util.WeatherIconResolver

@Composable
fun WeatherIcon(
    condition: WeatherCondition?,
    size: Dp = DesignTokens.IconSizeXL,
    modifier: Modifier = Modifier,
    contentDesc: String? = null
) {
    val icon = WeatherIconResolver.resolve(condition)
    val desc = contentDesc ?: WeatherIconResolver.description(condition)
    Icon(
        imageVector = icon,
        contentDescription = desc,
        modifier = modifier.size(size).semantics { contentDescription = desc }
    )
}
