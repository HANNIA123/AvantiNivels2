package com.example.curdfirestore.NivelPresentacion.PresentacionConductor

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.CoilImage
import com.example.curdfirestore.NivelAplicacion.InfTextos
import com.example.curdfirestore.NivelAplicacion.LineaGris
import com.example.curdfirestore.NivelAplicacion.NombreCom
import com.example.curdfirestore.NivelAplicacion.TituloPantalla
import com.example.curdfirestore.NivelAplicacion.pruebaMenu
import com.example.curdfirestore.NivelAplicacion.UserData
import com.example.curdfirestore.NivelAplicacion.VehicleData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun F_PerfilConductor(
    usuario: UserData,
    vehiculo: VehicleData,
    navController: NavController,
    correo: String
){
    BoxWithConstraints {
        maxh = this.maxHeight - 50.dp
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier.height(45.dp)) {
                pruebaMenu(navController,correo)
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
                TituloPantalla(Titulo = "Mi perfil", navController,)

                // Cargar y mostrar la imagen con Coil
                CoilImage(url = usuario.usu_foto, modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
                )
                Text(
                    text = "Información de la cuenta",
                    modifier=Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color(71, 12, 107),
                        fontSize = 23.sp,
                    )
                )

//Datos del conductor
                InfTextos(Title = "Nombre de usuario", Inf =usuario.usu_nombre_usuario )
                InfTextos(Title = "Tipo de usuario", Inf =usuario.usu_tipo )
//Falta cambiar contraseñ
                Row(
                    horizontalArrangement = Arrangement.spacedBy(230.dp)
                ) {
                    Text(
                        text = "Contraseña",
                        modifier = Modifier
                            .offset(x = 30.dp)
                            .padding(2.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp
                        )
                    )
                    IconButton(
                        modifier = Modifier.offset(y = -10.dp),
                        onClick = {
                            navController.navigate(route = "cambiar_password_con/$correo")
                        }) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Icono Usuario",
                            tint = Color(104, 104, 104),


                            )

                    }

                }
                LineaGris()
                Text(
                    text = "Información personal",
                    modifier=Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(
                        color = Color(71, 12, 107),
                        fontSize = 23.sp,
                    )
                )
                InfTextos(Title = "Nombre", Inf = NombreCom(
                    nombre = usuario.usu_nombre,
                    apellidop = usuario.usu_primer_apellido,
                    apellidom = usuario.usu_segundo_apellido
                ) )

                InfTextos(Title = "Boleta: ", Inf = usuario.usu_boleta)
                InfTextos(Title = "Correo electrónico", Inf =correo )
                InfTextos(Title = "Número telefónico", Inf =usuario.usu_telefono)


                    LineaGris()
                    Text(
                        text = "Información del vehiculo",
                        modifier=Modifier.align(Alignment.CenterHorizontally),
                        style = TextStyle(
                            color = Color(71, 12, 107),
                            fontSize = 23.sp,
                        )
                    )
                    InfTextos(Title = "Marca", Inf =vehiculo.vehi_marca )

                    InfTextos(Title = "Color", Inf =vehiculo.vehi_color )
                    InfTextos(Title = "Placas", Inf =vehiculo.vehi_placas )
                    InfTextos(Title = "Modelo", Inf =vehiculo.vehi_modelo )
                    InfTextos(Title = "Poliza de seguro", Inf =vehiculo.vehi_poliza )




            }
        }
    }
}