package com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.Pasajeros.GuardarSolicitud
import com.example.curdfirestore.R
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.obtenerFechaEnFormato
import com.example.curdfirestore.NivelAplicacion.TextInMarker
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelAplicacion.HorarioData
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.Pasajeros.RegistraSolicitudHorario
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.google.android.gms.maps.model.LatLng

//Agregado el 11/12/2023
@Composable
fun VentanaNoFound(
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
                    text = "Resultados",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento no hay ningún viaje que coincida con tu horario, " +
                            "puedes intentarlo de nuevo más adelante",
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
                        painter = painterResource(id = R.drawable.triste),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {

                        navController.navigate("home_viaje_pasajero/$email")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}
@Composable
fun VentanaLejos(
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
                    text = "Resultados",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Lo sentimos, en este momento no hay paradas cercanas. Puedes volver a intentarlo más" +
                            " adelante",
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
                        painter = painterResource(id = R.drawable.triste),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {

                        navController.navigate("home_viaje_pasajero/$email")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}
@Composable
fun VentanaMarker(
    navController: NavController,
    email:String,
    parada: ParadaData,
    horario: HorarioData,
    horarioId:String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    var boton by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }
    var enviado by remember { mutableStateOf(false) }

    var ejecutado by remember { mutableStateOf(false) }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier
                .background(Color.White)
                .padding(10.dp)) {


                var markerLatO by remember { mutableStateOf(0.0) }
                var markerLonO by remember { mutableStateOf(0.0) }
                val markerCoordenadasLatLngO = convertirStringALatLng(parada.par_ubicacion)

                if (markerCoordenadasLatLngO != null) {
                    markerLatO = markerCoordenadasLatLngO.latitude
                    markerLonO = markerCoordenadasLatLngO.longitude
                    // Hacer algo con las coordenadas LatLng
                    println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
                } else {
                    // La conversión falló
                    println("Error al convertir la cadena a LatLng")
                }

                val addressP = convertCoordinatesToAddress(LatLng(markerLatO, markerLonO))


                if (parada.par_nombre == "Origen") {

                    Text(
                        text = "Punto de partida",
                        modifier = Modifier.padding(2.dp),

                        style = TextStyle(
                            Color(137, 13, 88),
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center

                    )

                        TextInMarker(Label ="Dirección: ", Text = addressP)
                   // TextInMarker(Label ="Trayecto: ", Text = "${horario.horario_trayecto} hrs")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material.Button(
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
                            androidx.compose.material.Text(
                                text = "Cerrar", style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }

                } else if (parada.par_nombre == "Destino") {

                    Text(
                        text = "Punto de llegada",
                        modifier = Modifier.padding(2.dp),

                        style = TextStyle(
                            Color(137, 13, 88),
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center

                    )

                        TextInMarker(Label ="Dirección: ", Text = addressP)


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        androidx.compose.material.Button(
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
                            androidx.compose.material.Text(
                                text = "Cerrar", style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }

                } else {

                    //---
                    Text(
                        text = "Información de la parada",
                        modifier = Modifier.padding(2.dp),

                        style = TextStyle(
                            Color(137, 13, 88),
                            fontSize = 18.sp
                        ),
                        textAlign = TextAlign.Center

                    )



                    if (enviado == false) {

                        TextInMarker(Label = "Parada: ", Text = parada.par_nombre)
                        if (horario.horario_trayecto == "0") {
                            TextInMarker(Label = "Origen: ", Text = "UPIITA-IPN")
                            TextInMarker(Label ="Destino: ", Text = addressP)
                            TextInMarker(Label ="Horario aproximada: ", Text = "${parada.par_hora} hrs")

                        } else {
                            TextInMarker(Label ="Origen: ", Text = addressP)
                            TextInMarker(Label = "Destino: ", Text = "UPIITA-IPN")
                            TextInMarker(Label ="Horario aproximada: ", Text = "${parada.par_hora} hrs")

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
                                androidx.compose.material.Button(
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
                                    androidx.compose.material.Text(
                                        text = "Cerrar", style = TextStyle(
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                    )
                                }

                                androidx.compose.material.Button(
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            137,
                                            13,
                                            88
                                        )
                                    ),
                                    onClick = {
                                        //Guardar en la bd
                                        boton = true
                                        //  onDismiss()


                                        // navController.navigate("home_viaje_pasajero/$email")

                                    }) {
                                    androidx.compose.material.Text(
                                        text = "Solicitar", style = TextStyle(
                                            fontSize = 15.sp,
                                            color = Color.White
                                        )
                                    )
                                }

                            }
                        }

                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {


                            Text(
                                text = "Tu solicitud ha sido enviada. Te avisaremos cuando el conductor la acepte",
                                modifier = Modifier.padding(2.dp),

                                style = TextStyle(
                                    Color.Black,
                                    fontSize = 15.sp
                                ),
                                textAlign = TextAlign.Center


                            )



                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                androidx.compose.material.Button(
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            233,
                                            168,
                                            219
                                        )
                                    ),
                                    onClick = {
                                        // Cerrar el diálogo cuando se hace clic en "Cerrar"
                                        navController.navigate(route = "ver_itinerario_pasajero/$email")

                                    }) {
                                    androidx.compose.material.Text(
                                        text = "Cerrar", style = TextStyle(
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
            //--
        }
    }

    if (boton==true && ejecutado==false){
        val fecha_now= obtenerFechaEnFormato().toString()


        val solicitudData = SolicitudData(
            viaje_id = parada.viaje_id,
            horario_id = horarioId,
            conductor_id =parada.user_id ,
            pasajero_id=email,
            parada_id = parada.par_id,
            horario_trayecto = horario.horario_trayecto,
            solicitud_status = "Pendiente",
            solicitud_date = fecha_now,

        )


        GuardarSolicitud(navController, email, solicitudData,horarioId ) //Registra la solicitud en la BD
        RegistraSolicitudHorario(navController = navController, userId = email, horarioId =horarioId , status = "Si") //Actauliza horario_solicitud
        //GuardarCoordenadas(navController, userid,viajeData)
        ejecutado=true
        enviado=true

    }


}



@Composable
fun VentanaPreviaParadas(
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
                    text = "¡Tu viaje obtuvo resultados!",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        Color(137, 13, 88),
                        fontSize = 18.sp
                    ),
                    textAlign = TextAlign.Center

                )

                Text(
                    text = "Te mostramos las paradas cercanas. Puedes seleccionar alguna y solicitarla",
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
                        painter = painterResource(id = R.drawable.cheque),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                     //navController.navigate("ver_itinerario_pasajero/$email")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}