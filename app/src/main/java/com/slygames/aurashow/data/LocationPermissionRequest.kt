// File: ui/permissions/LocationPermissionRequest.kt

package com.slygames.aurashow.ui.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionRequest(
    onPermissionGranted: () -> Unit
) {
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    when {
        locationPermissionState.status.isGranted -> {
            onPermissionGranted()
        }
        locationPermissionState.status.shouldShowRationale -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Location permission is required to fetch local weather.")
                Spacer(Modifier.height(8.dp))
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
        else -> {
            // Initial permission request in progress or denied permanently
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Please grant location permission.")
            }
        }
    }
}
