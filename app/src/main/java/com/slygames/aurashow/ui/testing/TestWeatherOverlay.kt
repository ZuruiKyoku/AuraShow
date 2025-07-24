package com.slygames.aurashow.ui.test

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slygames.aurashow.ui.effects.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestWeatherOverlay(
    weatherType: String,
    onEndTest: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Show selected weather effect
        when (weatherType) {
            "Thunderstorm" -> ThunderstormEffect(RainStrength = 2f, LighningtStrength = 2f,  modifier = Modifier.fillMaxSize())
            "Rain" -> RainEffect(strength = 2f, modifier = Modifier.fillMaxSize())
            "Light Rain" -> RainEffect(strength = 1f, modifier = Modifier.fillMaxSize())
            "Heavy Storm" -> ThunderstormEffect(RainStrength = 3f, LighningtStrength = 3f, modifier = Modifier.fillMaxSize())
            "Snow" -> SnowEffect(strength = 1f, modifier = Modifier.fillMaxSize())
            "Fog" -> FogEffect(strength = 1f, modifier = Modifier.fillMaxSize())
            "Clear" -> ClearEffect(modifier = Modifier.fillMaxSize())
            "Clouds" -> CloudOverlayEffect(modifier = Modifier.fillMaxSize())
            "Extreme" -> ThunderstormEffect(RainStrength = 3f, LighningtStrength = 3f, modifier = Modifier.fillMaxSize())
            "Wind" -> WindEffect(strength = 2f, modifier = Modifier.fillMaxSize())
        }

        // End Test button (bottom right)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = onEndTest,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.2f))
            ) {
                Text("End Test")
            }
        }
    }
}
