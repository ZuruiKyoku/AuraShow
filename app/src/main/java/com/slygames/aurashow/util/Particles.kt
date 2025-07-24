package com.slygames.aurashow.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.slygames.aurashow.ui.effects.Season
import kotlin.math.PI
import kotlin.random.Random

// 🌧️ Internal data class for falling drops
data class Raindrop(
    val x: Float,
    val y: Float,
    val speed: Float,
    val length: Float,
    val width: Float
)

// 💦 Internal data class for splash rings
data class RaindropSplash(
    val origin: Offset,
    val velocity: Offset,
    var radius: Float = 0f,
    var alpha: Float = 1f
)

// Snowflakes
data class Snowflake(
    val x: Float,
    val y: Float,
    val wind: Float,
    val fall: Float,
    val size: Float,
    val rotation: Float,
    val spin: Float,
    val alpha: Float,
    val driftAmplitude: Float,
    val phase: Float
) {
    companion object {
        fun random(screenWidth: Float, screenHeight: Float, strength: Float): Snowflake {
            val factor = (strength - 1f) / 2f
            return Snowflake(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight,
                wind = LerpFloat(30f, 100f, factor),
                fall = LerpFloat(40f, 120f, factor),
                size = LerpFloat(6f, 9f, factor),
                rotation = Random.nextFloat() * 360f,
                spin = LerpFloat(-30f, 30f, factor),
                alpha = LerpFloat(0.3f, 0.8f, factor),
                driftAmplitude = LerpFloat(10f, 30f, factor),
                phase = Random.nextFloat() * (2 * PI).toFloat()
            )
        }
    }
}

// Leaves
data class LeafParticle(
    val x: Float,
    val y: Float,
    val speed: Float,
    val drift: Float,
    val size: Float,
    val rotation: Float,
    val spin: Float,
    val color: Color
) {
    companion object {
        fun random(screenWidth: Float, screenHeight: Float, strength: Float, season: Season): LeafParticle {
            val factor = (strength - 1f) / 2f

            val colors = when (season) {
                Season.SPRING -> listOf(Color(0xFF81C784), Color(0xFFAED581), Color(0xFFF48FB1))
                Season.SUMMER -> listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                Season.FALL -> listOf(Color(0xFFE57373), Color(0xFFFFB74D), Color(0xFFFFF176))
                Season.WINTER -> emptyList()
            }

            return LeafParticle(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight,
                speed = LerpFloat(60f, 180f, factor),
                drift = LerpFloat(-30f, 30f, factor),
                size = LerpFloat(8f, 20f, factor),
                rotation = Random.nextFloat() * 360f,
                spin = LerpFloat(-90f, 90f, factor),
                color = if (colors.isNotEmpty()) colors.random() else Color.Transparent
            )
        }
    }
}

// Wind lines
data class WindLine(
    val x: Float,
    val y: Float,
    val speed: Float,
    val length: Float,
    val thickness: Float,
    val phase: Float
) {
    companion object {
        fun random(screenWidth: Float, screenHeight: Float, strength: Float): WindLine {
            val factor = (strength - 1f) / 2f
            return WindLine(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight,
                speed = LerpFloat(100f, 300f, factor),
                length = LerpFloat(30f, 80f, factor),
                thickness = LerpFloat(1f, 3f, factor),
                phase = Random.nextFloat() * (2 * PI).toFloat()
            )
        }
    }
}

// ☁️ Cloud data with depth-driven parallax
data class Cloud(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Size,
    val alpha: Float
) {
    companion object {
        fun random(screenWidth: Float, screenHeight: Float): Cloud {
            val depth = Random.nextFloat() // 0 = far, 1 = near

            val width = LerpFloat(150f, 500f, depth)
            val height = LerpFloat(40f, 120f, depth)
            val speed = LerpFloat(4f, 20f, depth) * if (Random.nextBoolean()) 1f else -1f
            val alpha = LerpFloat(0.1f, 0.35f, depth)

            return Cloud(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight * 0.5f,
                speed = speed,
                size = Size(width, height),
                alpha = alpha
            )
        }
    }
}

// ☁️ One fog band layer
data class FogBand(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Size,
    val depthFactor: Float
) {
    companion object {
        fun random(screenWidth: Float, screenHeight: Float, strength: Float): FogBand {
            val depthFactor = Random.nextFloat() // 0 = far, 1 = near
            val base = (strength - 1f) / 2f

            val width = LerpFloat(300f,900f,base) * LerpFloat(0.6f, 1.3f, depthFactor)
            val height = LerpFloat(80f,200f,base) * LerpFloat(0.7f, 1.2f, depthFactor)
            val speed = LerpFloat(5f,30f,base) * LerpFloat(0.4f, 1.0f, depthFactor) * if (Random.nextBoolean()) 1f else -1f

            return FogBand(
                x = Random.nextFloat() * screenWidth,
                y = Random.nextFloat() * screenHeight * 0.6f,
                speed = speed,
                size = Size(width, height),
                depthFactor = depthFactor
            )
        }
    }
}

// ⚡ Data class for lightning
data class LightningStrike(
    val segments: List<Offset>,
    val strokeWidth: Float = Random.nextFloat() * 2f + 1.5f,
    var alpha: Float = 1f,
    val color: Color = Color.White.copy(
        red = 0.9f + Random.nextFloat() * 0.1f,
        green = 0.9f + Random.nextFloat() * 0.05f,
        blue = 1f
    )
)