package com.slygames.aurashow.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slygames.aurashow.model.WeatherResponse
import com.slygames.aurashow.ui.effects.WeatherEffects

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherTimeOverlay(
    showClock: Boolean,
    is24HourFormat: Boolean,
    showWeather: Boolean,
    weather: WeatherResponse?,
    weatherUnitIsFahrenheit: Boolean,
    showWeatherEffects: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        if (showClock || showWeather) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color(0x66000000), shape = RoundedCornerShape(12.dp)) // semi-transparent black
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    if (showClock) {
                        Clock(is24HourFormat = is24HourFormat)
                    }

                    if (showWeather) {
                        WeatherOverlay(
                            weather = weather,
                            weatherUnitIsFahrenheit = weatherUnitIsFahrenheit
                        )
                    }
                }
            }
            if(showWeather && showWeatherEffects){
                WeatherEffects(weather)
            }
        }
    }
}
