package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.curdfirestore.R
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.pruebaMenu


//Varibles globales


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
var mh=0.dp
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarParadas(
    navController: NavController,
    viajeID: String,
    correo: String,

) {

    var show by remember { mutableStateOf(false) }
    var horarioParada by remember { mutableStateOf("Establecer horario") }

    var nombreParada by remember {
        mutableStateOf("")
    }

    var taskMenuOpen by remember { mutableStateOf(false) }
    var taskMenuOpenHora by remember { mutableStateOf(false) }
    var taskMenuOpenMinutos by remember { mutableStateOf(false) }

    var dia by remember { mutableStateOf("Lunes") }
    var horaO by remember { mutableStateOf("7") }
    var minutoO by remember { mutableStateOf("00") }

    var nameError by remember { mutableStateOf(false) } // 1 -- Field obligatorio

    val context = LocalContext.current




    val minutos: ArrayList<String> = ArrayList()

    for (i in 0..59) {
        minutos.add(i.toString())
    }

    val horas: ArrayList<String> = ArrayList()

    for (i in 0..23) {
        horas.add(i.toString())
    }
    var name by remember {
        mutableStateOf("")
    }
    BoxWithConstraints {
     mh = this.maxHeight - 50.dp
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(45.dp)) {
                pruebaMenu(navController, correo)
            }
        }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(mh)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TituloPantalla(Titulo = "Registrar\nparada", navController)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                /*Nombre de la parada*/
                Text(
                    text = "Nombre de la parada",
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.Start),
                    style = TextStyle(
                        color = Color.Black, fontSize = 18.sp
                    )
                )
                OutlinedTextField(
                    value = nombreParada,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color(104, 104, 104)),
                    onValueChange = { nombreParada = it
                        nameError= false //2
                    },
                    // label = { androidx.compose.material.Text("Email") },
                    isError = nameError, //3
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                        .border(
                        width = 1.dp, shape = RectangleShape, color = Color.LightGray
                    )

                )
                Spacer(modifier = Modifier.height(16.dp))

                /*Seleccionar horario de partida*/




                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.LightGray
                        )
                        .clickable {
                            show = true

                        }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        modifier = Modifier
                            .size(55.dp)
                            .padding(10.dp, 5.dp),
                        tint = Color(104, 104, 104)
                    )
                    Text(
                        text = "$horarioParada (hrs)",
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp, 15.dp, 10.dp, 10.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color(104, 104, 104)

                        )
                    )

                }


                if (show == true) {
                    VentanaHora(
                        navController = navController,
                        userId = "123",
                        show = true,
                        { show = false },
                        onConfirm = {
                                selectedTime ->
                            horarioParada = selectedTime
                            // Haz algo con la hora seleccionada
                            // Por ejemplo, actualiza un ViewModel o realiza alguna otra acción
                            // println("Hora seleccionada: $selectedTime")
                        }
                    )
                    //
                }

                //fin menu


                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Ubicación de la parada",
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.Start),
                    style = TextStyle(
                        color = Color.Black, fontSize = 18.sp
                    )
                )

                val assistiveElementText = if (nameError) "Ingresa el nombre de la parada" else "" // 4
                val assistiveElementColor = if (nameError) { // 5
                    MaterialTheme.colors.error
                } else {
                    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
                }


                /*Seleccionar en el mapa*/
                androidx.compose.material.Button(
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray),
                    onClick = {

                            nameError = nombreParada.isBlank()
                            if (nombreParada.isNotBlank()) {
//                                finalizar=true
                                var hora="$horaO:$minutoO"
                                navController.navigate("registrar_parada_barra/$correo/$viajeID/$nombreParada/$hora")


                            } else {
                                // El texto está vacío, mostrar un mensaje de error o realizar acciones adicionales
                                println("El nombre está vacío. Por favor, ingrese algo.")
                            }

                            //navController.navigate("mapa_origen_conductor/$idViaje/$email")


                       // navController.navigate(route = "registrar_origen_conductor/$correo")

                    }) {
                    Icon(
                        imageVector = Icons.Filled.Place,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = Color(104, 104, 104)
                    )
                    Text(
                        text = "Selecciona en el mapa",
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color(104, 104, 104)

                        )
                    )
                }

Column(modifier=Modifier.fillMaxWidth().padding(15.dp).offset(y= 120.dp)){
    Button(colors = ButtonDefaults.buttonColors(containerColor = Color(194, 99, 157)),
        onClick = {

        },
modifier=Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Cancelar", style = TextStyle(
                fontSize = 20.sp,
            )
        )

    }



}

                //Fin



            }
            //Notones de guardar y cancelar

        }

    }



}

@Composable
fun MyDiaologExitosa(
    navController: NavController,
    email: String,
    idViaje: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(modifier = Modifier.background(Color.White).padding(5.dp)) {
                Text(
                    text = "Confirmación",
                    modifier = Modifier.padding(2.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Viaje registrado",
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
                            .size(70.dp).padding(5.dp),
                        painter = painterResource(id = R.drawable.cheque),
                        contentDescription = "Icono Viajes",
                        tint = Color(137, 13, 88)
                    )

                    // Primer botón para "Ver viaje"
                    TextButton(onClick = {

val pantalla="viaje"
                       navController.navigate("ver_viaje/$idViaje/$email/$pantalla")
                    }) {
                        Text(text = "Ver viaje")
                    }

                    // Segundo botón (agregado) - Puedes personalizar el texto y la acción según tus necesidades
                    TextButton(onClick = {
                        // Acción para el segundo botón
                        navController.navigate("nueva_parada/$idViaje/$email")
                    }) {
                        Text(text = "Nueva parada")
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun RegistrarParadasPreview() {
    // Esta función se utiliza para la vista previa
    var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()
    AgregarParadas(navController = navController, viajeID = "6552", correo)

}
