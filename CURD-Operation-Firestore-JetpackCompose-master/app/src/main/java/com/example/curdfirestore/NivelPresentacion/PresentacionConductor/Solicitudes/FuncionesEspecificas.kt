package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun obtenerFechaEnFormato(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val fechaActual = Date()
    return dateFormat.format(fechaActual)
}

@Composable
fun CoordenadasToAdress(CoordenadaRec:String): String{
    var markerLatO by remember { mutableStateOf(0.0) }
    var markerLonO by remember { mutableStateOf(0.0) }

    val markerCoordenadasLatLngO = convertirStringALatLng(CoordenadaRec)

    if (markerCoordenadasLatLngO != null) {
        markerLatO = markerCoordenadasLatLngO.latitude
        markerLonO = markerCoordenadasLatLngO.longitude
        //println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
    } else {
        println("Error al convertir la cadena a LatLng")
    }

    var dire= convertCoordinatesToAddress(coordenadas = LatLng(markerLatO, markerLonO))
    return dire
}