package com.example.curdfirestore.NivelAplicacion

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/api/usuario/{id}")
    suspend fun pasarUsuario(@Path("id") usuarioId: String): UserData //Obtiene los datos de un id dado
    @GET("/api/vehiculo/{id}")
    suspend fun pasarVehiculo(@Path("id") vehiculoId: String): VehicleData //Obtiene los datos de un id dado
    //Agregado 22/11/2023
    @POST("/api/registrarviaje") // Reemplaza con la ruta de tu endpoint
    fun enviarViaje(@Body viajeData: ViajeData): Call<RespuestaApi>
    //Agregado 10/12/2023
    @POST("/api/registrarhorario") // Reemplaza con la ruta de tu endpoint
    fun enviarHorario(@Body horarioData: HorarioData): Call<RespuestaApi>
    @POST("/api/registrarparada") // Reemplaza con la ruta de tu endpoint
    fun enviarParada(@Body paradaData: ParadaData): Call<RespuestaApi>
    @PUT("/api/modificarviaje/{id}")
    fun modificarViaje(@Path("id") viajeId: String, @Body datosModificados: ViajeData): Call<RespuestaApi>
    //Agregado 02/12/2023
    @GET("/api/viaje/{id}")
    suspend fun pasarViaje(@Path("id") viajeId: String): ViajeData //Obtener los datos del viaje con un id dado
    @GET("/api/paradas/{id}")
    suspend fun pasarParadas(@Path("id") viajeId: String): List<ParadaData> // Obtener una lista de paradas para el viaje con el ID dado
    @GET("/api/itinerarioviajes/{id}")
    suspend fun obtenerItinerarioCon(@Path("id") userId: String): List<ViajeDataReturn> // Obtener una lista de viajes para el viaje con el ID dado

    //Agregado 10/12/2023 ---------
    @GET("/api/busquedaviajes/{id}")
    suspend fun busquedaViajesPas(@Path("id") userId: String): Response<List<ViajeDataReturn>>// Obtener una lista de paradas para las caracteristicas del horario del pasajero

    @GET("/api/busquedaparadas/{id}")
    suspend fun busquedaParadasPas(@Path("id") userId: String): List<ParadaData> // Obtener una lista de paradas para las caracteristicas del horario del pasajero
    @GET("/api/horario/{id}")
    suspend fun pasarHorario(@Path("id") viajeId: String): HorarioData //Obtener los datos del horario con un id dado

    //Agregado 13/12/2023
    @GET("/api/itinerarioviajespasajero/{id}")
    suspend fun obtenerItinerarioPas(@Path("id") userId: String): List<HorarioDataReturn> // Obtener una lista de viajes para el viaje con el ID dado
    @POST("/api/registrarsolicitud") // Reemplaza con la ruta de tu endpoint
    fun enviarSolicitud(@Body solicitudData: SolicitudData): Call<RespuestaApi>
    //Agregado 15/12/2023 -- Obtener las solictudes del conductor
    @GET("/api/solicitudesconductor/{id}")
    suspend fun obtenerSolicitudesCon(@Path("id") userId: String): Response<List<SolicitudData>> // Obtener una lista de solicitudes con el id dado
    @GET("/api/solicitudespasajero/{id}")
    suspend fun obtenerSolicitudesPas(@Path("id") userId: String): Response<List<SolicitudData>> // Obtener una lista de solicitudes con el id dado


    @GET("/api/obtenerparada/{id}")
    suspend fun obtenerParada(@Path("id") paradaId: String): ParadaData
    //Agregado 16/12/2023
    @GET("/api/solicitudesparada/{id}")
    suspend fun obtenerSolicitudesParada(@Path("id") paradaId: String): Response<List<SolicitudData>> // Obtener una lista de solicitudes con el id dado



    //Modificar status de la solicitud
    @PUT("/api/modificarstatussolicitud/{id}/{status}")
    fun modificarStatusSoli(@Path("id") viajeId: String, @Path("status") status: String): Call<RespuestaApi>

    @PUT("/api/modificarsolicitudhorario/{id}/{status}")
    fun modificarStatusSoliHorario(@Path("id") horarioId: String, @Path("status") status: String): Call<RespuestaApi>

    @GET("/api/consultasolicitudes/{iduser}/{idhorario}/{status}")
    suspend fun obtenerItiSolicitudesPas(@Path("iduser") userId: String,@Path("idhorario") horaId: String, @Path("status") status: String
    ): List<SolicitudData>

    //Agregado 17/12/2023
    @POST("/api/registrarreporte") // Reemplaza con la ruta de tu endpoint
    fun enviarReporte(@Body reporteData: ReporteData): Call<RespuestaApi>

    //Agregado 18/12/2023
    @POST("/api/registrarimprevisto") // Reemplaza con la ruta de tu endpoint
    fun enviarImprevisto(@Body imprevistoData: ImprevistoData): Call<RespuestaApi>
    // Obtener una lista de viajes par

//---------------


}