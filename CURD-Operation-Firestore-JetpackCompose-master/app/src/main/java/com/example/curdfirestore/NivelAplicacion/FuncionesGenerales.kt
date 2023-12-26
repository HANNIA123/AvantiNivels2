package com.example.curdfirestore.NivelAplicacion

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.F_HomeConductor
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.F_PerfilConductor
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.F_HomePasajero
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.F_PerfilPasajero
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public var BASE_URL =
    "http://192.168.1.79:3000"
data class Viaje(
    var usu_id: String="",
    var viaje_destino:String="",
    var viaje_origen:String="",

    )

data class RespuestaApi(
    val success: Boolean,
    val message: String,
    val userId: String
)

data class MensajeResponse
    (val mensaje: String


)

object RetrofitClient {
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
//Esta función obtine los datos del usuario y vehiculo, manda a la pantalla de Home y Perfil
@Composable
fun ObtenerHome(
    navController: NavController,
    correo: String,
    pantalla:String
) {
    var usuario by remember { mutableStateOf<UserData?>(null) }
    var vehiculo by remember { mutableStateOf<VehicleData?>(null) }
    var text by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    LaunchedEffect(key1 = true) {
        try {
            val  resultadoUsuario = RetrofitClient.apiService.pasarUsuario(correo)
            usuario=resultadoUsuario
            // Haz algo con el objeto Usuario

            println("Usuario obtenido: $usuario")
        } catch (e: Exception) {
            text="Error al obtener usuario: $e"
            println("Error al obtener usuario: $e")
        }
    }
    LaunchedEffect(key1 = true) {
        try {
            val  resultadoVehiculo = RetrofitClient.apiService.pasarVehiculo(correo)
            vehiculo=resultadoVehiculo
            // F_HomeConductor(usuario = usuario, navController = navController, userID = correo)
            // Haz algo con el objeto Usuario

        } catch (e: Exception) {
            text="Error al obtener usuario: $e"
            println("Error al obtener usuario: $e")
        }
    }
    // Construir la interfaz de usuario utilizando el estado actualizado
    //Se cambio 08/12/2023

    if (usuario != null) {
        // Utilizar el objeto Usuario en otra función @Composable
        if (pantalla=="Home"){
            //Dirigir a pantalla segín el tipo de usuario
            if (usuario!!.usu_tipo=="Conductor") { //Cambio 09/12/2023
                F_HomeConductor(usuario!!, navController, correo) //Pantalla de home

            }
            else if (usuario!!.usu_tipo=="Pasajero"){
                F_HomePasajero(usuario = usuario!!, navController =navController ,  correo )
            }

        }
        if(pantalla=="Perfil"){

            if (usuario!!.usu_tipo=="Conductor"){
                F_PerfilConductor(usuario = usuario!!, vehiculo = vehiculo!!, navController =navController , correo = correo )

            }
            else if (usuario!!.usu_tipo=="Pasajero"){
                F_PerfilPasajero(usuario = usuario!!, navController =navController , correo = correo )
            }
        }
    }
}


