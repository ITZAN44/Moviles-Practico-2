package com.example.practico_2.datos.remoto

import com.example.practico_2.datos.modelo.Genero
import com.example.practico_2.datos.modelo.Libro
import com.example.practico_2.datos.modelo.LibroRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("libros")
    suspend fun obtenerLibros(): Response<List<Libro>>

    @GET("libros/{id}")
    suspend fun obtenerLibroPorId(@Path("id") id: Int): Response<Libro>

    @POST("libros")
    suspend fun crearLibro(@Body libro: LibroRequest): Response<Libro>

    @PUT("libros/{id}")
    suspend fun actualizarLibro(@Path("id") id: Int, @Body libro: LibroRequest): Response<Libro>

    @DELETE("libros/{id}")
    suspend fun eliminarLibro(@Path("id") id: Int): Response<Unit>

    @GET("generos")
    suspend fun obtenerGeneros(): Response<List<Genero>>

    @POST("generos")
    suspend fun crearGenero(@Body genero: Genero): Response<Genero>

    @POST("libro-generos")
    suspend fun agregarGeneroALibro(@Body cuerpo: Map<String, Int>): Response<Unit>
}
