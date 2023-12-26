package com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.LineaGris
import com.example.curdfirestore.NivelAplicacion.TexItinerario
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.convertCoordinatesToAddress
import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng
import com.example.curdfirestore.NivelAplicacion.eliminarParada
import com.example.curdfirestore.NivelAplicacion.eliminarViaje
import com.example.curdfirestore.NivelAplicacion.pruebaMenuPasajero
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.maxh
import com.example.curdfirestore.NivelAplicacion.HorarioDataReturn
import com.example.curdfirestore.NivelAplicacion.TextViaje
import com.example.curdfirestore.NivelAplicacion.eliminarSolicitud_actualizarHorario
import com.example.curdfirestore.NivelAplicacion.eliminarViajeP
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoSolicitudes
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.VentanaCancelarViaje
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.VentanaEliminarViaje
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.actualizarStatusViaje
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.VentanaSinSolicitudes
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerItinerarioPasajeroSinSoli(
    navController: NavController,
    userId: String,
    viajes: List<HorarioDataReturn>,

    ) {
    var show by rememberSaveable { mutableStateOf(false) }
    BoxWithConstraints {
        maxh = this.maxHeight - 50.dp
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(45.dp)) {
                pruebaMenuPasajero(navController, userId)
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
                TituloPantalla(Titulo = "Viajes \n registrados", navController,)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    var nmar=0
                    var nlun=0
                    var nmier=0
                    var njue=0
                    var nvie=0
                    var nsab=0
                    var ndom=0
                    var displayedDays = 0


                    val Sol_Aceptada = viajes.all { it.horario_solicitud == "Si" }
                    if (Sol_Aceptada || viajes == null) {
                        show = true
                        VentanaSinSolicitudes(navController, userId, show, { show = false }, {})
                    } else {

                    for (viaje in viajes){

                        //------------------------------------------------------------------

                        val sortedViajes = listOf("Lunes", "Martes", "Miércoles","Jueves", "Viernes", "Sábado", "Domingo")
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

                        if (displayedDays==0) {
                            sortedViajes.forEach { day ->

                                viajes.filter { it.horario_dia == day }.forEach { viaje ->


                                        //Para filtrar si se envio o no la solicitud
                                    if(viaje.horario_solicitud=="No"){ //Solamente mostrar los viajes que no tienen solicitud

                                    //---------------VISUALIZAR------------------------------------------
                                    //Convertir String a coordenadas  -- ORIGEN

                                    var markerLatO by remember { mutableStateOf(0.0) }
                                    var markerLonO by remember { mutableStateOf(0.0) }

                                    val markerCoordenadasLatLngO = convertirStringALatLng(viaje.horario_origen)

                                    if (markerCoordenadasLatLngO != null) {
                                        markerLatO = markerCoordenadasLatLngO.latitude
                                        markerLonO = markerCoordenadasLatLngO.longitude
                                        //println("Latitud: ${markerCoordenadasLatLngO.latitude}, Longitud: ${markerCoordenadasLatLngO.longitude}")
                                    } else {
                                        println("Error al convertir la cadena a LatLng")
                                    }

                                    //-------------DESTINO-------------------------------------------

                                    var markerLatD by remember { mutableStateOf(0.0) }
                                    var markerLonD by remember { mutableStateOf(0.0) }

                                    val markerCoordenadasLatLngD = convertirStringALatLng(viaje.horario_destino)

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


                                    if (viaje.horario_dia == "Lunes")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------


                                    if (viaje.horario_dia == "Martes")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------

                                    if (viaje.horario_dia == "Miércoles")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------

                                    if (viaje.horario_dia == "Jueves")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------

                                    if (viaje.horario_dia == "Viernes")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)


                                        }
                                    } ///------------


                                    if (viaje.horario_dia == "Sábado")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------

                                    if (viaje.horario_dia == "Domingo")
                                    {
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

                                            crearItinerarioSin(navController, viaje, userId, origen, destino)

                                        }
                                    } ///------------

                                }


                            }
                            displayedDays++
                        }


                    }
                }}
            }
        }
    }
    }

    }


fun actualizarStatusViajeP(documentId: String, campo: String, valor: Any) {
    try {
        val db = FirebaseFirestore.getInstance()
        db.collection("horario").document(documentId).update(campo, valor)
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
fun VentanaEliminarViajeP(
    navController: NavController,
    userId: String,
    horario_id: String,
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

                    val documentId= horario_id
                    eliminarViajeP(documentId)
                    onDismiss()
                    showSecondDialog = true

                }) {
                    Text("Aceptar ")
                    //Text("${horario_id}")
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
                    navController.navigate("ver_itinerario_pasajero_sin/$userId")
                }) {
                    Text("Cerrar")
                }
            }
        )
    }
}



@Composable
fun VentanaCancelarViajeP(
    navController: NavController,
    userId: String,
    horario_id: String,
    horario_status: String,
    showDialogCancel: Boolean,
    onDismiss: () -> Unit,
) {
    if (showDialogCancel) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(if (horario_status == "Disponible") "Cancelar viaje" else "Activar viaje")
            },
            text = {
                Text(if (horario_status == "Disponible") "¿Estás seguro de cancelar este viaje?. El status de tu viaje cambiará a No disponible hasta que vuelvas a activarlo." else "¿Estás seguro de activar este viaje?")
            },
            confirmButton = {
                TextButton(onClick = {
                    val documentIdUpdate = horario_id

                    val nuevoStatus = if (horario_status == "Disponible") {
                        "Cancelado"

                    } else {
                        "Disponible"
                    }

                    actualizarStatusViajeP(documentIdUpdate, "horario_status", nuevoStatus)
                    navController.navigate("ver_itinerario_pasajero_sin/$userId")
                }) {
                    Text("Aceptar")
                    //Text("${horario_id}")

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

@Composable
fun crearItinerarioSin(
    navController: NavController,
    viaje: HorarioDataReturn,
    userId: String,
    origen: String,
    destino: String,
){
    //----------------VER DETALLES DEL VIAJE-----------------------

    if (viaje.horario_trayecto == "1") {
        TexItinerario(Label = "Origen:", Text = "${origen}")
        TexItinerario(
            Label = "Destino:",
            Text = "UPIITA-IPN"
        )
        TexItinerario(
            Label = "Hora de llegada: ",
            Text = "${viaje.horario_hora}"
        )
        if (viaje.horario_status == "Cancelado") {
            TextViaje(
                Text = "Has cancelado este viaje."
            )
        } else {
            // No hay contenido en el bloque else, por lo que el texto estará oculto
        }
    } else {
        TexItinerario(
            Label = "Origen:",
            Text = "UPIITA-IPN"
        )
        TexItinerario(Label = "Destino:", Text = "${destino}")

        TexItinerario(
            Label = "Hora de partida: ",
            Text = "${viaje.horario_hora}"
        )
        if (viaje.horario_status == "Cancelado") {
            TextViaje(
                Text = "Has cancelado este viaje"
            )
        } else {
            // No hay contenido en el bloque else, por lo que el texto estará oculto
        }

    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            //.align(Alignment.CenterHorizontally)
            .fillMaxWidth()
    ) {


        if (viaje.horario_solicitud == "Si") {
            //Boton para ver viaje
            TextButton(onClick = {
                //Ventana con detalles de la solicitud
                // navController.navigate("ver_paradas_pasajero/$userId/${viaje.horario_id}")
                // navController.navigate("ver_viaje/${viaje.viaje_id}/$userId/$pantalla")
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

        }else{
            //Boton para ver viaje
            TextButton(onClick = {
                navController.navigate("ver_paradas_pasajero/$userId/${viaje.horario_id}")
                // navController.navigate("ver_viaje/${viaje.viaje_id}/$userId/$pantalla")
            }

            ) {
                Text(
                    text = "Buscar paradas",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(104, 104, 104),
                    )
                )
            }
        }


        //---------BOTON PARA CANCELAR------------------------------------------
        var showDialogCancel by remember {
            mutableStateOf(
                false
            )
        }

        TextButton(
            onClick = {
                if (viaje.horario_status == "Disponible") {
                    println("STATUS")
                    println("${viaje.horario_status}")
                    showDialogCancel = true
                } else {
                    showDialogCancel = true
                    println("STATUS")
                    println("${viaje.horario_status}")
                }

            }) {
            Text(
                text = if (viaje.horario_status == "Disponible") "Cancelar" else "Activar",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }

        VentanaCancelarViajeP(
            navController,
            userId,
            viaje.horario_id,
            viaje.horario_status,
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

        VentanaEliminarViajeP(
            navController,
            userId,
            viaje.horario_id,
            showDialog,
            { showDialog = false })

        //---------------------------------------------------
    }
    LineaGris()


}