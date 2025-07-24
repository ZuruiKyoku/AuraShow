package com.slygames.aurashow.ui.test

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TestWeatherPopup(
    selectedWeather: String,
    onSelectWeather: (String) -> Unit,
    onStartTest: () -> Unit,
    onDismiss: () -> Unit
) {
    val weatherOptions = listOf(
        "Thunderstorm", "Rain", "Light Rain", "Heavy Storm", "Snow", "Fog", "Clear", "Clouds", "Extreme", "Wind"
    )

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Test Weather Effect", style = MaterialTheme.typography.titleLarge)

                // Weather type dropdown
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedTextField(
                        value = selectedWeather,
                        onValueChange = {},
                        label = { Text("Weather Type") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        weatherOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    onSelectWeather(it)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Button(onClick = onStartTest) {
                        Text("Start Test")
                    }
                }
            }
        }
    }
}