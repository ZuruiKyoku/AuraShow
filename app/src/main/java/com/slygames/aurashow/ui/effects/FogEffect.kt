package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.FogBand
import com.slygames.aurashow.util.LerpFloat
import kotlin.math.*

@Composable
fun FogEffect(
    modifier: Modifier = Modifier,
    strength: Float = 2f // 1.0 = light mist, 3.0 = heavy fog
) {
    val clampedStrength = strength.coerceIn(1f, 3f)

    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    val fogCount = LerpFloat(4f, 10f, (clampedStrength - 1f) / 2f).roundToInt()
    var fogBands by remember { mutableStateOf<List<FogBand>>(emptyList()) }
    var trigger by remember { mutableStateOf(0) }

    // Initialize fog bands
    LaunchedEffect(screenWidth, screenHeight, strength) {
        fogBands = List(fogCount) {
            FogBand.random(screenWidth, screenHeight, clampedStrength)
        }
    }

    // Animate fog motion
    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTime != 0L) {
                    val deltaSeconds = (now - lastTime) / 1_000_000_000f

                    fogBands = fogBands.map { band ->
                        val newX = band.x + band.speed * deltaSeconds
                        val wrappedX = if (newX > screenWidth + band.size.width)
                            -band.size.width
                        else if (newX + band.size.width < 0f)
                            screenWidth
                        else
                            newX

                        band.copy(x = wrappedX)
                    }

                    trigger++
                }
                lastTime = now
            }
        }
    }

    // Draw layered fog
    Canvas(modifier = modifier.zIndex(4f)) {
        trigger // force recomposition

        fogBands.forEach { band ->
            val alpha = LerpFloat(0.08f, 0.25f, band.depthFactor)
            val fogColor = Color(0xDDFFFFFF).copy(alpha = alpha)

            val gradientBrush = Brush.radialGradient(
                colors = listOf(fogColor, Color.Transparent),
                center = Offset(band.size.width / 2f, band.size.height / 2f),
                radius = band.size.width * 0.6f
            )

            drawRoundRect(
                brush = gradientBrush,
                topLeft = Offset(band.x, band.y),
                size = band.size,
                cornerRadius = CornerRadius(band.size.height / 2f, band.size.height / 2f)
            )
        }
    }
}