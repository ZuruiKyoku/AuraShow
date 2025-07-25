package com.slygames.aurashow.ui

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.model.WeatherResponse
import com.slygames.aurashow.ui.components.Slideshow
import com.slygames.aurashow.ui.components.WeatherTimeOverlay
import com.slygames.aurashow.ui.test.TestWeatherOverlay
import com.slygames.aurashow.ui.test.TestWeatherPopup
import com.slygames.aurashow.util.FitModeNames

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    folderUri: Uri?,
    imageUris: List<Uri>,
    onPickFolder: () -> Unit,
    loadImages: (Uri) -> Unit,
    showOverlay: Boolean,
    showSettings: Boolean,
    toggleSettings: () -> Unit,
    slideDuration: Long,
    imageFitMode: FitModeNames,
    showClock: Boolean,
    is24HourFormat: Boolean,
    showWeather: Boolean,
    weather: WeatherResponse?,
    showWeatherEffects: Boolean,
    weatherUnitIsFahrenheit: Boolean,
    transitionType: TransitionType
) {
    var showTestPopup by remember { mutableStateOf(false) }
    var isTestingWeather by remember { mutableStateOf(false) }
    var selectedTestWeather by remember { mutableStateOf("Thunderstorm") }
    var nextTrigger by remember { mutableStateOf(false) }
    var prevTrigger by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        if (imageUris.isNotEmpty()) {
            Slideshow(
                imageUris = imageUris,
                slideDurationMillis = slideDuration,
                fitMode = imageFitMode,
                transitionType = transitionType,
                nextTrigger = nextTrigger,
                prevTrigger = prevTrigger,
                onTriggerConsumed = {
                    nextTrigger = false
                    prevTrigger = false
                }
            )
        }

        if (showClock || showWeather) {
            WeatherTimeOverlay(
                showClock = showClock,
                is24HourFormat = is24HourFormat,
                showWeather = showWeather,
                weather = weather,
                weatherUnitIsFahrenheit = weatherUnitIsFahrenheit,
                showWeatherEffects = showWeatherEffects
            )
        }

        if (showOverlay && !showSettings) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(Color.Black.copy(alpha = 0.3f))  // subtle translucent black background
                    .padding(horizontal = 12.dp, vertical = 6.dp) // padding inside the background box
            ){
            Text(
                "AuraShow",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Cursive,
                    fontSize = 36.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .align(Alignment.TopCenter),
                textAlign = TextAlign.Center
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = toggleSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                }
                IconButton(onClick = { showTestPopup = true }) {
                    Icon(Icons.Filled.Bolt, contentDescription = "Test Weather Effects", tint = Color.Yellow)
                }
            }

                // Prev/Next buttons vertically centered, full height
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { prevTrigger = true },
                            modifier = Modifier
                                .padding(start = 8.dp)
                        ) {
                            Text("Previous")
                        }

                        Button(
                            onClick = { nextTrigger = true },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Text("Next")
                        }
                    }
                }

                Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onPickFolder) {
                    Text("Pick Folder")
                }
            }
        }}

        if (showTestPopup) {
            TestWeatherPopup(
                selectedWeather = selectedTestWeather,
                onSelectWeather = { selectedTestWeather = it },
                onStartTest = {
                    showTestPopup = false
                    isTestingWeather = true
                },
                onDismiss = { showTestPopup = false }
            )
        }

        if (isTestingWeather) {
            TestWeatherOverlay(
                weatherType = selectedTestWeather,
                onEndTest = { isTestingWeather = false }
            )
        }

        folderUri?.let {
            LaunchedEffect(it) {
                loadImages(it)
            }
        }
    }
}