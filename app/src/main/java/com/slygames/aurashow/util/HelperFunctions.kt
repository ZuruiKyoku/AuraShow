package com.slygames.aurashow.util

import androidx.compose.ui.layout.ContentScale
import kotlin.math.roundToInt

// 🔧 Float lerp helper
fun LerpFloat(start: Float, end: Float, fraction: Float): Float {
    return start + (end - start) * fraction.coerceIn(0f, 1f)
}

fun LerpInt(start: Int, end: Int, fraction: Float): Int {
    return (start + (end - start) * fraction.coerceIn(0f, 1f)).roundToInt()
}

enum class FitModeNames(val label: String, val scale: ContentScale) {
    Fit("Fit", ContentScale.Fit),
    Crop("Crop", ContentScale.Crop),
    FillBounds("Fill", ContentScale.FillBounds),
    Inside("Inside", ContentScale.Inside),
    None("None", ContentScale.None);

    companion object {
        fun fromContentScale(scale: ContentScale): FitModeNames =
            values().find { it.scale == scale } ?: Fit

        fun fromLabel(label: String): FitModeNames =
            values().find { it.label == label } ?: Fit
    }
}