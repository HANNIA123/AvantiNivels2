package com.example.curdfirestore.NivelAplicacion

import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.squareup.okhttp.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

@Composable
fun convertCoordinatesToAddress(coordenadas:LatLng): String {
    val latitud: Double = coordenadas.latitude
    val longitud: Double = coordenadas.longitude
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitud, longitud, 1)!!

    return if (addresses.isNotEmpty()) {
        val address = addresses[0]
        val addressLines = address.getAddressLine(0).split(", ")
        val firstTwoLines = addressLines.take(2).joinToString(", ")
        firstTwoLines

    } else {
        "No se encontraron direcciones para las coordenadas dadas"
    }
}

fun recortarDireccion(Direccion:String):String{
    // Obtener una lista de subcadenas separadas por comas
    val listaSubcadenas = Direccion.split(",")
    val primerosTresElementos = listaSubcadenas.take(3)
    // Convertir los primeros tres elementos a una cadena separada por comas
    val nuevaDireccion = primerosTresElementos.joinToString(",")

    return nuevaDireccion
}
@Composable
fun convertCoordinatesToPlaceName(coordenadas: LatLng): String {
    val latitud: Double = coordenadas.latitude
    val longitud: Double = coordenadas.longitude
    val context = LocalContext.current
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address> = geocoder.getFromLocation(latitud, longitud, 1)!!

    return if (addresses.isNotEmpty()) {
        val address = addresses[0]
        address.featureName ?: "Nombre de lugar no disponible"
    } else {
        "No se encontraron lugares para las coordenadas dadas"
    }
}
//Agregado 11/12/2023
@Composable
fun getDistance(coordenadas1:String, coordenadas2:String): Float {
    var Lat1 by remember { mutableStateOf(0.0) }
    var Lon1 by remember { mutableStateOf(0.0) }

    var Lat2 by remember { mutableStateOf(0.0) }
    var Lon2 by remember { mutableStateOf(0.0) }



    val markerCoordenadas1 = convertirStringALatLng(coordenadas1)
    if (markerCoordenadas1!= null) {
        Lat1 = markerCoordenadas1.latitude
        Lon1 = markerCoordenadas1.longitude
    }
    val markerCoordenadas2 = convertirStringALatLng(coordenadas2)
    if (markerCoordenadas2!= null) {
        Lat2 = markerCoordenadas2.latitude
        Lon2 = markerCoordenadas2.longitude
    }


    val lat1: Double = Lat1
    val lon1: Double = Lon1
    val lat2: Double = Lat2
    val lon2: Double = Lon2
    val location1 = Location("")
    location1.latitude = lat1
    location1.longitude = lon1

    val location2 = Location("")
    location2.latitude = lat2
    location2.longitude = lon2

    return location1.distanceTo(location2)
}


///Agregado--
// Función para calcular la distancia entre dos puntos utilizando la API de Google Maps
 fun calculateDistance(origin: String, destination: String, apiKey: String): Float {
    // Llama a la función correspondiente para obtener la distancia
    val distanceResult = getDistanceA(origin, destination, apiKey)
    // Analiza la respuesta y extrae la distancia
    val distance = distanceResult?.get("routes")?.asJsonArray?.get(0)?.asJsonObject
        ?.get("legs")?.asJsonArray?.get(0)?.asJsonObject
        ?.get("distance")?.asJsonObject?.get("value")?.asFloat ?: 0f

    return distance
}

// La función getDistance debe obtener la distancia entre dos puntos utilizando la API de Google Maps.
// Implementa esta función según tus necesidades y utiliza el apiKey proporcionado.
// Devuelve el resultado en formato JSON.
 fun getDistanceA(origin: String, destination: String, apiKey: String): JsonObject? {
    // Implementa la lógica para llamar a la API de Google Maps para obtener la distancia
    // y devuelve el resultado en formato JSON.
    // Puedes utilizar bibliotecas como Retrofit o HttpClient para realizar la solicitud HTTP.
    // Aquí un ejemplo ficticio:
    val url = "https://maps.googleapis.com/maps/api/directions/json?" +
            "origin=$origin&destination=$destination&key=$apiKey"
    val response = /* Lógica para realizar la solicitud HTTP */ null
    return /* Lógica para analizar la respuesta y devolver el resultado en formato JSON */ null
}
