package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.InicioViajes


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.curdfirestore.NivelAplicacion.Conductor.GuardarViaje
import com.example.curdfirestore.NivelAplicacion.SearchBar
import com.example.curdfirestore.NivelAplicacion.searchPlaces
import com.example.curdfirestore.R
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.maxh
import com.example.curdfirestore.NivelAplicacion.ViajeData
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelAplicacion.obtenerUbicacion
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaRechazarUbicacion
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios.mapaMarker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IniciarViajeCon(
    navController: NavController,
    userid:String,

) {

    //Obtener ubicacion
    var controlador by remember {
        mutableStateOf(false)
    }
    var ubicacion by remember {
        mutableStateOf("")
    }

    BoxWithConstraints {
        maxh = this.maxHeight
    }

    var latitud by remember {
        mutableStateOf("")
    }
    var longitud by remember {
        mutableStateOf("")
    }

    var pasarlatitud by remember {
        mutableStateOf("")
    }
    var pasarlongitud by remember {
        mutableStateOf("")
    }

    var inicial by remember {
        mutableStateOf("si")
    }
    var valorMapa: String by remember { mutableStateOf("barra") } //El que regresa



    var ubiMarker  by remember { mutableStateOf("19.3898164,-99.11023") }


    var ejecutado by remember { mutableStateOf(false) }
    var boton by remember { mutableStateOf(false) }
    var launch by remember { mutableStateOf(true) }
    var primeraVez by remember {
        mutableStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxh)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                //.height(maxh - 30.dp),
            ) {

                    println("Ejecuacion numero $primeraVez")


                    var selectedPlace by remember { mutableStateOf<Place?>(null) }

                        var show by remember {
                            mutableStateOf(false)
                        }
                        val context = LocalContext.current
                        val fusedLocationClient: FusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(context)





                LaunchedEffect(true) {
                    while (true) {
                        launch=true

                        delay(3000)
                    }
                }

                if (launch == true) {
                    obtenerUbicacion { nuevaUbicacion ->
                        ubicacion = nuevaUbicacion
                    }
                    launch=false

                }
println("---OJO UBICACION $ubicacion")


                        if(ubicacion!=""){
                            val markerCoordenadasLatLng = convertirStringALatLng(ubicacion)
                            var miUbic by remember {
                                mutableStateOf(LatLng(0.0, 0.0))
                            }
                            println("Coordenadas Location----- $ubicacion")

                            if (markerCoordenadasLatLng != null) {
                                var markerLat = markerCoordenadasLatLng.latitude
                                var markerLon = markerCoordenadasLatLng.longitude
                                miUbic = LatLng(markerLat, markerLon)
                                // Hacer algo con las coordenadas LatLng
                                println("Latitud: ${markerCoordenadasLatLng.latitude}, Longitud: ${markerCoordenadasLatLng.longitude}")
                            } else {
                                // La conversión falló
                                println("Error al convertir la cadena a LatLng")
                            }

                            var direccion by remember {
                                mutableStateOf("")
                            }

                            var markerState = rememberMarkerState(position = miUbic)
                            direccion= convertCoordinatesToAddress(coordenadas = miUbic)
                            var cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(markerState.position, 17f)
                            }

                            latitud = markerState.position.latitude.toString()
                            longitud = markerState.position.longitude.toString()

                            selectedPlace?.let { place ->
                                markerState = rememberMarkerState(
                                    position = LatLng(
                                        place.latLng.latitude,
                                        place.latLng.longitude
                                    )
                                )
                                pasarlatitud = place.latLng.latitude.toString()
                                pasarlongitud = place.latLng.longitude.toString()


                                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                    LatLng(
                                        place.latLng.latitude,
                                        place.latLng.longitude
                                    ), 17f
                                )
                                direccion=convertCoordinatesToAddress(LatLng(       place.latLng.latitude,
                                    place.latLng.longitude))
                            }
                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxSize(),
                                cameraPositionState = cameraPositionState
                            ) {

                                Marker(
                                    state = markerState,
                                    title = "Origen",
                                    snippet = "Ubicación: $direccion",
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.marcador),
                                )

                            }
                        }








                //Botones
                Column(  modifier = Modifier
                    .fillMaxWidth()

                    .offset(y = maxh - 130.dp),
                    horizontalAlignment = Alignment.Start
                )
                {

                    Column(
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(25.dp, 15.dp)

                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .size(25.dp) // Tamaño del botón
                                .background(
                                    Color(137, 13, 88),
                                    shape = CircleShape
                                )
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


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                        //.offset( y = maxh -80.dp)
                        ,
                        horizontalAlignment = Alignment.Start,
                    ) {

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(137, 13, 88),
                            ),
                            onClick = {
                                var ubicacionF = "$pasarlatitud,$pasarlongitud"
                                boton = true
                                // navController.navigate(route = "registrar_destino_pasajero/$userid/$dia/$horao/$ubicacionF")
                                //  navController.navigate(route = "registrar_viajed_conductor/$userid/$dia/$horao/$horad/$ubicacionF")
                            }) {
                            Text(text = "Siguiente")
                        }
                    }

                }

            }
        }

    }

    /*if (boton==true && ejecutado==false){
        var ubicacion=""
        if(valorMapa=="barra"){
            ubicacion="$pasarlatitud,$pasarlongitud"
        }
        else{
            ubicacion=ubiMarker
        }
        /*val viajeData = ViajeData(
           viaje_origen = "$latitud,$longitud"
         )*/
        // val ubicacion="$pasarlatitud,$pasarlongitud"
        val destino="19.5114059,-99.1265259" //Coordenadas de UPIITA
        val viajeData = ViajeData(
            usu_id = userid,
            viaje_dia = dia,
            viaje_hora_partida = horao,
            viaje_origen = ubicacion,
            viaje_hora_llegada = horad,
            viaje_destino = destino,
            viaje_trayecto = "1",
            viaje_status = "Disponible",
            viaje_num_lugares = lugares
        )

        GuardarViaje(navController, userid, viajeData)
        //GuardarCoordenadas(navController, userid,viajeData)
        ejecutado=true
    }
*/
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun RegistrarViajeConductorPreview() {
    // Esta función se utiliza para la vista previa
    var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()
    IniciarViajeCon(navController = navController, correo,
     )

}









