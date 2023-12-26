package com.example.curdfirestore.NivelPresentacion.PresentacionConductor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.ApiService
import com.example.curdfirestore.NivelAplicacion.BASE_URL
import com.example.curdfirestore.NivelAplicacion.CoilImage
import com.example.curdfirestore.NivelAplicacion.Pasajeros.GuardarSolicitud
import com.example.curdfirestore.R
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.obtenerFechaEnFormato
import com.example.curdfirestore.NivelAplicacion.TextInMarker
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelAplicacion.HorarioData
import com.example.curdfirestore.NivelAplicacion.LineaGris
import com.example.curdfirestore.NivelAplicacion.MarkerItiData
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.RetrofitClient
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelAplicacion.UserData
import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun VentanaMarkerItinerario(
    navController: NavController,
    email:String,
    marker: MarkerItiData,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    var boton by remember { mutableStateOf(false) }

    var ejecutado by remember { mutableStateOf(false) }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {
                Text(
                    text = "Información",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        Color(137, 13, 88),
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center

                )

                val addressP = convertCoordinatesToAddress(marker.marker_ubicacion)


                TextInMarker(Label = "Nombre: ", Text = marker.marker_titulo)
                TextInMarker(Label = "Ubicación: ", Text = addressP)
                TextInMarker(Label = "Horario: ", Text = marker.marker_hora)

                if (marker.marker_titulo!="Origen" && marker.marker_titulo!="Destino" ){

                    //Consulta Usuario solo en paradas

                    var text by remember { mutableStateOf("") }
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val apiService = retrofit.create(ApiService::class.java)
                    //Obtener lista de viajes (Itinerario)
                    var busqueda by remember { mutableStateOf(false) }
                    var solicitudes by remember { mutableStateOf<List<SolicitudData>?>(null) }
                    LaunchedEffect(key1 = true) {
                        val response = RetrofitClient.apiService.obtenerSolicitudesParada(marker.marker_id)
                        try {
                            if (response.isSuccessful) {
                                solicitudes=response.body()
                            } else {
                                text="No se encontró ningún viaje que coincida con tu búsqueda"
                                busqueda=true
                            }
                        } catch (e: Exception) {
                            text = "Error al obtener Itinerario: $e"
                            println("Error al obtener Itinerario: $e")
                        }
                    }

                    if (solicitudes != null  && busqueda==false) {
                        LineaGris()
                        Text(
                            text = "Pasajeros",
                            modifier = Modifier.padding(2.dp),
                            style = TextStyle(
                                Color(137, 13, 88),
                                fontSize = 16.sp
                            ),

                            )
                        //con las solicitudes vamos a buscar a los pasajeros
                        for (solicitud in solicitudes!!){
                            if(solicitud.solicitud_status=="Aceptada"){
                                //Solo si es aceptada, obtenemos los datos de un usuario
                                var usuario by remember { mutableStateOf<UserData?>(null) }
                                var text by remember { mutableStateOf("") }
                                val retrofit = Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build()
                                val apiService = retrofit.create(ApiService::class.java)
                                LaunchedEffect(key1 = true) {
                                    try {
                                        val resultadoUsuario =
                                            RetrofitClient.apiService.pasarUsuario(solicitud.pasajero_id)
                                        usuario = resultadoUsuario
                                        // Haz algo con el objeto Usuario
                                        println("Usuario obtenido: $usuario")
                                    } catch (e: Exception) {
                                        text = "Error al obtener usuario: $e"
                                        println("Error al obtener usuario: $e")
                                    }
                                }

                                if (usuario!=null){
                                    //Mostrar nombre del pasajero y su foto
                                    Row(
                                        modifier=Modifier.padding(5.dp)
                                    ) {
                                        CoilImage(
                                            url = usuario!!.usu_foto, modifier = Modifier
                                                .size(70.dp)
                                                .clip(CircleShape)
                                        )
                                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(5.dp)){
                                            Text(
                                                buildAnnotatedString {
                                                    withStyle(
                                                        style = SpanStyle(
                                                            fontSize = 15.sp
                                                        )
                                                    ) {
                                                        append("${usuario!!.usu_nombre} ${usuario!!.usu_primer_apellido} ${usuario!!.usu_segundo_apellido}")
                                                    }


                                                }
                                            )
                                            Row(modifier = Modifier.clickable {
                                                navController.navigate("reportar_pasajero/${solicitud.pasajero_id}/$email")
                                            })
                                            {
                                                Icon(
                                                    imageVector = Icons.Filled.Warning,
                                                    contentDescription = "Reportar",
                                                    tint =   Color(104, 104, 104),

                                                )
                                                Spacer(modifier = Modifier.width(4.dp)) // Agrega espacio entre el icono y el texto
                                                Text(
                                                    text = "Reportar",
                                                    style = TextStyle(
                                                        fontSize = 13.sp,
                                                        color = Color(104, 104, 104),
                                                    ),
                                                    modifier = Modifier.align(Alignment.CenterVertically) // Alinea el texto verticalmente con el icono


                                                )

                                            }



                                        }

                                    }


                                }


                            }

                        }

                    }


                }



                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    // Primer botón para "Ver viaje"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    233,
                                    168,
                                    219
                                )
                            ),
                            onClick = {
                                // Cerrar el diálogo cuando se hace clic en "Cerrar"
                                onDismiss()
                            }) {
                            Text(
                                text = "Cerrar", style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    137,
                                    13,
                                    88
                                )
                            ),
                            onClick = {
                                //Guardar en la bd
                                boton=true
                                //  onDismiss()
                                // navController.navigate("home_viaje_pasajero/$email")

                            }) {
                            Text(
                                text = "Editar", style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }

                    }

                }
            }
        }
    }


}


//AGREGADO 19/12/23
@Composable
fun VentanaNoPasajeros(
    navController: NavController,
    userId:String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {
                Text(
                    text = "Pasajeros",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes pasajeros agregados a tus viajes registrados. ",
                    modifier = Modifier
                        .padding(2.dp),
                    style = TextStyle(
                        color = Color(104, 104, 104),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Justify,
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        painter = painterResource(id = R.drawable.users),
                        contentDescription = "Icono Pasajeros",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("home_viaje_conductor/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}

@Composable
fun VentanaNoItinearios(
    navController: NavController,
    userId:String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {
                Text(
                    text = "Itinearios",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes viajes registrados para ser mostrados en el itinerario. ",
                    modifier = Modifier
                        .padding(2.dp),
                    style = TextStyle(
                        color = Color(104, 104, 104),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Justify,
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Icono Itineario",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        tint = Color(137, 13, 88),
                    )
                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("home_viaje_conductor/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}

@Composable
fun VentanaNoSolicitudes(
    navController: NavController,
    userId:String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {
                Text(
                    text = "Solicitudes",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes solicitudes. Te avisaremos cuando recibas alguna. ",
                    modifier = Modifier
                        .padding(2.dp),
                    style = TextStyle(
                        color = Color(104, 104, 104),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Justify,
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        painter = painterResource(id = R.drawable.solicitud),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("home_viaje_conductor/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}


@Composable
fun VentanaRechazarUbicacion(
    navController: NavController,
    email:String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {
                Text(
                    text = "Acceso a la ubicación",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Para registar un viaje, es necesario acceder a tu ubicación",
                    modifier = Modifier
                        .padding(2.dp),
                    style = TextStyle(
                        color = Color(104, 104, 104),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Justify,
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        painter = painterResource(id = R.drawable.ubicacion),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {

                        navController.navigate("home_viaje_conductor/$email")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}



