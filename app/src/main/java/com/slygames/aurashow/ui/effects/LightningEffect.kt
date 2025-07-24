package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.LightningStrike
import com.slygames.aurashow.util.LerpFloat
import com.slygames.aurashow.util.LerpInt

@Composable
fun LightningEffect(
    modifier: Modifier = Modifier,
    stormStrength: Float = 1f // Range: 1.0 to 3.0
) {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    var lightningStrikes by remember { mutableStateOf(listOf<LightningStrike>()) }
    var flashAlpha by remember { mutableStateOf(0f) }

    LaunchedEffect(stormStrength) {
        while (true) {
            val delayRange = LerpFloat(6000f, 1500f, (stormStrength - 1f) / 2f)
            delay(Random.nextLong(delayRange.toLong(), (delayRange * 2).toLong()))

            val boltCount = Random.nextInt(
                LerpInt(1, 2, (stormStrength - 1f) / 2f),
                LerpInt(2, 5, (stormStrength - 1f) / 2f) + 1
            )

            val strikes = List(boltCount) {
                LightningStrike(
                    segments = generateBoltPath(
                        startX = Random.nextFloat() * screenWidth,
                        startY = 0f,
                        segmentCount = 8,
                        segmentLength = 80f
                    )
                )
            }

            val flickerCount = Random.nextInt(
                LerpInt(1, 3, (stormStrength - 1f) / 2f),
                LerpInt(2, 5, (stormStrength - 1f) / 2f) + 1
            )

            repeat(flickerCount) {
                lightningStrikes = strikes
                flashAlpha = LerpFloat(0.2f, 1.0f, (stormStrength - 1f) / 2f)
                delay(60L)
                flashAlpha = 0f
                delay(80L)
            }

            // Fade each bolt individually
            while (lightningStrikes.any { it.alpha > 0f }) {
                withFrameNanos {
                    lightningStrikes = lightningStrikes.map {
                        it.copy(alpha = (it.alpha - 0.05f).coerceAtLeast(0f))
                    }
                }
            }

            lightningStrikes = emptyList()
        }
    }

    Canvas(modifier = modifier.zIndex(10f)) {
        drawRect(color = Color.White.copy(alpha = flashAlpha))

        lightningStrikes.forEach { strike ->
            for (i in 0 until strike.segments.size - 1) {
                drawLine(
                    color = strike.color.copy(alpha = strike.alpha),
                    start = strike.segments[i],
                    end = strike.segments[i + 1],
                    strokeWidth = strike.strokeWidth
                )
            }
        }
    }
}

private fun generateBoltPath(
    startX: Float,
    startY: Float,
    segmentCount: Int,
    segmentLength: Float
): List<Offset> {
    val path = mutableListOf(Offset(startX, startY))
    var current = Offset(startX, startY)

    repeat(segmentCount) {
        val angle = Random.nextFloat() * PI.toFloat() / 3f - (PI.toFloat() / 6f)
        val dx = cos(angle) * Random.nextFloat() * 20f
        val dy = sin(angle) * segmentLength + segmentLength

        current += Offset(dx, dy)
        path.add(current)
    }

    return path
}