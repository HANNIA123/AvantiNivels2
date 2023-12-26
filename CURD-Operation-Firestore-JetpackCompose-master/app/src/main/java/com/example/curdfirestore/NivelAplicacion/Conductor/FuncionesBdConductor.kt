package com.example.curdfirestore.NivelAplicacion.Conductor

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
import com.example.curdfirestore.NivelAplicacion.ImprevistoData
import com.example.curdfirestore.NivelAplicacion.RespuestaApi
import com.example.curdfirestore.NivelAplicacion.RetrofitClient

import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.F_VerItinerarioConductor
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes.F_VerViajeConductor

import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.F_VerPasajerosCon
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.F_VerSolicitudesCon
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.VentanaSolicitudAceptada
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.VentanaSolicitudRechazada
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.ReporteData
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelAplicacion.ViajeData
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelPresentacion.PantallaError
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.VentanaSolicitudNoPermitida
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.disminuirLugaresViaje
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoItinearios
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoPasajeros
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoSolicitudes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ObtenerViajeRegistrado(
    navController: NavController,
    viajeId: String,
    correo: String,
    pantalla: String

) {
    var viaje by remember { mutableStateOf<ViajeData?>(null) }
    var parada by remember { mutableStateOf<ParadaData?>(null) }
    var text by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    LaunchedEffect(key1 = true) {
        try {
            val  resultadoViaje = RetrofitClient.apiService.pasarViaje(viajeId)
            viaje=resultadoViaje
            // Haz algo con el objeto Usuario

            println("Viaje obtenido: $viaje")
        } catch (e: Exception) {
            text="Error al obtener viaje: $e"
            println("Error al obtener viaje: $e")
        }
    }
    //Obtener lista de paradas
    var paradas by remember { mutableStateOf<List<ParadaData>?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            val resultadoViajes = RetrofitClient.apiService.pasarParadas(viajeId)
            paradas = resultadoViajes
            // Haz algo con la lista de ViajeData
            println("Viajes obtenidos!!!!!!!--------: $paradas")
        } catch (e: Exception) {
            text = "Error al obtener parada: $e"
            println("Error al obtener parada: $e")
        }
    }
    // Construir la interfaz de usuario utilizando el estado actualizado
    if (viaje != null && paradas !=null) {
        F_VerViajeConductor(navController,correo, viaje!!, paradas!!, pantalla, viajeId) //Pantalla de home
    }
}






//Fin agregado 10/12/2023


//Agregado 22/11/2023
@Composable
fun GuardarViaje(
    navController: NavController,
    correo: String,
    viajeData: ViajeData
){
    var resp by remember { mutableStateOf("") }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarViaje(viajeData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // Manejar la respuesta exitosa aquí
                val respuesta = response.body()?.message ?: "Mensaje nulo"
                val idViaje=response.body()?.userId.toString()
                resp=respuesta

                navController.navigate(route = "nueva_parada/$idViaje/$correo")

            } else {
                resp="Entro al else"
            }
        }
        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            TODO("Not yet implemented")
        }
    }
    )
}

@Composable
fun GuardarParada(
    navController: NavController,
    paradaData: ParadaData
){
    var resp by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarParada(paradaData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // Manejar la respuesta exitosa aquí
                val respuesta = response.body()?.message ?: "Mensaje nulo"
                resp=respuesta
                // ...
            } else {
                resp="Entro al else"
            }
        }
        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            TODO("Not yet implemented")
        }
    }
    )
}

//Esta función obtine los datos del itinerario del conductor
@Composable
fun ObtenerSolicitudesConductor(
    navController: NavController,
    userId: String,
) {
    var show by rememberSaveable {mutableStateOf(false)}
    var text by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)
    var busqueda by remember { mutableStateOf(false) }
    var solicitudes by remember { mutableStateOf<List<SolicitudData>?>(null) }
    LaunchedEffect(key1 = true) {
        val response = RetrofitClient.apiService.obtenerSolicitudesCon(userId)
        try {
            if (response.isSuccessful) {
                solicitudes=response.body()
            } else {
                text="No se encontró ningún viaje que coincida con tu búsqueda"
                busqueda=true
                show=true
            }
        } catch (e: Exception) {
            text = "Error al obtener Itinerario: $e"
            println("Error al obtener Itinerario: $e")
        }
    }

    if (solicitudes != null  && busqueda==false) {
        F_VerSolicitudesCon(navController, userId,
            solicitudes!!
        )
        //BusquedaParadasPasajero(navController,correo, horarioId, viajes!!) //Pantalla de home
    }
    if (busqueda==true){
        VentanaNoSolicitudes(navController, userId,show,{show=false }, {})

    }
}

@Composable
fun RespuestaSolicitud(
    navController: NavController,
    userId: String,
    solicitudId: String,
    status:String,
    viajeId:String

) {
    var text by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf(false) }
    var show1 by remember { mutableStateOf(false) }
    var show2 by remember { mutableStateOf(false) }
    var show3 by remember { mutableStateOf(false) }



    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)

    val call = apiService.modificarStatusSoli(solicitudId, status)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // La modificación fue exitosa
                val respuestaApi = response.body()
                // Realizar acciones adicionales si es necesario

                confirm=true
                println("Se modificaaa")

            } else {
                // Ocurrió un error, manejar según sea necesario
                // Puedes obtener más información del error desde response.errorBody()
            }
        }

        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            // Manejar errores de red o excepciones
            t.printStackTrace()
        }
    })
    if (confirm){

        if(status=="Aceptada"){

              //  disminuirLugaresViaje(viajeId, "viaje_num_lugares", newLugares.toString())
            show1=true
                VentanaSolicitudAceptada(navController, userId ,show1,{show1=false }, {})

        }
        if(status=="Rechazada"){
            show2=true
            VentanaSolicitudRechazada(navController, userId ,show2,{show2=false }, {})
        }

    }
}



//Agregado 17/12/2023
@Composable
fun GuardarReporte(
    navController: NavController,
    reporteData: ReporteData
){
    var resp by remember { mutableStateOf("") }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarReporte(reporteData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // Manejar la respuesta exitosa aquí
                val respuesta = response.body()?.message ?: "Mensaje nulo"

                resp=respuesta

            } else {
                resp="Entro al else"
            }
        }
        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            TODO("Not yet implemented")
        }
    }
    )

}


//Agregado 18/12/2023
@Composable
fun GuardarImprevisto(
    navController: NavController,
    imprevistoData: ImprevistoData
){
    var resp by remember { mutableStateOf("") }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarImprevisto(imprevistoData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // Manejar la respuesta exitosa aquí
                val respuesta = response.body()?.message ?: "Mensaje nulo"

                resp=respuesta

            } else {
                resp="Entro al else"
            }
        }
        override fun onFailure(call: Call<RespuestaApi>, t: Throwable) {
            TODO("Not yet implemented")
        }
        }
        )

}
//MODIFICADO 19/12/23
@Composable
fun ObtenerPasajerosConductor(
    navController: NavController,
    userId: String,
) {
    var show by rememberSaveable {mutableStateOf(false)}
    var text by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)
    var busqueda by remember { mutableStateOf(false) }
    var solicitudes by remember { mutableStateOf<List<SolicitudData>?>(null) }
    LaunchedEffect(key1 = true) {
        val response = RetrofitClient.apiService.obtenerSolicitudesCon(userId)
        try {
            if (response.isSuccessful) {
                solicitudes=response.body()
            } else {
                text="No se encontró ningún viaje que coincida con tu búsqueda"
                busqueda=true
                show=true
            }
        } catch (e: Exception) {
            text = "Error al obtener Itinerario: $e"
            println("Error al obtener Itinerario: $e")
        }
    }

    if (solicitudes != null  && busqueda==false) {
        F_VerPasajerosCon(navController, userId,
            solicitudes!!
        )
        //BusquedaParadasPasajero(navController,correo, horarioId, viajes!!) //Pantalla de home
    }
    if (busqueda==true){
        VentanaNoPasajeros(navController, userId,show,{show=false }, {})

    }
}



//--------------------------------------------------------------------
//MODIFICADO 19/12/23
@Composable
fun ObtenerItinerarioConductor(
    navController: NavController,
    userId: String,
) {
    var show by rememberSaveable {mutableStateOf(false)}
    var control by remember { mutableStateOf("try") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)
    var viajes by remember { mutableStateOf<List<ViajeDataReturn>?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            val resultadoViajes = RetrofitClient.apiService.obtenerItinerarioCon(userId)
            viajes = resultadoViajes
        } catch (e: Exception) {
            println("Error al obtener parada: $e")
            control="catch"
            show=true
        }
    }

    if(viajes!=null){
        if (control=="try" ) {
            F_VerItinerarioConductor(navController,userId, viajes!!)
        } else{
            VentanaNoItinearios(navController, userId,show,{show=false }, {})
        }
    } else{
        VentanaNoItinearios(navController, userId,show,{show=false }, {})
    }
}



