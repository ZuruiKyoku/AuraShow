package com.slygames.aurashow.ui.effects

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.LeafParticle
import com.slygames.aurashow.util.LerpFloat
import com.slygames.aurashow.util.Snowflake
import com.slygames.aurashow.util.WindLine
import java.time.LocalDate
import kotlin.math.roundToInt
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WindEffect(
    modifier: Modifier = Modifier,
    strength: Float = 2f
) {
    val clampedStrength = strength.coerceIn(1f, 3f)
    val season = remember { getCurrentSeason() }

    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    val lineCount = LerpFloat(10f, 30f, (clampedStrength - 1f) / 2f).roundToInt()
    val leafCount = if (season == Season.WINTER) 0 else LerpFloat(3f, 10f, (clampedStrength - 1f) / 2f).roundToInt()
    val snowflakeCount = if (season == Season.WINTER) LerpFloat(10f, 25f, (clampedStrength - 1f) / 2f).roundToInt() else 0

    var windLines by remember { mutableStateOf(List(lineCount) { WindLine.random(screenWidth, screenHeight, clampedStrength) }) }
    var leaves by remember { mutableStateOf(List(leafCount) { LeafParticle.random(screenWidth, screenHeight, clampedStrength, season) }) }
    var snowflakes by remember { mutableStateOf(List(snowflakeCount) { Snowflake.random(screenWidth, screenHeight, clampedStrength) }) }

    var frameTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTime != 0L) {
                    val delta = (now - lastTime) / 1_000_000_000f

                    windLines = windLines.map {
                        val newX = it.x + it.speed * delta
                        val newY = it.y + sin(it.phase + newX / 50f) * 5f
                        if (newX > screenWidth) WindLine.random(0f, screenHeight, clampedStrength)
                        else it.copy(x = newX, y = newY)
                    }

                    leaves = leaves.map {
                        val newX = it.x + it.speed * delta
                        val newY = it.y + it.drift * delta
                        val newRotation = it.rotation + it.spin * delta
                        if (newX > screenWidth || newY > screenHeight) {
                            LeafParticle.random(0f, screenHeight, clampedStrength, season)
                        } else {
                            it.copy(x = newX, y = newY, rotation = newRotation)
                        }
                    }

                    snowflakes = snowflakes.map { flake ->
                        val newX = flake.x + flake.wind * delta
                        val newY = flake.y + flake.fall * delta
                        val newRotation = flake.rotation + flake.spin * delta
                        if (newY > screenHeight || newX < -50f || newX > screenWidth + 50f) {
                            Snowflake.random(0f, screenHeight, clampedStrength)
                        } else {
                            flake.copy(x = newX, y = newY, rotation = newRotation)
                        }
                    }

                    frameTrigger++
                }
                lastTime = now
            }
        }
    }

    Canvas(modifier = modifier.zIndex(4f)) {
        frameTrigger

        windLines.forEach {
            drawLine(
                color = Color.White.copy(alpha = 0.2f),
                start = Offset(it.x, it.y),
                end = Offset(it.x - it.length, it.y),
                strokeWidth = it.thickness
            )
        }

        leaves.forEach {
            rotate(it.rotation, pivot = Offset(it.x, it.y)) {
                drawOval(
                    color = it.color,
                    topLeft = Offset(it.x - it.size / 2, it.y - it.size / 4),
                    size = androidx.compose.ui.geometry.Size(it.size, it.size / 2)
                )
            }
        }

        snowflakes.forEach { flake ->
            rotate(flake.rotation, pivot = Offset(flake.x, flake.y)) {
                drawCircle(
                    color = Color.White.copy(alpha = flake.alpha),
                    radius = flake.size,
                    center = Offset(flake.x, flake.y)
                )
            }
        }
    }
}

// Season enum
enum class Season { SPRING, SUMMER, FALL, WINTER }

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentSeason(): Season {
    val today = LocalDate.now()
    val month = today.monthValue
    val day = today.dayOfMonth
    return when {
        month == 12 && day >= 21 || month <= 3 && (month != 3 || day < 20) -> Season.WINTER
        month == 3 && day >= 20 || month in 4..5 || (month == 6 && day < 21) -> Season.SPRING
        month == 6 && day >= 21 || month in 7..8 || (month == 9 && day < 22) -> Season.SUMMER
        else -> Season.FALL
    }
}
