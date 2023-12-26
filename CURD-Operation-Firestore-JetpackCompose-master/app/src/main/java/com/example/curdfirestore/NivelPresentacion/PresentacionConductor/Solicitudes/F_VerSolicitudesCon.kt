package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.ApiService
import com.example.curdfirestore.NivelAplicacion.BASE_URL
import com.example.curdfirestore.NivelAplicacion.CoilImage
import com.example.curdfirestore.NivelAplicacion.Conductor.RespuestaSolicitud
import com.example.curdfirestore.NivelAplicacion.LineaGris
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.RetrofitClient
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelAplicacion.UserData
import com.example.curdfirestore.NivelAplicacion.ViajeData
import com.example.curdfirestore.NivelPresentacion.PantallaError
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoSolicitudes
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var maxh = 0.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerSolicitudesCon(
    navController: NavController,
    userId: String,
    solicitudes: List<SolicitudData>,


) {
    var show1 by remember { mutableStateOf(false) }
    var idviaje by remember { mutableStateOf("") }
    var idsol by remember { mutableStateOf("") }
    var aceptar by remember { mutableStateOf(false) }
    var rechazar by remember { mutableStateOf(false) }
    var show by rememberSaveable { mutableStateOf(false) }
    val solicitudesOrdenadas = solicitudes.sortedBy { it.solicitud_date }
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
                TituloPantalla(Titulo = "Solicitudes", navController)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    val Sol_Pendiente = solicitudes.any { it.solicitud_status == "Pendiente" }
                    val Sol_Rechazadas = solicitudes.all { it.solicitud_status == "Rechazada" }
                    if (Sol_Rechazadas || !Sol_Pendiente) {
                        show = true
                        VentanaNoSolicitudes(navController, userId, show, { show = false }, {})
                    } else {
                        for (solicitud in solicitudesOrdenadas) {

                            if (solicitud.solicitud_status == "Pendiente") {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {

                                    //Obtener nombre del pasajero
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
                                    //Obtener informacion de la parada
                                    //Obtener nombre del pasajero
                                    var parada by remember { mutableStateOf<ParadaData?>(null) }
                                    var text2 by remember { mutableStateOf("") }
                                    val retrofit2 = Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build()
                                    val apiService2 = retrofit2.create(ApiService::class.java)
                                    LaunchedEffect(key1 = true) {
                                        try {
                                            val resultadoParada =
                                                RetrofitClient.apiService.obtenerParada(solicitud.parada_id)
                                            parada = resultadoParada
                                            // Haz algo con el objeto Usuario
                                            println("Parada obtenido: $usuario")
                                        } catch (e: Exception) {
                                            text = "Error al obtener usuario: $e"
                                            println("Error al obtener usuario: $e")
                                        }
                                    }
                                    if (usuario != null) {
                                        Row(
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            CoilImage(
                                                url = usuario!!.usu_foto, modifier = Modifier
                                                    .size(75.dp)
                                                    .clip(CircleShape)
                                            )
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(10.dp, 15.dp, 10.dp, 15.dp)
                                            ) {
                                                Text(
                                                    buildAnnotatedString {
                                                        withStyle(
                                                            style = SpanStyle(
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 15.sp
                                                            )
                                                        ) {
                                                            append("${usuario!!.usu_nombre} ${usuario!!.usu_primer_apellido}")
                                                        }

                                                        withStyle(
                                                            style = SpanStyle(
                                                                fontSize = 15.sp
                                                            )
                                                        ) {
                                                            append(" te ha enviado una solicitud.")
                                                        }

                                                        appendLine()

                                                        withStyle(
                                                            style = SpanStyle(
                                                                fontSize = 13.sp,
                                                                color = Color.Gray
                                                            )
                                                        ) {
                                                            append("${solicitud.solicitud_date}")
                                                        }


                                                    }
                                                )
                                            }

                                        }
                                    }


                                    if (parada != null) {
                                        Text(
                                            text = "Parada solicitada: ${parada!!.par_nombre} ",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp
                                            )
                                        )
                                        var ubiParada = CoordenadasToAdress(parada!!.par_ubicacion)
                                        Text(
                                            text = "Ubicación: $ubiParada ",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp
                                            )
                                        )
                                        Text(
                                            text = "Hora aproximada: ${parada!!.par_hora} ",
                                            style = TextStyle(
                                                color = Color.Black,
                                                fontSize = 15.sp
                                            )
                                        )

                                    }

                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth()
                                ) {

                                    //Boton para editar
                                    TextButton(onClick = {
                                        aceptar = true
                                        idsol = solicitud.solicitud_id
                                        idviaje=solicitud.viaje_id
                                    }) {
                                        Text(
                                            text = "Aceptar",
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(104, 104, 104),
                                            )
                                        )
                                    }

                                    TextButton(onClick = {
                                        println("Boton rechazar")
                                        rechazar = true
                                        idsol = solicitud.solicitud_id
                                    }) {
                                        Text(
                                            text = "Rechazar",
                                            style = TextStyle(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(104, 104, 104),
                                            )
                                        )
                                    }

                                }
                                LineaGris()
                            }
                    }// LLAVE DEL FOR  DE SOLICITUDES ORDENADAS

                }
            }

                }


            }


        }

    if(aceptar) {
        var error by remember { mutableStateOf(false) }
        //Consulta a viajes para obtener el numero de lugares
        var viaje by remember { mutableStateOf<ViajeData?>(null) }
        val retrofit1 = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService1 = retrofit1.create(ApiService::class.java)
        LaunchedEffect(key1 = true) {
            try {
                val resultadoViaje = RetrofitClient.apiService.pasarViaje(idviaje) //Consulta el viaje que coincida con un id dado
                viaje = resultadoViaje
                // Haz algo con el objeto Usuario

                println("Viaje obtenido: $viaje")
            } catch (e: Exception) {
                //text="Error al obtener viaje: $e"
                println("Error al obtener viaje: $e")
                error = true
            }
        }
        var numLugares = 0
        if (viaje != null) {
            var nl = viaje!!.viaje_num_lugares
            numLugares = nl.toInt()


        println("Numero lugares: $numLugares")

        if (numLugares > 0) {

            var newLugares = numLugares - 1

            actualizarLugares(idviaje, "viaje_num_lugares", newLugares.toString())
            RespuestaSolicitud(
                navController,
                userId,
                solicitudId = idsol,
                status = "Aceptada",
                viajeId = idviaje
            )
        } else {
            show1 = true
            VentanaSolicitudNoPermitida(navController, userId, show1, { show1 = false }, {})
        }


        //actualizarLugares(idviaje, "viaje_num_lugares")

    }
    }
    if(rechazar){
        println("Otra vez")
        RespuestaSolicitud(navController, userId, solicitudId =idsol, status = "Rechazada" , viajeId=idviaje)


    }

    }


fun actualizarLugares(documentId: String, campo: String, valor: Any) {
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


/*
@Preview(showBackground = true)
@Composable
fun MyComposablePreviewVerSol() {
    // Esta función se utiliza para la vista previa
    var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()

    val viajeData: ViajeData = ViajeData(/* provide constructor arguments here if needed */)
    val listaDeViajes: List<SolicitudData> = listOf(
        SolicitudData(/* provide constructor arguments here if needed */),

        )
    F_VerSolicitudesCon(navController = navController, userId = correo, listaDeViajes)
}



*/