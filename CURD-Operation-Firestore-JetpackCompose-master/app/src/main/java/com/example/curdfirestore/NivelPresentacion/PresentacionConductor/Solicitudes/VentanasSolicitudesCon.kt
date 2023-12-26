package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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


@Composable
fun VentanaSolicitudAceptada(
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
                    text = "Solicitud Aceptada",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Aceptaste la solictud, puedes ver los detalles en tu itinerario de viaje ",
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
                        navController.navigate(route = "ver_solicitudes_conductor/$email")
                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}

@Composable
fun VentanaSolicitudRechazada(
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
                    text = "Solicitud Rechazada",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "La solictud ha sido rechazado, no te preocupes, el pasajero no será notificado. ",
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
                        painter = painterResource(id = R.drawable.not),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        navController.navigate(route = "ver_solicitudes_conductor/$email")

                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}

@Composable
fun VentanaSolicitudNoPermitida(
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
                    text = "No puedes aceptar esta solicitud",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "En este momento ya no tienes lugares disponibles para este viaje.",
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
                        painter = painterResource(id = R.drawable.not),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {
                        navController.navigate(route = "ver_solicitudes_conductor/$email")

                    }) {
                        Text(text = "Aceptar")
                    }


                }
            }
        }
    }
}