package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.slygames.aurashow.util.Cloud

@Composable
fun CloudOverlayEffect(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current

    val screenWidth = with(density) { config.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { config.screenHeightDp.dp.toPx() }

    val cloudCount = 10
    var clouds by remember { mutableStateOf<List<Cloud>>(emptyList()) }
    var redrawTrigger by remember { mutableStateOf(0) }

    // Initialize cloud layers with depth
    LaunchedEffect(screenWidth, screenHeight) {
        clouds = List(cloudCount) {
            Cloud.random(screenWidth, screenHeight)
        }
    }

    // Animate clouds with delta time
    LaunchedEffect(Unit) {
        var lastTime = 0L
        while (true) {
            withFrameNanos { now ->
                if (lastTime != 0L) {
                    val deltaSeconds = (now - lastTime) / 1_000_000_000f

                    clouds = clouds.map { cloud ->
                        val newX = cloud.x + cloud.speed * deltaSeconds
                        val wrappedX = if (cloud.speed > 0) {
                            if (newX > screenWidth + cloud.size.width) -cloud.size.width else newX
                        } else {
                            if (newX + cloud.size.width < 0f) screenWidth else newX
                        }

                        cloud.copy(x = wrappedX)
                    }

                    redrawTrigger++
                }
                lastTime = now
            }
        }
    }

    // Draw layered clouds
    Canvas(modifier = modifier.zIndex(3f)) {
        redrawTrigger // trigger recomposition

        clouds.forEach { cloud ->
            drawRoundRect(
                color = Color.White.copy(alpha = cloud.alpha),
                topLeft = Offset(cloud.x, cloud.y),
                size = cloud.size,
                cornerRadius = CornerRadius(cloud.size.height / 2f)
            )
        }
    }
}