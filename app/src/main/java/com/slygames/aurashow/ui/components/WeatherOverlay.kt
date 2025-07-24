package com.slygames.aurashow.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.slygames.aurashow.model.WeatherResponse

@Composable
fun WeatherOverlay(
    weather: WeatherResponse?,
    weatherUnitIsFahrenheit: Boolean
) {
    if (weather != null) {
            Text(
                text = "${weather.name}: ${weather.main.temp}° ${if (weatherUnitIsFahrenheit) "F" else "C"} - ${weather.weather.firstOrNull()?.description}",
                color = Color.White,
            )
    }
}