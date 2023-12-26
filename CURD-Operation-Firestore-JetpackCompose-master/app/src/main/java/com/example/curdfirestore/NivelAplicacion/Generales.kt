package com.example.curdfirestore.NivelAplicacion

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.curdfirestore.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun NombreCom(nombre: String, apellidop: String, apellidom: String):String {
    var fn: String
    fn= nombre+" "+ apellidop+ " "+ apellidom
    return  fn
}
@Composable
fun LineaGris(){
    Box(
        modifier = Modifier
            .width(350.dp)
            .height(1.dp)
            //.align(Alignment.CenterHorizontally)
            .background(Color(222, 222, 222))

    )
}


@Composable
fun pruebaMenu(
    navController: NavController,
    userID:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = {

            navController.navigate(route = "inicio_viaje_conductor/$userID")
        }) {
            Icon(
                modifier = Modifier
                    .size(35.dp),

                imageVector = Icons.Filled.Home,
                contentDescription = "Icono Home",
                tint = Color(137, 13, 88),

                )
        }
    IconButton(onClick = {

        navController.navigate(route = "home_viaje_conductor/$userID")
    }) {
        Icon(
            modifier = Modifier
                .size(35.dp),

            painter = painterResource(id = R.drawable.car),
            contentDescription = "Icono Viajes",
            tint = Color(137, 13, 88)
        )
    }
       IconButton(onClick = {

           navController.navigate(route = "HomeCon/$userID")
       }) {

           Icon(
               modifier = Modifier
                   .size(35.dp),
               painter = painterResource(id = R.drawable.btuser),
               contentDescription = "Icono Usuario",
               tint = Color(137, 13, 88),

               )
       }
    }
}


@Composable //Agregado 09/12/2023
fun pruebaMenuPasajero(
    navController: NavController,
    userID:String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                modifier = Modifier
                    .size(35.dp),
                imageVector = Icons.Filled.Home,
                contentDescription = "Icono Home",
                tint = Color(137, 13, 88),

                )
        }
        IconButton(onClick = {
            navController.navigate(route = "home_viaje_pasajero/$userID")
        }) {
            Icon(
                modifier = Modifier
                    .size(35.dp),
                painter = painterResource(id = R.drawable.car),
                contentDescription = "Icono Viajes",
                tint = Color(137, 13, 88)
            )
        }
        IconButton(onClick = {
            navController.navigate(route = "HomeCon/$userID")
        }) {
            Icon(
                modifier = Modifier
                    .size(35.dp),
                painter = painterResource(id = R.drawable.btuser),
                contentDescription = "Icono Usuario",
                tint = Color(137, 13, 88),

                )
        }
    }
}

@Composable
fun encabezado(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        contentAlignment = Alignment.TopEnd
    )
    {
        Image(
            modifier = Modifier
                .width(130.dp),
            painter = painterResource(id = R.drawable.elipselado),
            contentDescription = "Fondo inicial",
            contentScale = ContentScale.FillWidth

        )
        Image(
            modifier = Modifier
                .width(100.dp)
                .padding(5.dp, 5.dp, 10.dp, 5.dp),
            painter = painterResource(id = R.drawable.logoa),
            contentDescription = "Logo",
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun TituloPantalla(Titulo: String,
                   navController: NavController
){
    Row {
        Row (
            modifier = Modifier
                .padding(5.dp, 10.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Box {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        modifier= Modifier
                            .height(57.dp)
                            .width(57.dp)
                            .align(Alignment.Center),

                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Icono atras",
                        tint= Color(137, 13, 88),

                        )
                }

            }
            Text(
                text= Titulo,  style = TextStyle(
                    color= Color(71, 12, 107),
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center

                )
            )

        }
        encabezado()
    }
}


@Composable
fun TituloNoBack(Titulo: String

){
    Row {
        Row (
            // modifier = Modifier
            //   .padding(5.dp, 0.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){


            Text(
                text= Titulo,  style = TextStyle(
                    color= Color(71, 12, 107),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center

                )
            )

        }
    }
}
@Composable
fun OnlyTitulo(Titulo: String,
                   navController: NavController
){
    Row {
        Row (
           // modifier = Modifier
             //   .padding(5.dp, 0.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Box {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        modifier= Modifier
                            .size(30.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Icono atras",
                        tint= Color(137, 13, 88),

                        )
                }

            }
            Text(
                text= Titulo,  style = TextStyle(
                    color= Color(71, 12, 107),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center

                )
            )

        }
    }
}
@Composable
fun CoilImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberAsyncImagePainter(model = Uri.parse(url)),
        contentDescription = null,
        modifier = modifier
    )
}
//Agregado 29/11/2023

fun convertirStringALatLng(coordenadas: String): LatLng? {
    try {
        // Supongamos que las coordenadas están separadas por una coma
        val partes = coordenadas.split(",")

        if (partes.size == 2) {
            val latitud = partes[0].trim().toDouble()
            val longitud = partes[1].trim().toDouble()

            return LatLng(latitud, longitud)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}
@Composable
fun InfTextos(Title: String, Inf: String ){
    Text(
        text = Title,
        modifier = Modifier
            .offset(x = 30.dp)
            .padding(2.dp),
        style = TextStyle(
            color = Color.Black,
            fontSize = 18.sp
        )
    )
    Text(
        text = Inf,
        modifier = Modifier
            .offset(x = 30.dp)
            .padding(2.dp),
        style = TextStyle(
            color = Color(104, 104, 104),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light
        )
    )

}

@Composable
fun TextoMarker(Label:String, Text:String) {
    Row {
        Text(
            text = Label,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            ),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center

        )
        Text(
            text = Text,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun TextInMarker(Label:String, Text:String){
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(     fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )) {
                append(Label)
            }


            withStyle(style = SpanStyle(
                fontSize = 15.sp
            )) {
                append(Text)
            }
        }
    )
}

@Composable
fun TexItinerario(Label:String, Text:String) {
    Row {
        Text(
            text = Label,
            style = TextStyle(

                fontSize = 18.sp
            ),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start

        )
        Text(
            text = Text,
            style = TextStyle(

                color= Color(104, 104, 104),
                fontSize = 18.sp
            ),

            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}

//-----------------------------------------------------------------------
//FUNCIONES PARA ELIMINAR EL VIAJE Y PARADAS

fun eliminarViaje(documentId: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("viaje").document(documentId)
        documentReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Documento con ID $documentId eliminado correctamente de Firestore.")
            } else {
                println("Error al intentar eliminar el documento de Firestore: ${task.exception}")
            }
        }
    } catch (e: Exception) {
        println("Error al intentar eliminar el documento de Firestore: $e")
    }
}


fun eliminarParada(fieldValue: String) {
    try {
        val db = FirebaseFirestore.getInstance()

        db.collection("parada")
            .whereEqualTo("viaje_id", fieldValue)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val documentReference = db.collection("parada").document(document.id)

                        documentReference.delete().addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                println("Documento con ID ${document.id} eliminado correctamente de Firestore.")
                            } else {
                                println("Error al intentar eliminar el documento de Firestore: ${deleteTask.exception}")
                            }
                        }
                    }
                } else {
                    println("Error al intentar encontrar el documento en Firestore: ${task.exception}")
                }
            }
    } catch (e: Exception) {
        println("Error al intentar eliminar el documento de Firestore: $e")
    }
}

//-----------------------------------------------

fun eliminarSolicitud_actualizarHorario(fieldValue: String) {
    try {
        val db = FirebaseFirestore.getInstance()

        db.collection("solicitud")
            .whereEqualTo("viaje_id", fieldValue)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val horarioId = document.getString("horario_id")
                        val solicitudReference = db.collection("solicitud").document(document.id)
                        solicitudReference.delete().addOnCompleteListener { deleteTask ->
                            if (deleteTask.isSuccessful) {
                                println("Documento ${document.id} eliminado correctamente de Firestore.")

                                if (horarioId != null) {
                                    val horarioReference = db.collection("horario").document(horarioId)
                                    horarioReference.update("horario_solicitud", "No")
                                        .addOnCompleteListener { updateTask ->
                                            if (updateTask.isSuccessful) {
                                                println("Campo actualizado correctamente en la colección 'horario'.")
                                            } else {
                                                println("Error al intentar actualizar el campo en 'horario': ${updateTask.exception}")
                                            }
                                        }
                                }
                            } else {
                                println("Error al intentar eliminar el documento de Firestore: ${deleteTask.exception}")
                            }
                        }
                    }
                } else {
                    println("Error al intentar encontrar el documento en Firestore: ${task.exception}")
                }
            }

    } catch (e: Exception) {
        println("Error al intentar eliminar el documento de Firestore: $e")
        }
}

//AGREGADO EL 23/12/23
@Composable
fun TextViaje(Text:String) {
    Row {
        Text(
            text = Text,
            style = TextStyle(

                color= Color.Red,
                fontSize = 18.sp
            ),

            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start)
           }
}




fun eliminarViajeP(documentId: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("horario").document(documentId)
        documentReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Documento con ID $documentId eliminado correctamente de Firestore.")
            } else {
                println("Error al intentar eliminar el documento de Firestore: ${task.exception}")
            }
        }
    } catch (e: Exception) {
        println("Error al intentar eliminar el documento de Firestore: $e")
    }
}



