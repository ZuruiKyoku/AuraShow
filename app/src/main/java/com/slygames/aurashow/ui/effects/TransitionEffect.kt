package com.slygames.aurashow.ui.effects

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.util.FitModeNames


@Composable
fun TransitionEffect(
    modifier: Modifier = Modifier,
    fitMode: FitModeNames,
    transitionType: TransitionType,
    progress: Float,
    from: Painter,
    to: Painter
) {
    var containerWidth by remember { mutableStateOf(0) }
    var containerHeight by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .clipToBounds()
            .background(Color.Black)
            .onSizeChanged {
                containerWidth = it.width
                containerHeight = it.height
            }
    ){


        // Show appropriate base image
        val basePainter = if (progress < 1f) from else to
        DrawImageStack(
            basePainter,
            fitMode,
            modifier.matchParentSize()
        )

        if (progress < 1f){
            when (transitionType) {
                TransitionType.Crossfade -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(alpha = progress)
                    )
                }
                TransitionType.SlideLeft -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                        .matchParentSize()
                        .graphicsLayer(translationX = (1f - progress) * containerWidth))
                }
                TransitionType.SlideRight -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(translationX = -(1f - progress) * containerWidth)
                    )
                }
                TransitionType.SlideUp -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(translationY = (1f - progress) * containerHeight)
                    )
                }
                TransitionType.SlideDown -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(translationY = -(1f - progress) * containerHeight)
                    )
                }
                TransitionType.ZoomIn -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(
                                scaleX = progress,
                                scaleY = progress,
                                alpha = progress
                            )
                    )
                }
                TransitionType.ZoomOut -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(
                                scaleX = 1f + (1f - progress),
                                scaleY = 1f + (1f - progress),
                                alpha = progress
                            )
                    )
                }
                TransitionType.RotateIn -> {
                    DrawImageStack(
                        painter = to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(
                                rotationZ = (1f - progress) * 360f,
                                alpha = progress
                            )
                    )
                }
                TransitionType.RotateOut -> {
                    DrawImageStack(
                        to,
                        fitMode,
                        Modifier
                            .matchParentSize()
                            .graphicsLayer(
                                rotationZ = progress * 360f,
                                alpha = progress
                            )
                    )
                }
                TransitionType.FadeToBlack -> {
                    if (progress < 0.5f) {
                        // First half: fade to black
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = progress * 2))
                        )
                    } else {
                        // Second half: fade from black into next DrawImageStack
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black)
                        )
                        DrawImageStack(
                            to,
                            fitMode,
                            Modifier
                                .matchParentSize()
                                .graphicsLayer(alpha = (progress - 0.5f) * 2)
                        )
                    }
                }
                TransitionType.WipeHorizontal -> {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clipToBounds()
                    ) {
                        DrawImageStack(
                            to,
                            fitMode,
                            Modifier
                                .fillMaxHeight()
                                .width(progress * 1000f.dp)
                        )
                    }
                }
                TransitionType.WipeVertical -> {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clipToBounds()
                    ) {
                        DrawImageStack(
                            to,
                            fitMode,
                            Modifier
                                .fillMaxWidth()
                                .height(progress * 1000f.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawImageStack(
    painter: Painter,
    fitMode: FitModeNames,
    modifier: Modifier = Modifier,
){
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = FitModeNames.Crop.scale,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = 1.2f,
                scaleY = 1.2f
            )
            .blur(24.dp)
            .then(modifier)
    )
    Image(
        painter = painter,
        contentDescription = null,
        contentScale = fitMode.scale,
        modifier = modifier,
    )
}