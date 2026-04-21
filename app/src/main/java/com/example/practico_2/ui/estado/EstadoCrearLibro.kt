package com.example.practico_2.ui.estado

sealed class EstadoCrearLibro {
    object Ideal : EstadoCrearLibro()
    object Cargando : EstadoCrearLibro()
    object Exito : EstadoCrearLibro()
    data class Error(val mensaje: String) : EstadoCrearLibro()
}
