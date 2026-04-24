package com.example.practico_2.ui.estado

sealed class EstadoEliminarGenero {
    object Ideal : EstadoEliminarGenero()
    object Cargando : EstadoEliminarGenero()
    object Exito : EstadoEliminarGenero()
    data class Error(val mensaje: String) : EstadoEliminarGenero()
}
