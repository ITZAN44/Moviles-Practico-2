package com.example.practico_2.ui.estado

sealed class EstadoCrearGenero {
    object Ideal : EstadoCrearGenero()
    object Cargando : EstadoCrearGenero()
    object Exito : EstadoCrearGenero()
    data class Error(val mensaje: String) : EstadoCrearGenero()
}
