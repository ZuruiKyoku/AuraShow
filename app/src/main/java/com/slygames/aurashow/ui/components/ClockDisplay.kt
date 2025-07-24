package com.slygames.aurashow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Clock(
    is24HourFormat: Boolean
) {
    var currentTime by remember { mutableStateOf("") }

    val timeFormat = remember(is24HourFormat) {
        SimpleDateFormat(
            if (is24HourFormat) "HH:mm:ss" else "hh:mm:ss a",
            Locale.getDefault()
        )
    }

    LaunchedEffect(is24HourFormat) {
        while (true) {
            currentTime = timeFormat.format(Date())
            kotlinx.coroutines.delay(1000L)
        }
    }


        Text(
            text = currentTime,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
}
