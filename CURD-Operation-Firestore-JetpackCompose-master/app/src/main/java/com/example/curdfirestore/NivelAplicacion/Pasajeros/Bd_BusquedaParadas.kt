package com.example.curdfirestore.NivelAplicacion.Pasajeros

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.curdfirestore.NivelAplicacion.ApiService
import com.example.curdfirestore.NivelAplicacion.BASE_URL
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.RespuestaApi
import com.example.curdfirestore.NivelAplicacion.RetrofitClient
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.VentanaNoFound
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*Primero busca un viaje que coincida con los datos que porporciono el pasajero,
de acuerdo al día y tipo de trayecto (no se considera el horario para una mayor probabilidad de
encontrar paradas. Esta busqueda se hace en el servidor.
* */
@Composable
fun ObtenerParadasPasajero(
    navController: NavController,
    correo: String,
    horarioId: String,

    ) {
    //Obtener lista de viajes (Itinerario)
    var viajes by remember { mutableStateOf<List<ViajeDataReturn>?>(null) }
    var show by rememberSaveable { mutableStateOf(false) }

    //var parada by remember { mutableStateOf<ParadaData?>(null) }
    var text by remember { mutableStateOf("") }
    var busqueda by remember { mutableStateOf(false) }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)

    LaunchedEffect(key1 = true) {
        try {
            //val  resultadoViaje = RetrofitClient.apiService.busquedaViajesPas(horarioId)
            val response = RetrofitClient.apiService.busquedaViajesPas(horarioId)
            if (response.isSuccessful) {
                viajes=response.body()
            } else {
                text="No se encontró ningún viaje que coincida con tu búsqueda"
                busqueda=true
            }
            // Haz algo con el objeto Usuario

        } catch (e: Exception) {
            text="Error al obtener viaje: $e"
            println("Error al obtener viaje: $e")
        }
    }



    if (viajes != null  && busqueda==false) {
        BusquedaParadasPasajero(navController,correo, horarioId, viajes!!) //Pantalla de home
    }
    if (busqueda==true){
        show=true
        VentanaNoFound(navController, correo,show,{show=false }, {}) //no encontro ninguna coincidencia

    }

}


@Composable
fun BusquedaParadasPasajero(
    navController: NavController,
    correo: String,
    horarioId: String,
    viajes:  List<ViajeDataReturn>
) {
    var paradas by remember { mutableStateOf<List<ParadaData>?>(null) }
    var text by remember { mutableStateOf("") }
    var busqueda by remember {
        mutableStateOf(true)
    }
    var show by remember {
        mutableStateOf(false)
    }
    //var listaParadas by remember { mutableStateOf<List<ViajeDataReturn>?>(null) }
    val listaParadas = mutableListOf<String>() // Reemplaza String con el tipo de elemento que deseas almacenar
    for (id in viajes){
        listaParadas.add(id.viaje_id)
    }
    val resultado = listaParadas.joinToString(",")
    LaunchedEffect(key1=true ) {
        try {
            val  resultadoParada = RetrofitClient.apiService.busquedaParadasPas(resultado)
            paradas=resultadoParada
            busqueda=true
        } catch (e: Exception) {
            busqueda=false
            text="Error al obtener parada: $e"
            println("Error al obtener viaje: $e")
        }
    }

    if (paradas!=null){
        //Obtenemos los datos del ultimo horario registrado y en esa cargamos la panatlla
        ObtenerHorario(navController = navController,
            correo =correo , viajes = viajes!!, paradas =paradas!!, horarioId )
    }
    if(!busqueda){
        show=true
        VentanaNoFound(navController, correo,show,{show=false }, {})
    }
}



//Crear un documento llamado Solicitud
//14/12/2023
@Composable
fun GuardarSolicitud(
    navController: NavController,
    correo: String,
    solicitudData: SolicitudData,
    idHorario:String
) {
    var show by rememberSaveable { mutableStateOf(false) }
    var controlador by remember { mutableStateOf(false) }
    var resp by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarSolicitud(solicitudData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                val respuesta = response.body()?.message ?: "Mensaje nulo"
                //val idHorario = response.body()?.userId.toString()
                resp = respuesta
                show=true
                // navController.navigate(route = "ver_itinerario_pasajero/$correo")

                controlador=true


            } else {
                resp = "Entro al else"
            }
        }
        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            TODO("Not yet implemented")
        }
    }
    )

}



