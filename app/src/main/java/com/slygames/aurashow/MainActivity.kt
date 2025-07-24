package com.slygames.aurashow

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import com.google.android.gms.location.LocationServices
import com.slygames.aurashow.model.TransitionType
import com.slygames.aurashow.ui.MainScreen
import com.slygames.aurashow.ui.SettingsScreen
import com.slygames.aurashow.ui.permissions.LocationPermissionRequest
import com.slygames.aurashow.ui.theme.AuraShowTheme
import com.slygames.aurashow.util.FitModeNames
import com.slygames.aurashow.util.WeatherConfig
import com.slygames.aurashow.viewmodel.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        private const val PREFS_NAME = "AuraShowPrefs"
        private const val KEY_FOLDER_URI = "folder_uri"
        private const val KEY_SLIDE_DURATION = "slide_duration"
        private const val KEY_FIT_MODE = "fit_mode"
        private const val KEY_SHOW_CLOCK = "show_clock"
        private const val KEY_24_HOUR_FORMAT = "24_hour_format"
        private const val KEY_SHOW_WEATHER = "show_weather"
        private const val KEY_WEATHERUNITISFAHRENHEIT = "unit_is_fahrenheit"
        private const val KEY_SHOW_WEATHER_EFFECTS = "show_weather_effects"
        private const val KEY_TRANSITION_EFFECT = "transition_effect"
    }

    private var folderUri by mutableStateOf<Uri?>(null)
    private var imageUris by mutableStateOf<List<Uri>>(emptyList())
    private var showOverlay by mutableStateOf(false)
    private var showSettings by mutableStateOf(false)
    private var slideDurationText by mutableStateOf(TextFieldValue("5"))
    private var slideDuration by mutableStateOf(5000L)
    private var imageFitMode by mutableStateOf(FitModeNames.Fit)
    private var showClock by mutableStateOf(false)
    private var is24HourFormat by mutableStateOf(false)
    private var locationPermissionGranted by mutableStateOf(false)
    private var transitionType by mutableStateOf(TransitionType.Crossfade)

    // Location state
    private var latitude by mutableStateOf<Double?>(null)
    private var longitude by mutableStateOf<Double?>(null)

    // Show animated weather effects toggle
    private var showWeatherEffects by mutableStateOf(true)
    private var showWeather by mutableStateOf(false)
    private var weatherUnitIsFahrenheit by mutableStateOf(true)


    // WeatherViewModel instance
    private val weatherViewModel by viewModels<WeatherViewModel>()

    private val folderPickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            folderUri = it
            saveFolderUri(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        // Restore saved prefs
        prefs.getString(KEY_FOLDER_URI, null)?.let { uriString ->
            val uri = Uri.parse(uriString)
            folderUri = uri
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        slideDuration = prefs.getLong(KEY_SLIDE_DURATION, 5000L)
        slideDurationText = TextFieldValue((slideDuration / 1000).toString())
        imageFitMode = FitModeNames.valueOf(prefs.getString(KEY_FIT_MODE, "Crop") ?: "Crop")
        showClock = prefs.getBoolean(KEY_SHOW_CLOCK, true)
        is24HourFormat = prefs.getBoolean(KEY_24_HOUR_FORMAT, false)
        showWeather = prefs.getBoolean(KEY_SHOW_WEATHER, true)
        weatherUnitIsFahrenheit = prefs.getBoolean(KEY_WEATHERUNITISFAHRENHEIT, true)
        showWeatherEffects = prefs.getBoolean(KEY_SHOW_WEATHER_EFFECTS, true)
        transitionType = TransitionType.valueOf(prefs.getString(KEY_TRANSITION_EFFECT, "Crossfade") ?: "Crossfade")

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        actionBar?.hide()

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            AuraShowTheme {
                val weather by weatherViewModel.weather.collectAsState()

                // Trigger weather loading when location & permission available
                LaunchedEffect(locationPermissionGranted, latitude, longitude, weatherUnitIsFahrenheit) {
                    if (locationPermissionGranted && latitude != null && longitude != null) {
                        weatherViewModel.loadWeather(latitude!!, longitude!!, WeatherConfig.API_KEY, weatherUnitIsFahrenheit)
                    }
                }

                var overlayTimerJob by remember { mutableStateOf<Job?>(null) }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = !showSettings) {
                            if (!showOverlay) {
                                showOverlay = true
                                overlayTimerJob?.cancel()
                                overlayTimerJob = CoroutineScope(Dispatchers.Main).launch {
                                    delay(5000)
                                    showOverlay = false
                                }
                            } else {
                                overlayTimerJob?.cancel()
                                showOverlay = false
                            }
                        }
                ){
                    if (!locationPermissionGranted) {
                        // Auto-request location permission composable
                        LocationPermissionRequest {
                            locationPermissionGranted = true
                            fetchLastLocation()
                        }
                    } else {
                        // Ensure location fetched if permission was previously granted
                        LaunchedEffect(Unit) {
                            fetchLastLocation()
                        }
                        if (showSettings) {
                            SettingsScreen(
                                slideDurationSeconds = slideDuration / 1000f,
                                onSlideDurationChange = {
                                    slideDuration = (it * 1000).toLong()
                                    slideDurationText = TextFieldValue(slideDuration.toString())
                                    saveSlideDuration(slideDuration)
                                },
                                imageFitMode = imageFitMode,
                                onImageFitModeChange = {
                                    imageFitMode = it
                                    saveFitMode(imageFitMode)
                                },
                                transitionType = transitionType,
                                onTransitionTypeChange = {
                                    transitionType = it
                                    saveTransitionType(transitionType)
                                },
                                showClock = showClock,
                                setShowClock = {
                                    showClock = it
                                    saveShowClock(it)
                                },
                                is24HourFormat = is24HourFormat,
                                setIs24HourFormat = {
                                    is24HourFormat = it
                                    saveIs24HourFormat(it)
                                },
                                showWeather = showWeather,
                                setShowWeather = {
                                    showWeather = it
                                    saveShowWeather(it)
                                },
                                showWeatherEffects = showWeatherEffects,
                                setShowWeatherEffects = {
                                    showWeatherEffects = it
                                    saveShowWeatherEffects(it)
                                },
                                onClose = {
                                    showSettings = false
                                    showOverlay = false
                                }
                            )

                        } else {
                            MainScreen(
                                folderUri = folderUri,
                                imageUris = imageUris,
                                onPickFolder = { folderPickerLauncher.launch(null) },
                                loadImages = { uri -> loadImageUrisFromFolder(uri) { imageUris = it } },
                                showOverlay = showOverlay,
                                showSettings = showSettings,
                                toggleSettings = { showSettings = !showSettings },
                                slideDuration = slideDuration,
                                imageFitMode = imageFitMode,
                                showClock = showClock,
                                is24HourFormat = is24HourFormat,
                                showWeather = showWeather,
                                weather = weather,
                                showWeatherEffects = showWeatherEffects,
                                weatherUnitIsFahrenheit = weatherUnitIsFahrenheit,
                                transitionType = transitionType
                            )
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLastLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
    }

    private fun saveFolderUri(uri: Uri) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString(KEY_FOLDER_URI, uri.toString()).apply()
    }

    private fun saveSlideDuration(durationMillis: Long) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putLong(KEY_SLIDE_DURATION, durationMillis).apply()
    }

    fun saveFitMode(mode: FitModeNames) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString(KEY_FIT_MODE, mode.name).apply()
    }

    private fun saveShowClock(show: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_CLOCK, show).apply()
    }

    private fun saveIs24HourFormat(is24: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_24_HOUR_FORMAT, is24).apply()
    }

    private fun saveShowWeather(show: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_WEATHER, show).apply()
    }

    private fun saveUnitIsFahrenheit(isFahr: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_WEATHERUNITISFAHRENHEIT, isFahr).apply()
    }

    private fun saveShowWeatherEffects(show: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_SHOW_WEATHER_EFFECTS, show).apply()
    }

    fun saveTransitionType(type: TransitionType) {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        prefs.edit().putString(KEY_TRANSITION_EFFECT, type.name).apply()
    }

    private fun loadImageUrisFromFolder(uri: Uri, onLoaded: (List<Uri>) -> Unit) {
        val imageList = mutableListOf<Uri>()
        val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )

        contentResolver.query(
            childrenUri,
            arrayOf(
                DocumentsContract.Document.COLUMN_DOCUMENT_ID,
                DocumentsContract.Document.COLUMN_MIME_TYPE
            ),
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val docId = cursor.getString(0)
                val mimeType = cursor.getString(1)
                if (mimeType.startsWith("image/")) {
                    val documentUri = DocumentsContract.buildDocumentUriUsingTree(uri, docId)
                    imageList.add(documentUri)
                }
            }
        }

        onLoaded(imageList)
    }
}