package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClearEffect(
    modifier: Modifier = Modifier
) {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    var shimmerPhase by remember { mutableStateOf(0f) }
    var trigger by remember { mutableStateOf(0) }

    // Animate shimmer slowly
    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTime != 0L) {
                    val deltaSeconds = (now - lastTime) / 1_000_000_000f
                    shimmerPhase += deltaSeconds * (PI / 10f).toFloat() // ~20s full cycle
                    if (shimmerPhase > 2 * PI) shimmerPhase -= (2 * PI).toFloat()
                    trigger++
                }
                lastTime = now
            }
        }
    }

    Canvas(modifier = modifier.zIndex(3f)) {
        trigger // observe for recomposition

        // Compute shimmer center slowly oscillating
        val shimmerX = screenWidth * (0.5f + 0.4f * cos(shimmerPhase))
        val shimmerY = screenHeight * (0.4f + 0.1f * sin(shimmerPhase / 2))

        val shimmerRadius = screenWidth * 0.7f

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFFCE5).copy(alpha = 0.06f),
                    Color.Transparent
                ),
                center = Offset(shimmerX, shimmerY),
                radius = shimmerRadius
            ),
            size = size
        )
    }
}
