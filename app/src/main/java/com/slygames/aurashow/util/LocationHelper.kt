package com.slygames.aurashow.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object LocationHelper {
    private var fusedLocationClient: FusedLocationProviderClient? = null

    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? = suspendCancellableCoroutine { cont ->
        val client = fusedLocationClient ?: return@suspendCancellableCoroutine cont.resume(null)
        client.lastLocation
            .addOnSuccessListener { location ->
                cont.resume(location)
            }
            .addOnFailureListener { exception ->
                cont.resumeWithException(exception)
            }
    }
}
