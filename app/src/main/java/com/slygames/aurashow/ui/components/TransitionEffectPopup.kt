package com.slygames.aurashow.ui.components

import GridWithScrollbar
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.slygames.aurashow.R
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.ui.effects.TransitionEffect
import com.slygames.aurashow.util.FitModeNames

@Composable
fun TransitionEffectPopup(
    currentSelection: TransitionType,
    fitMode: FitModeNames,
    onApply: (TransitionType) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            var selected by remember { mutableStateOf(currentSelection) }
            var transitionDuration by remember { mutableStateOf(1.5f) }
            var playTrigger by remember { mutableStateOf(false) }

            val image1 = painterResource(id = R.drawable.sample_preview)
            val image2 = painterResource(id = R.drawable.sample_preview_2)
            val image3 = painterResource(id = R.drawable.sample_preview_3)
            val image4 = painterResource(id = R.drawable.sample_preview_4)

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Select Transition Effect", style = MaterialTheme.typography.titleMedium)

                PreviewBox(
                    fitMode = fitMode,
                    transitionType = selected,
                    durationSeconds = transitionDuration,
                    playTrigger = playTrigger,
                    onAnimationEnd = { playTrigger = false },
                    images = listOf(image1, image2, image3, image4)
                )

                IconButton(
                    onClick = { playTrigger = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Duration: ${"%.1f".format(transitionDuration)}s")
                    Slider(
                        value = transitionDuration,
                        onValueChange = { transitionDuration = it },
                        valueRange = 1f..3f,
                        steps = 19
                    )
                }

                val gridState = rememberLazyGridState()

                GridWithScrollbar(state = gridState, containerHeightDp = 200f) {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Adaptive(100.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .height(200.dp)
                            .padding(top = 8.dp)
                    ) {
                        items(TransitionType.entries.toList()) { type ->
                            val isSelected = type == selected
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                        else MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        selected = type
                                        playTrigger = true
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = type.displayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onApply(selected)
                        onDismiss()
                    }) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

@Composable
fun PreviewBox(
    fitMode: FitModeNames,
    transitionType: TransitionType,
    durationSeconds: Float,
    playTrigger: Boolean,
    onAnimationEnd: () -> Unit,
    images: List<Painter>
) {
    val progress = remember { Animatable(1f) }
    var currentImage by remember { mutableStateOf(images[0]) }
    var fromPainter by remember { mutableStateOf(images[0]) }
    var toPainter by remember { mutableStateOf(images[0]) }
    var isAnimating by remember { mutableStateOf(false) }

    val durationMillis = (durationSeconds * 1000).toInt()

    LaunchedEffect(playTrigger) {
        if (playTrigger && !isAnimating) {
            isAnimating = true
            fromPainter = currentImage
            toPainter = images[(images.indexOf(currentImage) + 1) % images.size]
            progress.snapTo(0f)

            progress.animateTo(1f, tween(durationMillis))

            currentImage = toPainter  // only update after animation is done
            isAnimating = false
            onAnimationEnd()
        }
    }

    TransitionEffect(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.Gray),
        fitMode = fitMode,
        transitionType = transitionType,
        progress = if (isAnimating) progress.value else 1f,
        from = fromPainter,
        to = toPainter
    )
}