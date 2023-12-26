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
import com.example.curdfirestore.NivelAplicacion.RespuestaApi
import com.example.curdfirestore.NivelAplicacion.RetrofitClient
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.F_VerParadasPasajero
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.VentanaLejos
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.VentanaNoFound

import com.example.curdfirestore.NivelAplicacion.convertirStringALatLng

import com.example.curdfirestore.NivelAplicacion.HorarioData
import com.example.curdfirestore.NivelAplicacion.HorarioDataReturn
import com.example.curdfirestore.NivelAplicacion.ParadaData
import com.example.curdfirestore.NivelAplicacion.SolicitudData
import com.example.curdfirestore.NivelAplicacion.ViajeDataReturn
import com.example.curdfirestore.NivelAplicacion.getDistance
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.F_VerPasajerosCon
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.VentanaSolicitudAceptada
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Solicitudes.VentanaSolicitudRechazada
import com.example.curdfirestore.NivelPresentacion.PresentacionConductor.VentanaNoPasajeros
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios.F_VerItinerarioPasajeroCon
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios.F_VerItinerarioPasajeroPen
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Horarios.F_VerItinerarioPasajeroSinSoli
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.Solicitudes.F_VerConductoresPas
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.VentanaSinSolicitudes
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.VentanaSolicitudesConfirmados
import com.example.curdfirestore.NivelPresentacion.PresentacionPasajero.VentanaSolicitudesPendientes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun ObtenerHorario(
    navController: NavController,
    correo: String,
    viajes:  List<ViajeDataReturn>,
    paradas:  List<ParadaData>,
    horarioId:String

) {
    var filterviajes by remember { mutableStateOf<List<ParadaData>?>(null) }

    //var filterparadas: MutableList<List<ParadaData>> = mutableListOf()
    var filterparadas by remember { mutableStateOf<List<ParadaData>?>(null) }
    var coordenadasDis by remember { mutableStateOf("") }

    var horario by remember { mutableStateOf<HorarioData?>(null) }
    var text by remember { mutableStateOf("") }
    var validar by remember { mutableStateOf(false) }
    var validarcontenido by remember { mutableStateOf(false) }

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    var show by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        try {
            val  resultadoViaje = RetrofitClient.apiService.pasarHorario(horarioId)
            horario=resultadoViaje
            // Haz algo con el objeto Usuario

        } catch (e: Exception) {
            text="Error al obtener viaje: $e"
            println("Error al obtener viaje: $e")
        }
    }



    if (horario != null) {
        var listaActual = filterviajes?.toMutableList() ?: mutableListOf()

        //Coordenadas del pasajero
        var LatHorarioDes by remember { mutableStateOf(0.0) }
        var LonHorarioDes by remember { mutableStateOf(0.0) }

        var LatHorarioOri by remember { mutableStateOf(0.0) }
        var LonHorarioOri by remember { mutableStateOf(0.0) }



        var markerLatO by remember { mutableStateOf(0.0) }
        var markerLonO by remember { mutableStateOf(0.0) }
        val markerCoordenadasLatLngDes = convertirStringALatLng(horario!!.horario_destino)
        if (markerCoordenadasLatLngDes != null) {
            LatHorarioDes = markerCoordenadasLatLngDes.latitude
            LonHorarioDes = markerCoordenadasLatLngDes.longitude
        }


        val markerCoordenadasLatLngOri = convertirStringALatLng(horario!!.horario_origen)

        if (markerCoordenadasLatLngOri != null) {
            LatHorarioOri = markerCoordenadasLatLngOri.latitude
            LonHorarioOri = markerCoordenadasLatLngOri.longitude
            // Hacer algo con las coordenadas LatLng

        } else {
            // La conversión falló
            println("Error al convertir la cadena a LatLng")
        }


        var indiceViaje = 0
        while (indiceViaje < viajes.size) {
            var indiceParada = 0
            val viaje = viajes[indiceViaje]
            var lugaresDisp=0
            try{

                // Intenta convertir el String a Int
                lugaresDisp=viaje.viaje_num_lugares.toInt()

                // Imprime el resultado
                //println("El número convertido es: $numero")


                println("Lugares $lugaresDisp")
            }catch(e: NumberFormatException){
                println("No se pudo convertir el String a Int: ${e.message}")
            }

println("Lista Actual--- $listaActual")

            if (viaje.viaje_status == "Disponible" && lugaresDisp>0) {  //Validar el numero de lugares
            if (viaje.viaje_trayecto == "0") {
                coordenadasDis = horario!!.horario_destino
            } else {
                coordenadasDis = horario!!.horario_origen
            }
            while (indiceParada < paradas.size) {
                //Verificar

                val parada = paradas[indiceParada]
                println("Ceroooo")
                println("Una parada ${parada.par_nombre}")

                val markerCoordenadasLatLngO =
                    convertirStringALatLng(parada.par_ubicacion)
                if (markerCoordenadasLatLngO != null) {
                    markerLatO = markerCoordenadasLatLngO.latitude
                    markerLonO = markerCoordenadasLatLngO.longitude
                    // Hacer algo con las coordenadas LatLng
                }

                val distance = getDistance(
                    coordenadasDis, parada.par_ubicacion
                )
                println("La distancia $distance")

                if (distance <= 1000.0f) {
                    val nuevaP =
                        ParadaData(
                            user_id = parada.user_id,
                            viaje_id = parada.viaje_id,
                            par_nombre = parada.par_nombre,
                            par_hora = parada.par_hora,
                            par_ubicacion = parada.par_ubicacion,
                            par_id = parada.par_id
                        )
                    validarcontenido = true
                    listaActual.add(nuevaP)
                    println("Lista actual $listaActual")

                }
                println("El tipo de dato de miVariable es: ${distance::class.simpleName}")
                indiceParada++
            }

            if (indiceViaje == viajes.size - 1) {
                validar = true

            }
        }
            indiceViaje++

        }

        if(validar==true) {

            if (listaActual != null && validarcontenido==true) {

                F_VerParadasPasajero(
                    navController = navController,
                    correo = correo,
                    viajeData = viajes!!,
                    paradas = listaActual!!,
                    horarioId,
                    horario = horario!!
                )
            } else {
                show = true
                VentanaLejos(navController, correo, show, { show = false }, {})
            }
            println("filtro: $listaActual")
        }
        else{
            show = true
            VentanaLejos(navController, correo, show, { show = false }, {})
        }
    }

}


//Agregado 10/12/2023 -- Trabajando en esto-------
@Composable
fun GuardarHorario(
    navController: NavController,
    correo: String,
    horarioData: HorarioData
) {
    var resp by remember { mutableStateOf("") }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val apiService = retrofit.create(ApiService::class.java)
    val call: Call<RespuestaApi> = apiService.enviarHorario(horarioData)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                val respuesta = response.body()?.message ?: "Mensaje nulo"
                val idHorario = response.body()?.userId.toString()
                resp = respuesta
                navController.navigate(route = "ver_paradas_pasajero/$correo/$idHorario")
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


@Composable
fun ObtenerItinerarioPasajero(
    navController: NavController,
    userId: String,
    tipo:String
) {
    var text by remember { mutableStateOf("") }
    var show by rememberSaveable { mutableStateOf(false) }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)
    var viajes by remember { mutableStateOf<List<HorarioDataReturn>?>(null) }
    LaunchedEffect(key1 = true) {
        try {
            val resultadoViajes = RetrofitClient.apiService.obtenerItinerarioPas(userId)
            viajes = resultadoViajes
        } catch (e: Exception) {
            text = "Error al obtener Itinerario: $e"
            println("Error al obtener Itinerario: $e")
            show = true
        }
    }

    if (viajes != null) {
        when (tipo) {
            "p" -> {
                println("TIPO p")
                F_VerItinerarioPasajeroPen(navController, userId, viajes!!)
            }
            "c" -> {
                println("TIPO C")
                F_VerItinerarioPasajeroCon(navController, userId, viajes!!)
            }
            "s" -> {
                println("TIPO s")
                F_VerItinerarioPasajeroSinSoli(navController, userId, viajes!!)
            }
        }
    } else {
        when (tipo) {
            "p" -> {
                VentanaSolicitudesPendientes(navController, userId, show, { show = false }, {})
            }
            "c" -> {
                VentanaSolicitudesConfirmados(navController, userId, show, { show = false }, {})
            }
            "s" -> {
                VentanaSinSolicitudes(navController, userId, show, { show = false }, {})

            }
        }
    }
}

//Modificar que un horario tiene una solicitud
@Composable
fun RegistraSolicitudHorario(
    navController: NavController,
    userId: String,
    horarioId: String,
    status:String

) {
    println("Modifica horarioooo")
    var show by rememberSaveable { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf(false) }
    var show1 by remember { mutableStateOf(false) }
    var show2 by remember { mutableStateOf(false) }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService = retrofit.create(ApiService::class.java)
    //Obtener lista de viajes (Itinerario)


    val call = apiService.modificarStatusSoliHorario(horarioId, status)
    call.enqueue(object : Callback<RespuestaApi> {
        override fun onResponse(call: Call<RespuestaApi>, response: Response<RespuestaApi>) {
            if (response.isSuccessful) {
                // La modificación fue exitosa
                val respuestaApi = response.body()
                //     navController.navigate(route = "ver_itinerario_pasajero/$userId")


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


}

//Agrrgado 24/12/2023
//MODIFICADO 19/12/23
@Composable
fun ObtenerConductoresPasajero(
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
        val response = RetrofitClient.apiService.obtenerSolicitudesPas(userId)
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
        F_VerConductoresPas(navController, userId,
            solicitudes!!
        )
        //BusquedaParadasPasajero(navController,correo, horarioId, viajes!!) //Pantalla de home
    }
    if (busqueda==true){
        VentanaNoPasajeros(navController, userId,show,{show=false }, {})

    }
}

