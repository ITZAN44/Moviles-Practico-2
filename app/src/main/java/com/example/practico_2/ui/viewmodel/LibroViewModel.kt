package com.example.practico_2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practico_2.datos.modelo.LibroRequest
import com.example.practico_2.datos.repositorio.LibroRepositorio
import com.example.practico_2.ui.estado.EstadoCrearLibro
import com.example.practico_2.ui.estado.EstadoDetalleLibro
import com.example.practico_2.ui.estado.EstadoEditarLibro
import com.example.practico_2.ui.estado.EstadoGeneros
import com.example.practico_2.ui.estado.EstadoLibros
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

    private val _estadoGeneros = MutableStateFlow<EstadoGeneros>(EstadoGeneros.Cargando)
    val estadoGeneros: StateFlow<EstadoGeneros> = _estadoGeneros

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
                    // Asignar géneros uno por uno (o como pida la API)
                    libroCreado.id?.let { libroId ->
                        generosSeleccionados.forEach { generoId ->
                            repositorio.agregarGeneroALibro(libroId, generoId)
                        }
                    }
                    _estadoCrear.value = EstadoCrearLibro.Exito
                    obtenerLibros() // Refrescar lista
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
}
