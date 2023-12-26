package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoPasajeros
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun F_VerPasajerosCon(
    navController: NavController,
    userId: String,
    solicitudes: List<SolicitudData>
) {
    var idsol by remember { mutableStateOf("") }
    var aceptar by remember { mutableStateOf(false) }
    var rechazar by remember { mutableStateOf(false) }
    var nombresDeUsuarioMostrados = mutableSetOf<String>()
    var show by rememberSaveable { mutableStateOf(false) }
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
                TituloPantalla(Titulo = "Pasajeros", navController)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    val Sol_Aceptada = solicitudes.any { it.solicitud_status == "Aceptada" }
                    val Sol_Rechazadas = solicitudes.all { it.solicitud_status == "Rechazada" }
                    if (Sol_Rechazadas || !Sol_Aceptada) {
                        show = true
                        VentanaNoPasajeros(navController, userId, show, { show = false }, {})
                    } else {
                        for (solicitud in solicitudes) {
                            if (solicitud.solicitud_status == "Aceptada") {
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


                                    if (usuario != null && usuario!!.usu_nombre_usuario !in nombresDeUsuarioMostrados) {
                                        Row(
                                            modifier = Modifier.padding(5.dp)
                                        ) {
                                            CoilImage(
                                                url = usuario!!.usu_foto, modifier = Modifier
                                                    .size(85.dp)
                                                    .clip(CircleShape)
                                            )
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier.padding(
                                                    10.dp,
                                                    15.dp,
                                                    10.dp,
                                                    15.dp
                                                )
                                            ) {

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

                                                TextButton(
                                                    onClick = {
                                                        navController.navigate("reportar_pasajero/${solicitud.pasajero_id}/$userId")
                                                    },
                                                    modifier = Modifier
                                                        .background(color = Color.White)
                                                ) {
                                                    Row {
                                                        Icon(
                                                            imageVector = Icons.Filled.Warning,
                                                            contentDescription = "Reportar"
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp)) // Agrega espacio entre el icono y el texto
                                                        Text(
                                                            text = "Reportar",
                                                            style = TextStyle(
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = Color(104, 104, 104),
                                                            )
                                                        )
                                                    }
                                                }

                                            }

                                        }
                                        nombresDeUsuarioMostrados.add(usuario!!.usu_nombre_usuario)

                                        LineaGris()
                                    }
                                }
                            }
                        } //LLAVE DEL FOR
                    }
                    }
                }

            }


        }



    }





@Preview(showBackground = true)
@Composable
fun MyComposablePreviewVerSol1() {
    // Esta función se utiliza para la vista previa
    var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()

    val viajeData: ViajeData = ViajeData(/* provide constructor arguments here if needed */)
    val listaDeViajes: List<SolicitudData> = listOf(
        SolicitudData(/* provide constructor arguments here if needed */),

        )
    F_VerSolicitudesCon(navController = navController, userId = correo, listaDeViajes)
}



