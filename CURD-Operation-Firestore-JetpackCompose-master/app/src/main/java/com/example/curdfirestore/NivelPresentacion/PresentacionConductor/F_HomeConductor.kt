package com.example.curdfirestore.NivelPresentacion.PresentacionConductor

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

import com.example.curdfirestore.NivelAplicacion.CoilImage
import com.example.curdfirestore.NivelAplicacion.encabezado
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelAplicacion.LoginViewModel
import com.example.curdfirestore.NivelAplicacion.UserData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


var maxh=0.dp
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun F_HomeConductor(
    usuario: UserData,
    navController: NavController,
    userID:String
){
    val viewModel= LoginViewModel()

    BoxWithConstraints{
        maxh = this.maxHeight-50.dp
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(45.dp)) {

                pruebaMenu(navController,userID)
            }
        }
    ){




        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(maxh)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,

            ){
            encabezado()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // Cargar y mostrar la imagen con Coil
                CoilImage(url = usuario.usu_foto, modifier = Modifier

                    .clip(CircleShape)
                    .size(200.dp)
                )


                Text(
                    text="${usuario.usu_nombre} ${usuario.usu_primer_apellido} ${usuario.usu_segundo_apellido}",
                    style = TextStyle(
                        color= Color(71, 12, 107),
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center

                    )
                )

                //Botones del inicio
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            238, 236, 239
                        )
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(route = "perfil_conductor/$userID")

                    }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = Color(137, 13, 88),
                    )
                    Text(
                        text = "Mi perfil",
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color(137, 13, 88),

                            )
                    )
                }
                //Notificaciones
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            238, 236, 239
                        )
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(route = "notificaciones_conductor/$userID")
                    }) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = Color(137, 13, 88),
                    )
                    Text(
                        text = "Notificaciones",
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color(137, 13, 88),
                        )
                    )
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(
                            238, 236, 239
                        )
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.signOut()
                        navController.navigate(route = "login")
                        println("Se ha cerrado")
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp),
                        tint = Color(137, 13, 88),
                    )
                    Text(
                        text = "Cerrar sesión",
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 30.dp),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = Color(137, 13, 88),
                        )
                    )

                }

                //Fin de botones inicio

            }
        }
    }
}