package mx.edu.utez.inventarioqr.model

data class Articulo(val codigo: Long, val nombre: String, val descripcion: String, val categoriaId: Categoria, val cantidad: Int)
