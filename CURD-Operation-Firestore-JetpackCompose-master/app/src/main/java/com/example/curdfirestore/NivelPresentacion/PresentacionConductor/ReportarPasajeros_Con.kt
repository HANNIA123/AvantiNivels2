package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.TaskMenu
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.curdfirestore.NivelAplicacion.Conductor.GuardarReporte
import com.example.curdfirestore.NivelAplicacion.ReporteData
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelAplicacion.eliminarParada
import com.example.curdfirestore.NivelAplicacion.eliminarViaje
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportarPasajeros_Con(
    navController: NavController,
    usuarioPas: String,
    userId: String,
) {
    var taskMenuOpen by remember { mutableStateOf(false) }
    var motivo by remember { mutableStateOf("Acoso") }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var boton by remember { mutableStateOf(false) }
    var ejecutado by remember { mutableStateOf(false) }
    val texto: String = descripcion.text
    var showDialog by remember { mutableStateOf(false) }
    var showSecondDialog by remember { mutableStateOf(false) }
    var redirectUser by remember { mutableStateOf(false) }

    val motivos = arrayListOf<String>(
        "Acoso", "Cancelación de viaje", "No asistió a punto de encuentro", "Malas prácticas de conducción", "Otros"
    )

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
                TituloPantalla(Titulo = "Reportar\nusuario", navController)

                Column(
                    modifier = Modifier
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = "Motivo del reporte:",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )
                    Box(
                        Modifier
                            .border(width = 1.dp, shape = RectangleShape, color = Color.LightGray)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = motivo, style = TextStyle(
                                        color = Color(104, 104, 104), fontSize = 18.sp
                                    )
                                )
                            }
                        }
                        IconButton(
                            onClick = { taskMenuOpen = true },
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Seleccionar",
                                tint = Color(137, 13, 88),
                                modifier = Modifier.size(50.dp)
                            )

                            TaskMenu(motivos,
                                expanded = taskMenuOpen,
                                onItemClick = { motivo = it },
                                onDismiss = {
                                    taskMenuOpen = false
                                }


                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Detalles del reporte:",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )

                    BasicTextField(
                        value = descripcion.text,
                        onValueChange = { descripcion = TextFieldValue(it) },
                        textStyle = TextStyle(
                            color = Color(104, 104, 104),
                            fontSize = 18.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            //.fillMaxHeight()
                            .height(150.dp)
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = RectangleShape
                            )
                            .padding(8.dp) // Opcional: puedes ajustar el relleno según tus preferencias
                    )

                    Spacer(Modifier.size(16.dp))
                    Button(
                        colors= ButtonDefaults.buttonColors(backgroundColor = Color(137, 13, 88) ),
                        onClick = {

                            showDialog = true

                        }) {

                        Text(
                            text = "Enviar reporte", style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        )

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = {
                                    Text("Reportar usuario")
                                },
                                text = {
                                    Text("¿Estas seguro de reportar este usuario?")
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        boton=true
                                        showDialog = false
                                        showSecondDialog = true


                                    }) {
                                        Text("Aceptar")
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


                        if (showSecondDialog) {
                            AlertDialog(
                                onDismissRequest = { showSecondDialog = false },
                                title = {
                                    Text("Reporte exitoso")
                                },
                                text = {
                                    Text("Se ha reportado al usuario.")
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        showSecondDialog = false
                                        redirectUser = true
                                    }) {
                                        Text("Cerrar")
                                    }
                                }
                            )
                        }

                        if (redirectUser) {
                            navController.navigate(route = "ver_pasajeros_conductor/$userId")
                        }

                    }


                }


            }


        }


    }




    //Guardar reporte
    if (boton==true && ejecutado==false){
        val fecha_now= obtenerFechaEnFormato().toString()
        val reporteData = ReporteData(
            conductor_id = userId,
            pasajero_id =  usuarioPas,
            repor_motivo = motivo,
            repor_detalles = texto,
            repor_fecha = fecha_now
        )

        GuardarReporte(navController, reporteData)

        ejecutado=true
    }


}





