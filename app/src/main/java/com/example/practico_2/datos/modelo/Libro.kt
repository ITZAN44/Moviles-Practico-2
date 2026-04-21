package com.example.practico_2.datos.modelo

data class Libro(
    val id: Int? = null,
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Int,
    val generos: List<Genero>? = null
)

data class LibroRequest(
    val nombre: String,
    val autor: String,
    val editorial: String,
    val imagen: String,
    val sinopsis: String,
    val isbn: String,
    val calificacion: Int
)
