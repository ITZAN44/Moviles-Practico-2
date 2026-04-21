package com.example.practico_2.ui.estado

import com.example.practico_2.datos.modelo.Genero

sealed class EstadoGeneros {
    object Cargando : EstadoGeneros()
    data class Exito(val generos: List<Genero>) : EstadoGeneros()
    data class Error(val mensaje: String) : EstadoGeneros()
}
