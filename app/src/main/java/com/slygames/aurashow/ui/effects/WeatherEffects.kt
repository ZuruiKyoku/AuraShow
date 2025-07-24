package com.slygames.aurashow.ui.effects

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slygames.aurashow.model.WeatherResponse

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherEffects(
    weather: WeatherResponse?
) {
    val weatherId = weather?.weather?.firstOrNull()?.id ?: return

    // 🎯 Named conditions based on OpenWeatherMap ID ranges
    val isThunderstorm = weatherId in 200..232
    val isDrizzle = weatherId in 300..321
    val isRain = weatherId in 500..531
    val isSnow = weatherId in 600..622
    val isAtmosphere = weatherId in 701..781 // mist, fog, haze, smoke, etc.
    val isClear = weatherId == 800
    val isClouds = weatherId in 801..804
    val isExtreme = weatherId in 900..906 || weatherId in 958..962
    val isWindy = weatherId in 951..957

    // 🎬 Show only one effect (or stack later if needed)
    when {
        isThunderstorm -> {
            ThunderstormEffect(
                modifier = Modifier.fillMaxSize(),
                RainStrength = 2.0f,
                LighningtStrength = 2.0f
            )
        }

        isDrizzle -> {
            RainEffect(
                modifier = Modifier.fillMaxSize(),
                strength = 1.0f
            )
        }

        isRain -> {
            RainEffect(
                modifier = Modifier.fillMaxSize(),
                strength = 2.0f
            )
        }

        isSnow -> {
            val strength = when (weatherId) {
                602, 622 -> 3f
                600, 612, 615, 620 -> 1f
                else -> 2f
            }

            SnowEffect(
                modifier = Modifier.fillMaxSize(),
                strength = strength
            )
        }

        isAtmosphere -> {
            FogEffect(
                modifier = Modifier.fillMaxSize(),
                strength = 1f)
        }

        isClear -> {
            // No visual effect or subtle sunlight shimmer
            ClearEffect(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        isClouds -> {
            CloudOverlayEffect(
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        isExtreme -> {
            ThunderstormEffect(
                modifier = Modifier.fillMaxSize(),
                RainStrength = 3.0f,
                LighningtStrength = 3.0f
            )
        }

        isWindy -> {
            WindEffect(
                modifier = Modifier.fillMaxSize(),
                strength = 2.0f)
        }
    }
}