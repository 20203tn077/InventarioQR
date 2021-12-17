package mx.edu.utez.inventarioqr.model

data class Respuesta (val exitoso: Boolean, val respuesta: List<Articulo>, val mensaje: String)