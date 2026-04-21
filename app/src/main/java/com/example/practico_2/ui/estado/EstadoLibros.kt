package com.example.practico_2.ui.estado

import com.example.practico_2.datos.modelo.Libro

sealed class EstadoLibros {
    object Cargando : EstadoLibros()
    data class Exito(val libros: List<Libro>) : EstadoLibros()
    data class Error(val mensaje: String) : EstadoLibros()
    object Vacio : EstadoLibros()
}
