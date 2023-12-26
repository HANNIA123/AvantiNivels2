package com.example.curdfirestore.NivelPresentacion.PresentacionPasajero

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.curdfirestore.R


//AGREGADO 19/12/23
@Composable
fun VentanaNoConductores(
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
                    text = "Conductores",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes conductores agregados a tus viajes registrados. ",
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
                        painter = painterResource(id = R.drawable.conductor),
                        contentDescription = "Icono Conductores",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("home_viaje_pasajero/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}
@Composable
fun VentanaSinSolicitudes(
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
                    text = "Viajes registrados",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes viajes registrados.",
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
                        navController.navigate("ver_itinerario_pasajero/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}


@Composable
fun VentanaSolicitudesPendientes(
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
                    text = "Solicitudes pendientes",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes solicitudes pendientes.",
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
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Icono Solicitud Pendiente",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        tint = Color(137, 13, 88),
                    )
                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("ver_itinerario_pasajero/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}


@Composable
fun VentanaSolicitudesConfirmados(
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
                    text = "Viajes Confirmados",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Por el momento, no tienes viajes aceptados. ",
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
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Icono Itineario",
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp),
                        tint = Color(137, 13, 88),
                    )
                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        onDismiss()
                        navController.navigate("ver_itinerario_pasajero/$userId")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}