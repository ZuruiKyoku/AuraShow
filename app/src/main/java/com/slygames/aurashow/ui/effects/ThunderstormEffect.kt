package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ThunderstormEffect(
    modifier: Modifier = Modifier,
    RainStrength: Float = 1.0f,
    LighningtStrength: Float = 1.0f
) {
    Box(modifier = modifier) {
        RainEffect(modifier = Modifier.fillMaxSize(), strength = RainStrength)
        LightningEffect(modifier = Modifier.fillMaxSize(), stormStrength = LighningtStrength)
    }
}