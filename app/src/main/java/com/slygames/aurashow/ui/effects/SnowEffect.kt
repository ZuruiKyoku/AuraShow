package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.Snowflake
import com.slygames.aurashow.util.LerpFloat
import kotlin.math.*

@Composable
fun SnowEffect(
    modifier: Modifier = Modifier,
    strength: Float = 2f // 1.0 = light, 3.0 = heavy
) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val screenWidth = with(density) { config.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { config.screenHeightDp.dp.toPx() }

    val clampedStrength = strength.coerceIn(1f, 3f)
    val flakeCount = LerpFloat(50f, 200f, (clampedStrength - 1f) / 2f).roundToInt()
    val maxPileHeight = LerpFloat(20f, 40f, (clampedStrength - 1f) / 2f)

    val pileResolution = (screenWidth / 6).roundToInt().coerceAtLeast(60)
    var flakes by remember { mutableStateOf<List<Snowflake>>(emptyList()) }
    var snowPile by remember { mutableStateOf(FloatArray(pileResolution) { 0f }) }
    var redrawTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(screenWidth, screenHeight, strength) {
        flakes = List(flakeCount) {
            Snowflake.random(screenWidth, screenHeight, clampedStrength)
        }
    }

    LaunchedEffect(Unit) {
        var lastTimeNanos = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTimeNanos != 0L) {
                    val deltaSeconds = (now - lastTimeNanos) / 1_000_000_000f

                    flakes = flakes.map { flake ->
                        val driftX = sin(flake.y / 20f + flake.phase) * flake.driftAmplitude
                        val newX = (flake.x + driftX * deltaSeconds).coerceIn(0f, screenWidth)
                        val newY = flake.y + flake.fall * deltaSeconds

                        val pileIndex = ((newX / screenWidth) * snowPile.size)
                            .toInt().coerceIn(0, snowPile.lastIndex)
                        val pileHeight = snowPile[pileIndex]

                        if (newY + flake.size >= screenHeight - pileHeight) {
                            val impact = max(flake.size * 2f, 4f)
                            snowPile[pileIndex] = (pileHeight + impact)
                                .coerceAtMost(maxPileHeight)
                            Snowflake.random(screenWidth, screenHeight, clampedStrength)
                        } else {
                            flake.copy(x = newX, y = newY)
                        }
                    }

                    val meltRate = 0.1f * deltaSeconds
                    snowPile = snowPile.map { height ->
                        (height - meltRate).coerceAtLeast(0f)
                    }.toFloatArray()

                    redrawTrigger++
                }
                lastTimeNanos = now
            }
        }
    }

    Canvas(modifier = modifier.zIndex(5f)) {
        redrawTrigger // trigger recomposition

        flakes.forEach { flake ->
            val shimmerAlpha = (0.6f + 0.4f * sin(flake.y / 10f + flake.phase)) *
                    (1f - (flake.size / 10f).coerceIn(0f, 1f))
            drawCircle(
                color = Color.White.copy(alpha = shimmerAlpha),
                radius = flake.size,
                center = Offset(flake.x, flake.y),
                style = Fill
            )
        }

        val sliceWidth = size.width / snowPile.size
        snowPile.forEachIndexed { index, height ->
            if (height > 0f) {
                val avgHeight = (
                        snowPile.getOrElse(index - 1) { 0f } +
                                snowPile[index] +
                                snowPile.getOrElse(index + 1) { 0f }
                        ) / 3f

                drawRect(
                    color = Color.White,
                    topLeft = Offset(index * sliceWidth, size.height - avgHeight),
                    size = androidx.compose.ui.geometry.Size(sliceWidth + 1f, avgHeight)
                )
            }
        }
    }
}
