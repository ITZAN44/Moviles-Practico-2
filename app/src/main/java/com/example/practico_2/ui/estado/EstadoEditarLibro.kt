package com.example.practico_2.ui.estado

sealed class EstadoEditarLibro {
    object Ideal : EstadoEditarLibro()
    object Cargando : EstadoEditarLibro()
    object Exito : EstadoEditarLibro()
    data class Error(val mensaje: String) : EstadoEditarLibro()
}
