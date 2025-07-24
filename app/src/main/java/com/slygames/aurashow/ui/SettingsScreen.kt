package com.slygames.aurashow.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.ui.components.ImageFitModePopup
import com.slygames.aurashow.ui.components.TransitionEffectPopup
import com.slygames.aurashow.util.FitModeNames
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    slideDurationSeconds: Float,
    onSlideDurationChange: (Float) -> Unit,
    imageFitMode: FitModeNames,
    onImageFitModeChange: (FitModeNames) -> Unit,
    transitionType: TransitionType,
    onTransitionTypeChange: (TransitionType) -> Unit,
    showClock: Boolean,
    setShowClock: (Boolean) -> Unit,
    is24HourFormat: Boolean,
    setIs24HourFormat: (Boolean) -> Unit,
    showWeather: Boolean,
    setShowWeather: (Boolean) -> Unit,
    showWeatherEffects: Boolean,
    setShowWeatherEffects: (Boolean) -> Unit,
    onClose: () -> Unit  // <- Add this new parameter
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("General", "Extras")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 48.dp) // <– Top padding shifted down
        ) {
        TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> GeneralSettings(
                    slideDurationSeconds,
                    onSlideDurationChange,
                    imageFitMode,
                    onImageFitModeChange,
                    transitionType,
                    onTransitionTypeChange
                )
                1 -> ExtraSettings(
                    showClock,
                    setShowClock,
                    is24HourFormat,
                    setIs24HourFormat,
                    showWeather,
                    setShowWeather,
                    showWeatherEffects,
                    setShowWeatherEffects
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Red)
        }
    }
}


@Composable
fun GeneralSettings(
    slideDurationSeconds: Float,
    onSlideDurationChange: (Float) -> Unit,
    imageFitMode: FitModeNames,
    onImageFitModeChange: (FitModeNames) -> Unit,
    transitionType: TransitionType,
    onTransitionTypeChange: (TransitionType) -> Unit,
) {
    val presets = listOf(60f, 120f, 180f, 240f, 300f)
    var showFitPopup by remember { mutableStateOf(false) }
    var showTransitionPopup by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Slide Duration", color = Color.White)
        SlideDurationSelector(
            selectedDuration = slideDurationSeconds,
            onDurationSelected = onSlideDurationChange
        )
        Text("Image Fit Mode: $imageFitMode", color = Color.White)
        Button(onClick = { showFitPopup = true }) {
            Text("Change Fit Mode")
        }

        Text("Transition Type: $transitionType", color = Color.White)
        Button(
            onClick = { showTransitionPopup = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text("Change Transition Type")
        }
    }

    if (showFitPopup) {
        ImageFitModePopup(
            currentFitMode = imageFitMode,
            onApply = {
                onImageFitModeChange(it)
                showFitPopup = false
            },
            onDismiss = { showFitPopup = false }
        )
    }

    if (showTransitionPopup) {
        TransitionEffectPopup(
            currentSelection = transitionType,
            fitMode = imageFitMode,
            onApply = {
                onTransitionTypeChange(it)
                showTransitionPopup = false
            },
            onDismiss = { showTransitionPopup = false }
        )
    }

}

@Composable
fun ExtraSettings(
    showClock: Boolean,
    setShowClock: (Boolean) -> Unit,
    is24HourFormat: Boolean,
    setIs24HourFormat: (Boolean) -> Unit,
    showWeather: Boolean,
    setShowWeather: (Boolean) -> Unit,
    showWeatherEffects: Boolean,
    setShowWeatherEffects: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledSwitch("Show Clock", showClock, setShowClock)
        LabeledSwitch("24-Hour Format", is24HourFormat, setIs24HourFormat)
        LabeledSwitch("Show Weather", showWeather, setShowWeather)
        LabeledSwitch("Show Weather Effects", showWeatherEffects, setShowWeatherEffects)
    }
}

@Composable
fun SlideDurationSelector(
    selectedDuration: Float,
    onDurationSelected: (Float) -> Unit
) {
    val options = listOf(1f, 2f, 3f, 4f, 5f) // minutes
    val minSeconds = 5f
    val maxSeconds = 300f

    val displayTime = remember(selectedDuration) {
        val minutes = selectedDuration.toInt() / 60
        val seconds = selectedDuration.toInt() % 60
        if (minutes > 0) "$minutes:${seconds.toString().padStart(2, '0')}" else "${seconds}s"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Slide Duration: $displayTime",
            color = Color.White,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Slider for fine tuning
        Slider(
            value = selectedDuration,
            onValueChange = { onDurationSelected(it) },
            valueRange = minSeconds..maxSeconds,
            steps = ((maxSeconds - minSeconds) / 5).toInt() - 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Quick Set (Min)",
            color = Color.White,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            options.forEach { min ->
                val duration = min * 60f
                val isSelected = selectedDuration.roundToInt() == duration.roundToInt()
                Button(
                    onClick = { onDurationSelected(duration) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = min.toInt().toString(),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun LabeledSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f), color = Color.White)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun DropdownMenuBox(label: String, options: List<String>, selected: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}