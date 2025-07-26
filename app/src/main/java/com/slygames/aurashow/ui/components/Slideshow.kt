package com.slygames.aurashow.ui.components

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.ui.effects.TransitionEffect
import com.slygames.aurashow.util.FitModeNames
import kotlinx.coroutines.delay

@Composable
fun Slideshow(
    imageUris: List<Uri>,
    slideDurationMillis: Long = 5000L,
    fitMode: FitModeNames,
    transitionType: TransitionType,
    nextTrigger: Boolean,
    prevTrigger: Boolean,
    onTriggerConsumed: () -> Unit
) {
    if (imageUris.isEmpty()) return

    var order by remember(imageUris) { mutableStateOf(imageUris.shuffled()) }
    var currentIndex by remember { mutableStateOf(0) }
    var isAnimating by remember { mutableStateOf(false) }

    val progress = remember { Animatable(1f) }
    val durationMillis = 1000

    var fromUri by remember { mutableStateOf(order[currentIndex]) }
    var toUri by remember { mutableStateOf(order[(currentIndex + 1) % order.size]) }

    val fromPainter = rememberAsyncImagePainter(model = fromUri)
    val toPainter = rememberAsyncImagePainter(model = toUri)

    fun getWrappedIndex(offset: Int): Int {
        val size = order.size
        return (currentIndex + offset + size) % size
    }

    // Automatic slideshow
    LaunchedEffect(order, slideDurationMillis) {
        while (true) {
            delay(slideDurationMillis - durationMillis)
            if (!isAnimating) {
                isAnimating = true
                fromUri = order[getWrappedIndex(0)]
                toUri = order[getWrappedIndex(1)]
                progress.snapTo(0f)
                progress.animateTo(1f, tween(durationMillis))
                currentIndex = getWrappedIndex(1)
                isAnimating = false
            }
        }
    }

    // Manual next
    LaunchedEffect(nextTrigger) {
        if (nextTrigger && !isAnimating) {
            isAnimating = true
            fromUri = order[getWrappedIndex(0)]
            toUri = order[getWrappedIndex(1)]
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis))
            currentIndex = getWrappedIndex(1)
            isAnimating = false
            onTriggerConsumed()
        }
    }

    // Manual previous
    LaunchedEffect(prevTrigger) {
        if (prevTrigger && !isAnimating) {
            isAnimating = true
            fromUri = order[getWrappedIndex(0)]
            toUri = order[getWrappedIndex(-1)]
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis))
            currentIndex = getWrappedIndex(-1)
            isAnimating = false
            onTriggerConsumed()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background blurred layer
//        TransitionEffect(
//            modifier = Modifier
//                .fillMaxSize()
//                .graphicsLayer(
//                    scaleX = 1.2f,
//                    scaleY = 1.2f
//                )
//                .blur(64.dp),
//            fitMode = FitModeNames.Crop,
//            transitionType = transitionType,
//            progress = if (isAnimating) progress.value else 1f,
//            from = fromPainter,
//            to = toPainter,
//        )

        // Foreground main transition
        TransitionEffect(
            modifier = Modifier.fillMaxSize(),
            fitMode = fitMode,
            transitionType = transitionType,
            progress = if (isAnimating) progress.value else 1f,
            from = fromPainter,
            to = toPainter
        )
    }
}