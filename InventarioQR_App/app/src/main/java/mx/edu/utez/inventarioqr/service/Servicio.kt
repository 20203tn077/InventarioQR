package mx.edu.utez.inventarioqr.service

import mx.edu.utez.inventarioqr.model.*
import retrofit2.Call
import retrofit2.http.*

interface Servicio {
    @GET(value="articulo")
    fun getArticulos (): Call<RespuestaArticulos>

    @GET(value="articulo/{codigo}")
    fun getArticulo (@Path(value = "codigo") codigo: Long): Call<RespuestaArticulo>

    @GET(value="articulo/categoria/{id}")
    fun getArticulosByCategoria (@Path(value = "id") id: Int): Call<RespuestaArticulos>

    @GET(value="categoria")
    fun getCategorias (): Call<RespuestaCategorias>

    @POST(value="articulo")
    fun postArticulo (@Body articulo: Articulo): Call<Respuesta>

    @DELETE(value = "articulo/{codigo}")
    fun deleteArticulo (@Path(value = "codigo") codigo: Long): Call<Respuesta>
}