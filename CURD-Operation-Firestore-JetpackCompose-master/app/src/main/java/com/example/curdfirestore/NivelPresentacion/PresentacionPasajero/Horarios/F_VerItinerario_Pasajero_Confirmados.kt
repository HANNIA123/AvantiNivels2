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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.curdfirestore.NivelAplicacion.ApiService
import com.example.curdfirestore.NivelAplicacion.BASE_URL
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
import com.example.curdfirestore.NivelAplicacion.RetrofitClient
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.VentanaSolicitudesConfirmados
import com.google.android.gms.maps.model.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerItinerarioPasajeroCon(
    navController: NavController,
    userId: String,
    viajes: List<HorarioDataReturn>,

    ) {

    var show by rememberSaveable { mutableStateOf(false) }
    var control by rememberSaveable { mutableStateOf(false) }

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
                TituloPantalla(Titulo = "Viajes \n confirmados", navController,)

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


                                    //---------------VISUALIZAR------------------------------------------
                                    //Convertir String a coordenadas  -- ORIGEN

                                    var markerLatO by remember { mutableStateOf(0.0) }
                                    var markerLonO by remember { mutableStateOf(0.0) }

                                    val markerCoordenadasLatLngO =
                                        convertirStringALatLng(viaje.horario_origen)

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

                                    val markerCoordenadasLatLngD =
                                        convertirStringALatLng(viaje.horario_destino)

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

                                    //Filtrar las solicitudes-------------------------------------------

                                    var text by remember { mutableStateOf("") }
                                    var resultados by remember { mutableStateOf(false) }
                                    val retrofit = Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                    val apiService = retrofit.create(ApiService::class.java)
                                    //Obtener lista de viajes (Itinerario)
                                    var solicitudes by remember {
                                        mutableStateOf<List<SolicitudData>?>(
                                            null
                                        )
                                    }

                                    println("SOLICITUDES afuera del IF")
                                    println(solicitudes)


                                    if(viaje.horario_solicitud=="Si"){ //solo  si tiene solicitjud

                                        println("Datoos: $userId ${viaje.horario_id }")
                                        LaunchedEffect(key1 = true) {
                                            println("Lauch")
                                            try {
                                                val resultadoViajes =
                                                    RetrofitClient.apiService.obtenerItiSolicitudesPas(
                                                        userId,
                                                        viaje.horario_id,
                                                        "Aceptada"
                                                    )
                                                solicitudes = resultadoViajes
                                                println("Resultados $solicitudes")

                                            } catch (e: Exception) {
                                                text = "Error al obtener Itinerario: $e"
                                                println("Error al obtener Itinerario: $e")
                                                //control=true
                                            }
                                        }

                                        /*println("SOLICITUDES")
                                        println(solicitudes)
                                        println("horario id")
                                        println(viajes)*/

                                        if(viaje.horario_solicitud=="Si" && solicitudes==null){
                                            show = true
                                            VentanaSolicitudesConfirmados(navController, userId, show, { show = false }, {})
                                        }else {


                                            if (solicitudes != null) {
                                                for (solicit in solicitudes!!) {
                                                    if (viaje.horario_dia == "Lunes") {
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

                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )

                                                        }
                                                    } ///------------


                                                    if (viaje.horario_dia == "Martes") {
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


                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }
                                                    } ///------------

                                                    if (viaje.horario_dia == "Miércoles") {
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

                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }
                                                    } ///------------

                                                    if (viaje.horario_dia == "Jueves") {
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


                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }
                                                    } ///------------

                                                    if (viaje.horario_dia == "Viernes") {
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


                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }
                                                    } ///------------


                                                    if (viaje.horario_dia == "Sábado") {
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


                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }
                                                    } ///------------

                                                    if (viaje.horario_dia == "Domingo") {
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

                                                            crearItinerarioCon(
                                                                navController,
                                                                viaje,
                                                                userId,
                                                                origen,
                                                                destino
                                                            )
                                                        }


                                                    }
                                                }

                                            }

                                        }

                                }
                                    else{
                                        show = true
                                        VentanaSolicitudesConfirmados(navController, userId, show, { show = false }, {})
                                    }
                                }
                            }
                            displayedDays++
                        }


                    }
                }
            }
        }
    }


    }

@Composable
fun crearItinerarioCon(
    navController: NavController,
    viaje: HorarioDataReturn,
    userId: String,
    origen: String,
    destino: String,
){
    //----------------VER DETALLES DEL VIAJE-----------------------

    if (viaje.horario_trayecto == "1") {
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
            Text = "${viaje.horario_hora}"
        )
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
            Text = "${viaje.horario_hora}"
        )

    }


    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {

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
                    color = Color(
                        104,
                        104,
                        104
                    ),
                )
            )
        }




        //Boton para editar
        TextButton(onClick = { /*TODO*/ }) {
            Text(
                text = "Editar",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(104, 104, 104),
                )
            )
        }

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

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text("Eliminar viaje")
                },
                text = {
                    Text("¿Estas seguro de eliminar este viaje?")
                },
                confirmButton = {
                    TextButton(onClick = {

                        val documentIdToDelete =
                            viaje.horario_id;
                        eliminarViaje(
                            documentIdToDelete
                        )
                        eliminarParada(
                            documentIdToDelete
                        )

                        navController.navigate("ver_itinerario_pasajero/$userId/${viaje.horario_id}")

                    }) {
                        Text("Aceptar ")

                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancelar")
                    }
                }
            )

        }

        //---------------------------------------------------
    }
    LineaGris()
}
