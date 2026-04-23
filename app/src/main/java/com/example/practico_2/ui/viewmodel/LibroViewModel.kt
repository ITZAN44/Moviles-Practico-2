package com.example.practico_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_2.datos.modelo.Genero
import com.example.practico_2.datos.modelo.LibroRequest
import com.example.practico_2.datos.repositorio.LibroRepositorio
import com.example.practico_2.ui.estado.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LibroViewModel(private val repositorio: LibroRepositorio) : ViewModel() {

    private val _estado = MutableStateFlow<EstadoLibros>(EstadoLibros.Cargando)
    val estado: StateFlow<EstadoLibros> = _estado

    private val _estadoDetalle = MutableStateFlow<EstadoDetalleLibro>(EstadoDetalleLibro.Cargando)
    val estadoDetalle: StateFlow<EstadoDetalleLibro> = _estadoDetalle

    private val _estadoCrear = MutableStateFlow<EstadoCrearLibro>(EstadoCrearLibro.Ideal)
    val estadoCrear: StateFlow<EstadoCrearLibro> = _estadoCrear

    private val _estadoEditar = MutableStateFlow<EstadoEditarLibro>(EstadoEditarLibro.Ideal)
    val estadoEditar: StateFlow<EstadoEditarLibro> = _estadoEditar

    private val _estadoEliminar = MutableStateFlow<EstadoEliminarLibro>(EstadoEliminarLibro.Ideal)
    val estadoEliminar: StateFlow<EstadoEliminarLibro> = _estadoEliminar

    private val _estadoGeneros = MutableStateFlow<EstadoGeneros>(EstadoGeneros.Cargando)
    val estadoGeneros: StateFlow<EstadoGeneros> = _estadoGeneros

    private val _estadoCrearGenero = MutableStateFlow<EstadoCrearGenero>(EstadoCrearGenero.Ideal)
    val estadoCrearGenero: StateFlow<EstadoCrearGenero> = _estadoCrearGenero

    init {
        obtenerLibros()
        obtenerGeneros()
    }

    fun obtenerLibros() {
        viewModelScope.launch {
            _estado.value = EstadoLibros.Cargando
            repositorio.obtenerLibros()
                .onSuccess { libros ->
                    if (libros.isEmpty()) {
                        _estado.value = EstadoLibros.Vacio
                    } else {
                        _estado.value = EstadoLibros.Exito(libros)
                    }
                }
                .onFailure { error ->
                    _estado.value = EstadoLibros.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun obtenerGeneros() {
        viewModelScope.launch {
            _estadoGeneros.value = EstadoGeneros.Cargando
            repositorio.obtenerGeneros()
                .onSuccess { generos ->
                    _estadoGeneros.value = EstadoGeneros.Exito(generos)
                }
                .onFailure { error ->
                    _estadoGeneros.value = EstadoGeneros.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun crearLibro(libro: LibroRequest, generosSeleccionados: List<Int>) {
        viewModelScope.launch {
            _estadoCrear.value = EstadoCrearLibro.Cargando
            repositorio.crearLibro(libro)
                .onSuccess { libroCreado ->
                    libroCreado.id?.let { libroId ->
                        generosSeleccionados.forEach { generoId ->
                            repositorio.agregarGeneroALibro(libroId, generoId)
                        }
                    }
                    _estadoCrear.value = EstadoCrearLibro.Exito
                    obtenerLibros()
                }
                .onFailure { error ->
                    _estadoCrear.value = EstadoCrearLibro.Error(error.message ?: "Error al crear")
                }
        }
    }

    fun resetearEstadoCrear() {
        _estadoCrear.value = EstadoCrearLibro.Ideal
    }

    fun actualizarLibro(id: Int, libro: LibroRequest) {
        viewModelScope.launch {
            _estadoEditar.value = EstadoEditarLibro.Cargando
            repositorio.actualizarLibro(id, libro)
                .onSuccess {
                    _estadoEditar.value = EstadoEditarLibro.Exito
                    obtenerLibros()
                }
                .onFailure { error ->
                    _estadoEditar.value = EstadoEditarLibro.Error(error.message ?: "Error al actualizar")
                }
        }
    }

    fun resetearEstadoEditar() {
        _estadoEditar.value = EstadoEditarLibro.Ideal
    }

    fun eliminarLibro(id: Int) {
        viewModelScope.launch {
            _estadoEliminar.value = EstadoEliminarLibro.Cargando
            repositorio.eliminarLibro(id)
                .onSuccess {
                    _estadoEliminar.value = EstadoEliminarLibro.Exito
                    obtenerLibros()
                }
                .onFailure { error ->
                    _estadoEliminar.value = EstadoEliminarLibro.Error(error.message ?: "Error al eliminar")
                }
        }
    }

    fun resetearEstadoEliminar() {
        _estadoEliminar.value = EstadoEliminarLibro.Ideal
    }

    fun obtenerLibroPorId(id: Int) {
        viewModelScope.launch {
            _estadoDetalle.value = EstadoDetalleLibro.Cargando
            repositorio.obtenerLibroPorId(id)
                .onSuccess { libro ->
                    _estadoDetalle.value = EstadoDetalleLibro.Exito(libro)
                }
                .onFailure { error ->
                    _estadoDetalle.value = EstadoDetalleLibro.Error(error.message ?: "Error desconocido")
                }
        }
    }

    fun crearGenero(nombre: String) {
        if (nombre.isBlank()) {
            _estadoCrearGenero.value = EstadoCrearGenero.Error("El nombre no puede estar vacío")
            return
        }
        viewModelScope.launch {
            _estadoCrearGenero.value = EstadoCrearGenero.Cargando
            repositorio.crearGenero(Genero(nombre = nombre))
                .onSuccess {
                    _estadoCrearGenero.value = EstadoCrearGenero.Exito
                    obtenerGeneros()
                }
                .onFailure { error ->
                    _estadoCrearGenero.value = EstadoCrearGenero.Error(error.message ?: "Error al crear género")
                }
        }
    }

    fun resetearEstadoCrearGenero() {
        _estadoCrearGenero.value = EstadoCrearGenero.Ideal
    }
}
