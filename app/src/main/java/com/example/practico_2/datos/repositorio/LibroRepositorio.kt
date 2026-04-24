package com.example.practico_2.datos.repositorio

import com.example.practico_2.datos.modelo.Genero
import com.example.practico_2.datos.modelo.Libro
import com.example.practico_2.datos.modelo.LibroRequest
import com.example.practico_2.datos.remoto.ApiService

class LibroRepositorio(private val apiServicio: ApiService) {
    suspend fun obtenerLibros(): Result<List<Libro>> {
        return try {
            val respuesta = apiServicio.obtenerLibros()
            if (respuesta.isSuccessful) {
                Result.success(respuesta.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener libros: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerLibroPorId(id: Int): Result<Libro> {
        return try {
            val respuesta = apiServicio.obtenerLibroPorId(id)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error al obtener detalle del libro: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerGeneros(): Result<List<Genero>> {
        return try {
            val respuesta = apiServicio.obtenerGeneros()
            if (respuesta.isSuccessful) {
                Result.success(respuesta.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener géneros: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearLibro(libro: LibroRequest): Result<Libro> {
        return try {
            val respuesta = apiServicio.crearLibro(libro)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error al crear libro: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarLibro(id: Int, libro: LibroRequest): Result<Libro> {
        return try {
            val respuesta = apiServicio.actualizarLibro(id, libro)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar libro: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarLibro(id: Int): Result<Unit> {
        return try {
            val respuesta = apiServicio.eliminarLibro(id)
            if (respuesta.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar libro: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearGenero(genero: Genero): Result<Genero> {
        return try {
            val respuesta = apiServicio.crearGenero(genero)
            if (respuesta.isSuccessful && respuesta.body() != null) {
                Result.success(respuesta.body()!!)
            } else {
                Result.failure(Exception("Error al crear género: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarGenero(id: Int): Result<Unit> {
        return try {
            val respuesta = apiServicio.eliminarGenero(id)
            if (respuesta.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar género: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun agregarGeneroALibro(libroId: Int, generoId: Int): Result<Unit> {
        return try {
            val respuesta = apiServicio.agregarGeneroALibro(mapOf("libro_id" to libroId, "genero_id" to generoId))
            if (respuesta.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al asignar género: ${respuesta.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
