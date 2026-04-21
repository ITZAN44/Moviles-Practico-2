package com.example.practico_2.ui.estado

import com.example.practico_2.datos.modelo.Libro

sealed class EstadoDetalleLibro {
    object Cargando : EstadoDetalleLibro()
    data class Exito(val libro: Libro) : EstadoDetalleLibro()
    data class Error(val mensaje: String) : EstadoDetalleLibro()
}
