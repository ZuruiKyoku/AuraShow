package com.slygames.aurashow.ui.components

import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
    transitionType: TransitionType
) {
    var currentIndex by remember { mutableStateOf(0) }
    var order by remember(imageUris) { mutableStateOf(imageUris.shuffled()) }
    var showAlt by remember { mutableStateOf(false) }
    val progress = remember { Animatable(1f) }

    val imageFrom = order.getOrNull(currentIndex)
    val imageTo = order.getOrNull((currentIndex + 1) % order.size)

    val painterFrom = rememberAsyncImagePainter(model = imageFrom)
    val painterTo = rememberAsyncImagePainter(model = imageTo)

    LaunchedEffect(order, slideDurationMillis) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
            delay(slideDurationMillis - 1000) // account for animation time
            currentIndex = (currentIndex + 1) % order.size
        }
    }

    if (imageFrom != null && imageTo != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background with same transition (optional)
            TransitionEffect(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(32.dp),
                fitMode = fitMode,
                transitionType = transitionType,
                progress = progress.value,
                from = painterFrom,
                to = painterTo,
            )

            // Foreground main image with selected fit mode
            TransitionEffect(
                modifier = Modifier.fillMaxSize(),
                fitMode = fitMode,
                transitionType = transitionType,
                progress = progress.value,
                from = painterFrom,
                to = painterTo,
            )
        }
    }
}