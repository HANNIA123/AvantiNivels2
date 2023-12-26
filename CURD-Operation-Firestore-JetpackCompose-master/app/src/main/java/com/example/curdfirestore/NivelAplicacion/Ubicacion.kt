package com.example.curdfirestore.NivelAplicacion

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaRechazarUbicacion
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable

fun obtenerUbicacion(onUbicacionObtenida: (String) -> Unit) {
    var show by remember {
        mutableStateOf(false)
    }
    // Comprueba y solicita permisos de ubicación
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Comprueba y solicita permisos de ubicación
    // Comprueba y solicita permisos de ubicación
    DisposableEffect(context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
        onDispose { /* Cleanup */ }
    }


    fusedLocationClient.lastLocation
        .addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val lastKnownLocation = task.result
                // Utiliza lastKnownLocation
            } else {
                Log.w(ContentValues.TAG, "Failed to get location.")
            }
        }



    // ...

    // Obtiene la última ubicación conocida
    fusedLocationClient.lastLocation
        .addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val lastKnownLocation = task.result
                val nuevaUbicacion = "${lastKnownLocation.latitude},${lastKnownLocation.longitude}"
                println("Mi ubicación: Latitude: ${lastKnownLocation.latitude}, Longitude: ${lastKnownLocation.longitude}")
                Log.d(TAG, "Latitude: ${lastKnownLocation.latitude}, Longitude: ${lastKnownLocation.longitude}")
                // Llama a la función de devolución de llamada con la nueva ubicación
                onUbicacionObtenida(nuevaUbicacion)
            } else {
                show=true
                Log.w(TAG, "Failed to get location.")
            }
        }
}


