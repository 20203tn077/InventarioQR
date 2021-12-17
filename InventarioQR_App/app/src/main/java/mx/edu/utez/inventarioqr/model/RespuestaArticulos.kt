package mx.edu.utez.inventarioqr.model

data class RespuestaArticulos (val exitoso: Boolean, val respuesta: List<Articulo>, val mensaje: String)