package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import android.annotation.SuppressLint


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Top
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.layout.HorizontalAlignmentLine

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.rememberNavController
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelAplicacion.ViajeData
import com.example.curdfirestore.R





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RegistrarViajeCon(
    navController: NavController,
    correo: String
) {


    //Para el numero de lugares
    var numLugares by remember {
        mutableStateOf("")
    }
    var isNumLugaresValido by remember { mutableStateOf(true) }
    var nameError by remember { mutableStateOf(false) } // 1 -- Field obligatorio
    var campovacio by remember { mutableStateOf(false) } // 1 --
    var show by remember { mutableStateOf(false) }
    var show1 by remember { mutableStateOf(false) }
    var ejecutado by remember { mutableStateOf(false) }
    var u_origen by remember { mutableStateOf("0,0") }
    var u_destino by remember { mutableStateOf("0,0") }
    var boton by remember { mutableStateOf(false) }
    var latitud by remember {
        mutableStateOf("")
    }
    var longitud by remember {
        mutableStateOf("")
    }
    var longitudD by remember {
        mutableStateOf("")
    }
    var latitudD by remember {
        mutableStateOf("")
    }
    var taskMenuOpen by remember { mutableStateOf(false) }
    var nu by remember { mutableStateOf(false) }
    var taskMenuOpenHora by remember { mutableStateOf(false) }
    var taskMenuOpenMinutos by remember { mutableStateOf(false) }
    var taskMenuOpenHoraD by remember { mutableStateOf(false) }
    var taskMenuOpenMinutosD by remember { mutableStateOf(false) }
    var dia by remember { mutableStateOf("Lunes") }
    var horaO by remember { mutableStateOf("7") }
    var horarioOrigen by remember { mutableStateOf("Establecer horario") }
    var horarioDestino by remember { mutableStateOf("Establecer horario") }
    var minutoO by remember { mutableStateOf("00") }
    var horaD by remember { mutableStateOf("2") }
    var minutoD by remember { mutableStateOf("00") }

    var viaje by remember { mutableStateOf<ViajeData?>(null) }


    val context = LocalContext.current
    val dias = arrayListOf<String>(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )

    /*Agregado 10/12/2023*/
    //Trayecto=0 origen UPIITA
    //Trayecto=1 destino UPIITA
    var taskMenuOpenTrayectos by remember { mutableStateOf(false) }
    var trayecto by remember { mutableStateOf("UPIITA como origen") }
    var tipoTrayecto by remember { mutableStateOf(0) }
    val trayectos = arrayListOf<String>(
        "UPIITA como origen", "UPIITA como destino "
    )

    if (trayecto=="UPIITA como origen"){
        tipoTrayecto=0
    }
    else{
        tipoTrayecto=1 //UPIITA destino
    }
//Fin

    val horas: ArrayList<String> = ArrayList()
    val minutos: ArrayList<String> = ArrayList()

    for (i in 0..59) {
        minutos.add(i.toString())
    }
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TituloPantalla(Titulo = "Registrar\nviaje", navController)
            Column(
                modifier = Modifier
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "Información sobre el viaje",
                    modifier=Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color(71, 12, 107),
                        fontSize = 23.sp,
                    )
                )

//Seleccionar dia
                Text(
                    text = "Día",
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
                                text = dia, style = TextStyle(
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

                        TaskMenu(dias,
                            expanded = taskMenuOpen,
                            onItemClick = { dia = it },
                            onDismiss = {
                                taskMenuOpen = false
                            }


                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                /*Agregar opcionn de trayecto*/

                Text(
                    text = "Trayecto",
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
                                text = trayecto, style = TextStyle(
                                    color = Color(104, 104, 104), fontSize = 18.sp
                                )
                            )
                        }
                    }
                    IconButton(
                        onClick = { taskMenuOpenTrayectos = true },
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

                        com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios.TaskMenu(
                            trayectos,
                            expanded = taskMenuOpenTrayectos,
                            onItemClick = { trayecto = it },
                            onDismiss = {
                                taskMenuOpenTrayectos = false
                            }


                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
/*Seleccionar horario de partida*/

                if(tipoTrayecto==0){
                    Text(
                        text = "Hora de salida desde UPIITA",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )

                }
                else{
                    Text(
                        text = "Hora de salida de tu docimicilo",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )

                }
                //Nuevo formato


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
                        text = "$horarioOrigen (hrs)",
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
                            horarioOrigen = selectedTime
                            // Haz algo con la hora seleccionada
                            // Por ejemplo, actualiza un ViewModel o realiza alguna otra acción
                            // println("Hora seleccionada: $selectedTime")
                        }
                    )
                    //
                }
                println("Horario origeeen $horarioOrigen")
                /*Hora de llegada*/
                Spacer(modifier = Modifier.height(12.dp))
                if(tipoTrayecto==0){
                    Text(
                        text = "Hora de llegada a tu destino",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )
                }
                else{
                    Text(
                        text = "Hora de llegada a UPIITA",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.Black, fontSize = 18.sp
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.LightGray
                        )
                        .clickable {
                            show1 = true

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
                        text = "$horarioDestino (hrs)",
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

                if (show1 == true) {
                    VentanaHora(
                        navController = navController,
                        userId = "123",
                        show = true,
                        { show1 = false },
                        onConfirm = {
                                selectedTime ->
                            horarioDestino = selectedTime
                            // Haz algo con la hora seleccionada
                            // Por ejemplo, actualiza un ViewModel o realiza alguna otra acción
                            // println("Hora seleccionada: $selectedTime")
                        }
                    )
                    //
                }

                Spacer(modifier = Modifier.height(12.dp))


                Text(
                    text = "Lugares disponibles",
                    modifier = Modifier
                        .padding(2.dp)
                        .align(Alignment.Start),
                    style = TextStyle(
                        color = Color.Black, fontSize = 18.sp
                    )
                )
                OutlinedTextField(
                    value = numLugares,
                    colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color(104, 104, 104)),
                    onValueChange = {
                        numLugares = it
                        isNumLugaresValido = true // Restablecer la validez al cambiar el valor
                    },
                    isError = !isNumLugaresValido, // Invertir la validez para mostrar el error si es falso
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp, shape = RectangleShape, color = Color.LightGray
                        ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )


                if(campovacio){
                    Text(
                        text = "*Campo inválido",
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start),
                        style = TextStyle(
                            color = Color.DarkGray, fontSize = 12.sp
                        )
                    )
                }

            }
          //Notones de guardar y cancelar
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    modifier=Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(137, 13, 88)),
                    onClick = {
                       // horaO="$horaO:$minutoO"
                        //horaD="$horaD:$minutoD"
                        var hO=horarioOrigen
                        var hD=horarioDestino



                        // Validar el valor de numLugares al presionar el botón
                        val soloDigitos = numLugares.filter { it.isDigit() }
                        isNumLugaresValido = soloDigitos.isNotEmpty() // Verificar si hay al menos un dígito

                        if (isNumLugaresValido) {
                            var lugar=numLugares.toInt()
                            if(lugar<10) {


                                if (tipoTrayecto == 0) { //UPIITA COMO ORIGEN
                                    //El conductor elige el destino-- mapa destino
                                    navController.navigate(route = "registrar_destino_conductor/$correo/$dia/$hO/$hD/$numLugares")
                                } else { //UPIITA COMO DESTINO
                                    //El conductor elige el origen -- mapa origen
                                    navController.navigate(route = "registrar_origen_conductor/$correo/$dia/$hO/$hD/$numLugares")

                                }
                            }
                            else{
                                campovacio=true
                            }
                            // Realizar la lógica necesaria si el número es válido
                            // ...

                        } else {
                            campovacio=true
                            // Manejar el caso de error, por ejemplo, mostrar un mensaje
                            // ...

                        }


                                   }) {
                    Text(
                        text = "Siguiente", style = TextStyle(
                            fontSize = 20.sp,
                        )
                    )
                }

            }
            Spacer(modifier = Modifier.height(45.dp))
        }

    }


/*
    if (boton==true && ejecutado==false){
        val viajeData = ViajeData(
            usu_id = correo,
            viaje_dia = dia,
            viaje_hora_partida = "$horaO:$minutoO",
        )
        val hora="$horaO:$minutoO"
        navController.navigate(route = "registrar_origen_conductor/$correo/$dia/$hora)
        GuardarViaje(navController, correo, viajeData)
        ejecutado=true
    }
    */
}


@Composable
fun TaskMenu(
    options: ArrayList<String>,
    expanded: Boolean, // (1)
    onItemClick: (String) -> Unit,
    onDismiss: () -> Unit,

    ) {
    DropdownMenu( // (3)
        expanded = expanded, onDismissRequest = onDismiss
    ) {
        options.forEach { option ->
            DropdownMenuItem( // (4)
                modifier = Modifier.fillMaxWidth(), onClick = {
                    onItemClick(option)
                    onDismiss()
                }) {
                Text(text = option)
            }
        }
    }
}


@Composable
fun BotonesGyC() {
    Row(
        modifier = Modifier.padding(0.dp, 50.dp, 0.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(50.dp)

    ) {
        Button(colors = ButtonDefaults.buttonColors(containerColor = Color(137, 13, 88)),
            onClick = { /*TODO*/ }) {

            Text(
                text = "Guardar", style = TextStyle(
                    fontSize = 20.sp,
                )
            )

        }
        Button(colors = ButtonDefaults.buttonColors(containerColor = Color(194, 99, 157)),
            onClick = {

            }) {

            Text(
                text = "Cancelar", style = TextStyle(
                    fontSize = 20.sp,
                )
            )

        }
    }
}











@Preview(showBackground = true)
@Composable
fun MyComposablePreview() {
    // Esta función se utiliza para la vista previa
var correo="hplayasr1700@alumno.ipn.mx"
    val navController = rememberNavController()
    RegistrarViajeCon(navController = navController, correo = correo)
}