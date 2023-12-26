package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.curdfirestore.R
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.ViajeData
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.curdfirestore.NivelAplicacion.MarkerItiData
import com.example.curdfirestore.NivelAplicacion.TextoMarker
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelAplicacion.calculateDistance
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaMarkerItinerario
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.maxh
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.VentanaMarker
import com.example.curdfirestore.screen.getDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerViajeConductor(
    navController: NavController,
    correo: String,
    viajeData: ViajeData,
    paradas: List<ParadaData>,
    pantalla:String,
    viajeId:String

) {
//Agregados
    var filterviajes by remember { mutableStateOf<List<MarkerItiData>?>(null) }

    var listaActual = filterviajes?.toMutableList() ?: mutableListOf()
    val paradasPorMarcador = mutableMapOf<String, MarkerItiData>()

    var infparadas by remember { mutableStateOf<MarkerItiData?>(null) }
    var show by rememberSaveable { mutableStateOf(false) }



    //Convertir String a coordenadas  -- origen

    var markerLatO by remember { mutableStateOf(0.0) }
    var markerLonO by remember { mutableStateOf(0.0) }

    val markerCoordenadasLatLngO = convertirStringALatLng(viajeData.viaje_origen)

    if (markerCoordenadasLatLngO != null) {
        markerLatO = markerCoordenadasLatLngO.latitude
        markerLonO = markerCoordenadasLatLngO.longitude
        // Hacer algo con las coordenadas LatLng
        println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
    } else {
        // La conversión falló
        println("Error al convertir la cadena a LatLng")
    }

//Destino
    var markerLatD by remember { mutableStateOf(0.0) }
    var markerLonD by remember { mutableStateOf(0.0) }

    val markerCoordenadasLatLngD = convertirStringALatLng(viajeData.viaje_destino)

    if (markerCoordenadasLatLngD != null) {
        markerLatD = markerCoordenadasLatLngD.latitude
        markerLonD = markerCoordenadasLatLngD.longitude
        // Hacer algo con las coordenadas LatLng
        //  println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
    } else {
        // La conversión falló
        println("Error al convertir la cadena a LatLng")
    }

//Lista de los markers
    val latLngs = mutableListOf<LatLng>()


    BoxWithConstraints {
        maxh = this.maxHeight
    }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxh)
            ) {
                val origen = LatLng(markerLatO, markerLonO)
                val destino = LatLng(markerLatD, markerLonD)

/*Aqui trabajando*/
                val nOrigen=MarkerItiData(
                    marker_ubicacion = origen,
                    marker_hora = viajeData.viaje_hora_partida,
                    marker_titulo = "Origen",
                    marker_id = viajeId
                )

                listaActual.add(nOrigen)


                val nDestino=MarkerItiData(
                    marker_ubicacion = destino,
                    marker_hora = viajeData.viaje_hora_llegada,
                    marker_titulo = "Destino",
                    marker_id = viajeId
                )

                listaActual.add(nDestino)

                /*Fin*/

                var markerPositions = listOf(
                  origen, // Marker 1
                   destino, // Marker 2

                )


                var paradasPositions = listOf<LatLng>()
                var titlesPositions = listOf(
                    "Origen", "Destino"

                )
                var horaPositions = listOf(
                    viajeData.viaje_hora_partida,
                    viajeData.viaje_hora_llegada
                )


                var nparadas = paradas.sortedBy { it.par_hora }
println("Paradas en desorden $paradas")
                println("Paradas por horario $nparadas")

                for (parada in nparadas) {
                    var markerLat by remember { mutableStateOf(0.0) }
                    var markerLon by remember { mutableStateOf(0.0) }
                    val markerCoordenadasLatLng = convertirStringALatLng(parada.par_ubicacion)

                    if (markerCoordenadasLatLng != null) {
                        markerLat = markerCoordenadasLatLng.latitude
                        markerLon = markerCoordenadasLatLng.longitude
                        // Hacer algo con las coordenadas LatLng
                        // println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
                    } else {
                        // La conversión falló
                        println("Error al convertir la cadena a LatLng")
                    }
                    val ubiParada = LatLng(markerLat, markerLon)
                    val nParada = MarkerItiData(
                        marker_ubicacion = ubiParada,
                        marker_hora = parada.par_hora,
                        marker_titulo = parada.par_nombre,
                        marker_id = parada.par_id
                    )

                    listaActual.add(nParada)

                    markerPositions = markerPositions + ubiParada

                    paradasPositions = paradasPositions + ubiParada
                    titlesPositions = titlesPositions + parada.par_nombre
                    horaPositions = horaPositions + parada.par_hora
                }


                    MapViewContainer { googleMap: GoogleMap ->
                    // Habilita los controles de zoom
                   // googleMap.uiSettings.isZoomControlsEnabled = true
                    // Agrega los marcadores

                        for (newMarker in listaActual){
                            val markerOptions = MarkerOptions().position(newMarker.marker_ubicacion)
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador))
                            val marker = googleMap.addMarker(markerOptions)
                            paradasPorMarcador[marker!!.id] = newMarker



                            // Agregar un evento de clic al marcador
                            googleMap.setOnMarkerClickListener { marker ->
                                // Aquí puedes manejar el evento de clic en el marcador
                                // Por ejemplo, puedes mostrar un mensaje Toast con el título del marcador
                                //Toast.makeText(context, "Has hecho clic en el marcador: ${marker.title}", Toast.LENGTH_SHORT).show()
                                val paradaSeleccionada = paradasPorMarcador[marker.id]
                                if (paradaSeleccionada != null) {
                                    infparadas = paradaSeleccionada
                                    show = true
                                }


                                true

                            }


                        }

                    // Obtiene y agrega la ruta entre los marcadores
                    GlobalScope.launch(Dispatchers.Main) {
                        val apiKey = "AIzaSyAZmpsa99qsen70ktUWCSDbmEChisRMdlQ"
                        // Calcula las distancias entre los puntos y ordénalos
                        val sortedPositions = paradasPositions.sortedBy { point ->
                            calculateDistance(
                                "${origen.latitude},${origen.longitude}",
                                "${point.latitude},${point.longitude}",
                                apiKey
                            )
                        }

                        // Construye la cadena de waypoints ordenada
                        println("ORdenadaaaaa $sortedPositions")
                        println("No ordenada $paradasPositions")


                        val waypointStrings = sortedPositions.map { "${it.latitude},${it.longitude}" }
                        // Concatena las cadenas de texto con '|' como separador
                        val waypointsString = waypointStrings.joinToString("|")


                        // Llama a la función getDirections con la lista de waypoints
                        val directions = getDirections(
                            origin = "${origen.latitude},${origen.longitude}",
                            destination = "${destino.latitude},${destino.longitude}",
                            waypointsString, apiKey)
                       println("Direction------ $directions")
                        // Agrega la ruta
                        val polylineOptions = PolylineOptions().addAll(directions)
                        googleMap.addPolyline(polylineOptions)
                        // Modifica el nivel de zoom del mapa
                        val cameraUpdate =
                            CameraUpdateFactory.newLatLngZoom(markerPositions[0], 13f)
                        googleMap.animateCamera(cameraUpdate)
                    }

                }

                if (show) {
                    VentanaMarkerItinerario(
                        navController,
                        correo,
                        infparadas!!,
                        show,
                        { show = false },
                        {})
                }


Row(verticalAlignment = Alignment.Top){

//Boton
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier= Modifier
            .padding(15.dp)
            .offset(x = 20.dp, y = 25.dp)

    ) {
        IconButton(
            onClick = {

                if (pantalla=="itinerario"){
                    navController.navigate(route = "ver_itinerario_conductor/$correo")

                }
                else{
                    navController.navigate(route = "home_viaje_conductor/$correo")

                }

            },
            modifier = Modifier
                .size(25.dp) // Tamaño del botón
                .background(Color(137, 13, 88), shape = CircleShape)

        ) {
            Icon(
                modifier = Modifier
                    .size(23.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Icono Cerrar",
                tint = Color.White

            )


        }
    }

    Column(
        modifier= Modifier
            .fillMaxWidth()
            .padding(30.dp, 15.dp, 30.dp, 5.dp)
            .border(1.dp, Color.LightGray)
            .background(Color.White),

    ){

        Column(
            modifier= Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.Center
        ){
            TextoMarker(Label = "Dia: ", Text = viajeData.viaje_dia )
            TextoMarker(Label = "Hora de partida: ", Text = "${viajeData.viaje_hora_partida} hrs" )
            TextoMarker(Label = "Hora de llegada", Text = "${viajeData.viaje_hora_llegada} hrs" )
        }



    }

}


            }





            }


//Datos del conductor





}

@Composable
fun MapViewContainer(mapReady: (GoogleMap) -> Unit) {
    val context = LocalContext.current
    AndroidView(
        factory = { context: Context ->
            MapView(context).apply {
                onCreate(Bundle())
                getMapAsync(mapReady)
            }
        },
        update = { mapView ->
            mapView.onResume()
        }
    )
}




/*
@Preview(showBackground = true)
@Composable
fun MyComposablePreviewVerViaje() {
    // Esta función se utiliza para la vista previa
    var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()

    val viajeData: ViajeData = ViajeData(/* provide constructor arguments here if needed */)
    val listaDeViajes: List<ParadaData> = listOf(
        ParadaData(/* provide constructor arguments here if needed */),

    )
    F_VerViajeConductor(navController = navController, correo = correo, viajeData = viajeData, listaDeViajes, pantalla = "itinerario", viajeId = "123")
}
*/
