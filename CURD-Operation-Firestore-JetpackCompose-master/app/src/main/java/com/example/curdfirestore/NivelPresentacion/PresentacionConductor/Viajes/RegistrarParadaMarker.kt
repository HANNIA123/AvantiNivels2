package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.Conductor.GuardarParada
import com.example.curdfirestore.R
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.maxh
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrarParadaMarker(
    navController: NavController,
    userid:String,
    viajeid:String,
    nombrep:String,
    horap: String,
    ubicacionMarker:String
) {
    BoxWithConstraints {
        maxh = this.maxHeight
    }
    var ejecutado by remember { mutableStateOf(false) }
    var boton by remember { mutableStateOf(false) }


    var latitud by remember {
        mutableStateOf("")
    }
    var longitud by remember {
        mutableStateOf("")
    }
    var valorMapa: String? by remember { mutableStateOf("barra") }
    var show by rememberSaveable {mutableStateOf(false)}
    var TipoBusqueda: String by remember { mutableStateOf("barra") }

    val searchResults = remember { mutableStateOf(emptyList<Place>()) }
    val context = LocalContext.current
    val searchQuery = remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    var markerLat by remember { mutableStateOf(0.0) }
    var markerLon by remember { mutableStateOf(0.0) }

    val markerCoordenadasLatLng = convertirStringALatLng(ubicacionMarker)




    if (markerCoordenadasLatLng != null) {
        markerLat = markerCoordenadasLatLng.latitude
        markerLon = markerCoordenadasLatLng.longitude
        // Hacer algo con las coordenadas LatLng
        println("Latitud: ${markerCoordenadasLatLng.latitude}, Longitud: ${markerCoordenadasLatLng.longitude}")
    } else {
        // La conversión falló
        println("Error al convertir la cadena a LatLng")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxh - 10.dp),
            ) {


                //Empieza mapa

                val miUbic = LatLng(markerLat, markerLon)
                var markerState = rememberMarkerState(position = miUbic)
                var cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(markerState.position, 17f)
                }

                latitud = markerState.position.latitude.toString()
                longitud = markerState.position.longitude.toString()

                println("Tipo busqueda: $valorMapa")


                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {

                    Marker(
                        title = "Origen",
                        state = markerState,
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marcador),
                        snippet = "Punto de origen",
                        draggable = true
                    )

                }
                //Termina


                //Boton para regresar a la barra
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart).fillMaxWidth().padding(12.dp),
                ) {

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth().background(Color.White)
                            .border(
                                1.dp,
                                Color.LightGray
                            )
                            .padding(8.dp)
                            .clickable
                            {

                                var ubicacionMarkerDrag = "$latitud,$longitud"

                                navController.navigate(route = "registrar_parada_return/$userid/$viajeid/$nombrep/$horap/$ubicacionMarkerDrag")
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = Color(104, 104, 104),
                            modifier = Modifier.padding(5.dp)

                        )

                        Text(
                            text = "Buscar por dirección",
                            style = TextStyle(
                                color = Color(104, 104, 104),
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )

                    }


                }



                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .offset( y = maxh -80.dp)
                    ,
                    horizontalAlignment = Alignment.Start,
                ) {

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(137, 13, 88),
                        ),
                        onClick = {
                           // var ubicacionF = "$latitud,$longitud"
                            //navController.navigate(route = "registrar_destino_conductor/$userid/$dia/$horao/$horad/$ubicacionF")
                        boton=true
                            //navController.navigate(route = "registrar_viajed_conductor/$userid/$dia/$hora/$ubicacionF")
                        }) {
                        Text(text = "Siguiente")
                    }
                }

                MyDiaologExitosa(navController, userid,  viajeid,show,{show=false }, {})



                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.offset(x = 30.dp, y=-110.dp)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(25.dp) // Tamaño del botón
                            .background(Color(137, 13, 88), shape = CircleShape)

                    ) {
                        Icon(
                            modifier = Modifier
                                .size(23.dp),
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Icono atras",
                            tint = Color.White

                        )


                    }
                }



            }



            }

            //Barra de búsqueda

            //




        }

    //Guardar parada
    if (boton==true && ejecutado==false){
        val ubicacion="$latitud,$longitud"
        val paradaData = ParadaData(
            viaje_id = viajeid,
            par_nombre = nombrep,
            par_hora = horap,
            par_ubicacion = ubicacion,
            user_id = userid,
        )
        GuardarParada(navController = navController, paradaData = paradaData )

        ejecutado=true
        show=true
    }


    }












