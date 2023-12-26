package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.curdfirestore.NivelAplicacion.Conductor.GuardarReporte
import com.example.curdfirestore.NivelAplicacion.LineaGris
import com.example.curdfirestore.NivelAplicacion.ReporteData
import com.example.curdfirestore.NivelAplicacion.TexItinerario
import com.example.curdfirestore.NivelAplicacion.TextViaje
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelAplicacion.eliminarParada
import com.example.curdfirestore.NivelAplicacion.eliminarViaje
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.maxh
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelAplicacion.eliminarSolicitud_actualizarHorario
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.obtenerFechaEnFormato
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerItinerarioConductor(
    navController: NavController,
    userId: String,
    viajes: List<ViajeDataReturn>
) {
    val pantalla="itinerario"

    BoxWithConstraints {
        maxh = this.maxHeight - 50.dp
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(45.dp)) {
                pruebaMenu(navController, userId)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxh)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {

                TituloPantalla(Titulo = "Itinerario", navController,)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    var nlun = 0
                    var nmar = 0
                    var nmier = 0
                    var njue = 0
                    var nvie = 0
                    var nsab = 0
                    var ndom = 0
                    var displayedDays = 0

                    for (viaje in viajes) {


                        val sortedViajes = listOf(
                            "Lunes",
                            "Martes",
                            "Miércoles",
                            "Jueves",
                            "Viernes",
                            "Sábado",
                            "Domingo"
                        )
                            .sortedBy { day ->
                                when (day) {
                                    "Lunes" -> 1
                                    "Martes" -> 2
                                    "Miércoles" -> 3
                                    "Jueves" -> 4
                                    "Viernes" -> 5
                                    "Sábado" -> 6
                                    "Domingo" -> 7
                                    else -> 8 // Set a default value for days not specified
                                }
                            }

                        if (displayedDays == 0) {
                            sortedViajes.forEach { day ->

                                viajes.filter { it.viaje_dia == day }.forEach { viaje ->

                                    //Convertir String a coordenadas  -- ORIGEN

                                    var markerLatO by remember { mutableStateOf(0.0) }
                                    var markerLonO by remember { mutableStateOf(0.0) }

                                    val markerCoordenadasLatLngO =
                                        convertirStringALatLng(viaje.viaje_origen)

                                    if (markerCoordenadasLatLngO != null) {
                                        markerLatO = markerCoordenadasLatLngO.latitude
                                        markerLonO = markerCoordenadasLatLngO.longitude
                                    } else {
                                        println("Error al convertir la cadena a LatLng")
                                    }

                                    //-------------DESTINO-------------------------------------------

                                    var markerLatD by remember { mutableStateOf(0.0) }
                                    var markerLonD by remember { mutableStateOf(0.0) }

                                    val markerCoordenadasLatLngD =
                                        convertirStringALatLng(viaje.viaje_destino)

                                    if (markerCoordenadasLatLngD != null) {
                                        markerLatD = markerCoordenadasLatLngD.latitude
                                        markerLonD = markerCoordenadasLatLngD.longitude
                                    } else {
                                        println("Error al convertir la cadena a LatLng")
                                    }

                                    val coord_origen = LatLng(markerLatO, markerLonO)
                                    val origen = convertCoordinatesToAddress(coord_origen)

                                    val coord_destino = LatLng(markerLatD, markerLonD)
                                    val destino = convertCoordinatesToAddress(coord_destino)


                                    if (viaje.viaje_dia == "Lunes") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {

                                            if (nlun == 0) {
                                                Text(
                                                    text = "Lunes",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            nlun++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------


                                    if (viaje.viaje_dia == "Martes") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (nmar == 0) {
                                                Text(
                                                    text = "Martes",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            nmar++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------

                                    if (viaje.viaje_dia == "Miércoles") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (nmier == 0) {
                                                Text(
                                                    text = "Miércoles",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            nmier++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------

                                    if (viaje.viaje_dia == "Jueves") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (njue == 0) {
                                                Text(
                                                    text = "Jueves",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            njue++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------

                                    if (viaje.viaje_dia == "Viernes") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (nvie == 0) {
                                                Text(
                                                    text = "Viernes",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            nvie++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------


                                    if (viaje.viaje_dia == "Sábado") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (nsab == 0) {
                                                Text(
                                                    text = "Sábado",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            nsab++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------

                                    if (viaje.viaje_dia == "Domingo") {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                        ) {
                                            if (ndom == 0) {
                                                Text(
                                                    text = "Domingo",
                                                    style = TextStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(137, 13, 88),
                                                        fontSize = 18.sp
                                                    )
                                                )
                                            }
                                            ndom++

                                            crearItinerario(navController, viaje, userId, origen, destino, pantalla)

                                        }
                                    } ///------------

                                }


                            }
                            displayedDays++
                        }
                    } //TERMINA FOR

                }
            }

        }
    }
}


@Composable
fun VentanaEliminarViaje(
    navController: NavController,
    userId: String,
    viaje_id: String,
    viaje_status: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    var showSecondDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text("Eliminar viaje")
            },
            text = {
                Text("¿Estas seguro de eliminar este viaje?")
            },
            confirmButton = {
                TextButton(onClick = {

                    val documentId= viaje_id
                    eliminarViaje(documentId)
                    eliminarParada(documentId)
                    eliminarSolicitud_actualizarHorario(documentId)
                    onDismiss()
                    showSecondDialog = true

                }) {
                    Text("Aceptar ")
                    Text("${viaje_id}")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("Cerrar")
                }
            }
        )
    }

    if (showSecondDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text("Viaje eliminado exitosamente")
            },
            confirmButton = {
                TextButton(onClick = {
                    showSecondDialog = false
                    navController.navigate("ver_itinerario_conductor/$userId")
                }) {
                    Text("Cerrar")
                }
            }
        )
    }
}



@Composable
fun VentanaCancelarViaje(
    navController: NavController,
    userId: String,
    viaje_id: String,
    viaje_status: String,
    showDialogCancel: Boolean,
    onDismiss: () -> Unit,
) {
    if (showDialogCancel) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(if (viaje_status == "Disponible") "Cancelar viaje" else "Activar viaje")
            },
            text = {
                Text(if (viaje_status == "Disponible") "¿Estás seguro de cancelar este viaje?. El status de tu viaje cambiará a No disponible hasta que vuelvas a activarlo." else "¿Estás seguro de activar este viaje?")
            },
            confirmButton = {
                TextButton(onClick = {
                    val documentIdUpdate = viaje_id

                    val nuevoStatus = if (viaje_status == "Disponible") {
                        "Cancelado"

                     /*   //NOTIFICACIÓN
                        val fecha_now= obtenerFechaEnFormato().toString()
                        val id_viaje = "$viaje_id"
                        val notificacionesData = NotificacionesData(
                            conductor_id = userId,
                            pasajero_id = "",
                            doc_id = id_viaje,
                            not_fecha = fecha_now,
                            not_token =  "f0Wun6e4Q9a-AFwQfeoTCQ:APA91bF4izBxUkhPrvN2RdZV5aP8l_VVtdz_PUVPPMWUpK29yLBcoKfW0V7JjtMDmYyYl6InFlZ1zpplutU8Nj2tFZwqvv5dbjZ_qF75IdrCEYuDZzbRW24ljfFMrGvezooSoA8BL1g4",
                            not_tipo = "Cancelación de viaje",
                        )

                        LaunchedEffect(Unit) {
                            // Realizar la acción de guardar notificación fuera del contexto @Composable
                            GuardarNotificación(navController, notificacionesData)
                        }*/


                    } else {
                        "Disponible"
                    }

                    actualizarStatusViaje(documentIdUpdate, "viaje_status", nuevoStatus)
                    navController.navigate("ver_itinerario_conductor/$userId")
                }) {
                    Text("Aceptar ")
                    Text("${viaje_id}")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("Cerrar")
                }
            }
        )

    }
}



fun actualizarStatusViaje(documentId: String, campo: String, valor: Any) {
    try {
        val db = FirebaseFirestore.getInstance()
        db.collection("viaje").document(documentId).update(campo, valor)
            .addOnSuccessListener {
                println("Documento con ID $documentId actualizado correctamente de Firestore.")
            }
            .addOnFailureListener { exception ->
                println("Error al intentar actualizar el documento de Firestore: $exception")
            }
    } catch (e: Exception) {
        println("Excepción al intentar actualizar el documento de Firestore: $e")
    }
}


@Composable
fun crearItinerario(
    navController: NavController,
    viaje: ViajeDataReturn,
    userId: String,
    origen: String,
    destino: String,
    pantalla: String) {
    if (viaje.viaje_trayecto == "1") {
        TexItinerario(
            Label = "Origen:",
            Text = "${origen}"
        )
        TexItinerario(
            Label = "Destino:",
            Text = "UPIITA-IPN"
        )
        TexItinerario(
            Label = "Hora de llegada: ",
            Text = "${viaje.viaje_hora_partida}"
        )
        TexItinerario(
            Label = "Lugares: ",
            Text = "${viaje.viaje_num_lugares}"
        )

        if (viaje.viaje_status == "Cancelado") {
            TextViaje(
                Text = "Has cancelado este viaje"
            )
        } else {
            // No hay contenido en el bloque else, por lo que el texto estará oculto
        }

    } else {
        TexItinerario(
            Label = "Origen:",
            Text = "UPIITA-IPN"
        )
        TexItinerario(
            Label = "Destino:",
            Text = "${destino}"
        )
        TexItinerario(
            Label = "Hora de partida: ",
            Text = "${viaje.viaje_hora_partida}"
        )
        TexItinerario(
            Label = "Lugares: ",
            Text = "${viaje.viaje_num_lugares}"
        )
        if (viaje.viaje_status == "Cancelado") {
            TextViaje(
                Text = "Has cancelado este viaje"
            )
        } else {
            // No hay contenido en el bloque else, por lo que el texto estará oculto
        }
    }

    //-----------BOTON PARA AGREGAR PARADA--------------------------------
    Row(
        modifier = Modifier.padding(start = 90.dp) ,
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(
            onClick = {
                navController.navigate(route = "nueva_parada/${viaje.viaje_id}/$userId")
            },
            ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Parada",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }
    }


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            //.align(Alignment.CenterHorizontally)
            .fillMaxWidth()
    ) {

        //-----------BOTON PARA VER VIAJE--------------------------------
        TextButton(onClick = {
            navController.navigate("ver_viaje/${viaje.viaje_id}/$userId/$pantalla")
        }

        ) {
            Text(
                text = "Ver",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }


        //-----------BOTON PARA CANCELAR VIAJE--------------------------------
        var showDialogCancel by remember {
            mutableStateOf(
                false
            )
        }

        TextButton(
            onClick = {
                if (viaje.viaje_status == "Disponible") {
                    showDialogCancel = true
                } else {
                    showDialogCancel = true
                }

            }) {
            Text(
                text = if (viaje.viaje_status == "Disponible") "Cancelar" else "Activar",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }

        VentanaCancelarViaje(
            navController,
            userId,
            viaje.viaje_id,
            viaje.viaje_status,
            showDialogCancel,
            { showDialogCancel = false })


        //------------BOTON PARA ELIMINAR---------------------------------------
        var showDialog by remember {
            mutableStateOf(
                false
            )
        }

        TextButton(onClick = {
            showDialog = true
        }) {
            Text(
                text = "Eliminar",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }

        VentanaEliminarViaje(
            navController,
            userId,
            viaje.viaje_id,
            viaje.viaje_status,
            showDialog,
            { showDialog = false })

    }
    LineaGris()
}

/*
@Composable
fun GuardarNotificacionComposable(navController: NavController, notificacionesData: NotificacionesData) {
    GuardarNotificación(navController, notificacionesData)
}
*/