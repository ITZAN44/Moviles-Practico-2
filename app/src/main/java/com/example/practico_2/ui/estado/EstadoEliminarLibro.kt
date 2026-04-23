package com.example.practico_2.ui.estado

sealed class EstadoEliminarLibro {
    object Ideal : EstadoEliminarLibro()
    object Cargando : EstadoEliminarLibro()
    object Exito : EstadoEliminarLibro()
    data class Error(val mensaje: String) : EstadoEliminarLibro()
}
