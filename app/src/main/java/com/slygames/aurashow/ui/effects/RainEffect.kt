package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.LerpFloat
import com.slygames.aurashow.util.Raindrop
import com.slygames.aurashow.util.RaindropSplash
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun RainEffect(
    modifier: Modifier = Modifier,
    strength: Float = 2f // Range: 1.0 (light) to 3.0 (heavy)
) {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    val clampedStrength = strength.coerceIn(1f, 3f)
    val dropCount = LerpFloat(75f, 250f, (clampedStrength - 1f) / 2f).roundToInt()

    var drops by remember { mutableStateOf<List<Raindrop>>(emptyList()) }
    var splashes by remember { mutableStateOf<List<RaindropSplash>>(emptyList()) }
    var trigger by remember { mutableStateOf(0) }

    // Initialize raindrops based on strength
    LaunchedEffect(screenWidth, screenHeight, strength) {
        drops = List(dropCount) {
            Raindrop(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight,
                speed = LerpFloat(600f, 1800f, (clampedStrength - 1f) / 2f),
                length = LerpFloat(10f, 60f, (clampedStrength - 1f) / 2f),
                width = LerpFloat(0.5f, 4.5f, (clampedStrength - 1f) / 2f)
            )
        }
    }

    // Animate raindrops and splashes
    LaunchedEffect(Unit) {
        var lastTimeNanos = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTimeNanos != 0L) {
                    val deltaSeconds = (now - lastTimeNanos) / 1_000_000_000f

                    // Move drops and spawn splashes
                    drops = drops.map {
                        var newY = it.y + it.speed * deltaSeconds
                        val reset = newY > screenHeight
                        if (reset) {
                            newY = -it.length
                            splashes = splashes + RaindropSplash(
                                origin = Offset(it.x, screenHeight - 4f),
                                velocity = Offset(
                                    x = Random.nextFloat() * 60f - 30f, // slightly reduced spread
                                    y = Random.nextFloat() * -30f       // slightly reduced upward
                                ),
                                radius = 0f,
                                alpha = 1f
                            )

                        }
                        it.copy(
                            y = newY,
                            x = if (reset) Random.nextFloat() * screenWidth else it.x
                        )
                    }

                    // Animate splashes
                    splashes = splashes.mapNotNull { splash ->
                        val newRadius = splash.radius + 140f * deltaSeconds // slightly slower expansion
                        val newAlpha = splash.alpha - 2.5f * deltaSeconds   // slightly faster fade
                        val newOrigin = splash.origin + splash.velocity * deltaSeconds

                        if (newAlpha > 0f) {
                            splash.copy(
                                radius = newRadius,
                                alpha = newAlpha,
                                origin = newOrigin
                            )
                        } else null
                    }

                    trigger++
                }
                lastTimeNanos = now
            }
        }
    }

    Canvas(modifier = modifier.zIndex(5f)) {
        trigger // Observe trigger to recompose every frame

        // Draw raindrops
        drops.forEach {
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(it.x, it.y),
                end = Offset(it.x, it.y + it.length),
                strokeWidth = it.width
            )
        }

        // Draw splash rings
        splashes.forEach {
            drawCircle(
                color = Color.White.copy(alpha = it.alpha * 0.7f), // slightly dimmer splash
                radius = it.radius,
                center = it.origin,
                style = Stroke(width = 1.5f) // slightly thinner ring
            )
        }
    }
}
