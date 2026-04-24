package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practico_2.datos.modelo.LibroRequest
import com.example.practico_2.ui.estado.EstadoDetalleLibro
import com.example.practico_2.ui.estado.EstadoEditarLibro
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarLibro(idLibro: Int, viewModel: LibroViewModel, alTerminar: () -> Unit) {
    val estadoDetalle by viewModel.estadoDetalle.collectAsState()
    val estadoEditar by viewModel.estadoEditar.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf("0") }

    var datosCargados by remember { mutableStateOf(false) }

    LaunchedEffect(idLibro) {
        viewModel.obtenerLibroPorId(idLibro)
    }

    LaunchedEffect(estadoDetalle) {
        if (estadoDetalle is EstadoDetalleLibro.Exito && !datosCargados) {
            val libro = (estadoDetalle as EstadoDetalleLibro.Exito).libro
            nombre = libro.nombre
            autor = libro.autor
            editorial = libro.editorial
            imagen = libro.imagen
            sinopsis = libro.sinopsis
            isbn = libro.isbn
            calificacion = libro.calificacion.toString()
            datosCargados = true
        }
    }

    LaunchedEffect(estadoEditar) {
        if (estadoEditar is EstadoEditarLibro.Exito) {
            viewModel.resetearEstadoEditar()
            alTerminar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Libro") },
                navigationIcon = {
                    IconButton(onClick = alTerminar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (estadoDetalle is EstadoDetalleLibro.Cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = editorial, onValueChange = { editorial = it }, label = { Text("Editorial") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = imagen, onValueChange = { imagen = it }, label = { Text("URL Imagen") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = isbn, onValueChange = { isbn = it }, label = { Text("ISBN") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = sinopsis, onValueChange = { sinopsis = it }, label = { Text("Sinopsis") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                
                Spacer(modifier = Modifier.height(16.dp))

                if (estadoEditar is EstadoEditarLibro.Cargando) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && autor.isNotBlank() && isbn.isNotBlank()) {
                                val libroEditado = LibroRequest(
                                    nombre = nombre,
                                    autor = autor,
                                    editorial = editorial,
                                    imagen = imagen,
                                    sinopsis = sinopsis,
                                    isbn = isbn,
                                    calificacion = calificacion.toIntOrNull() ?: 0
                                )
                                viewModel.actualizarLibro(idLibro, libroEditado)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar Cambios")
                    }
                }

                if (estadoEditar is EstadoEditarLibro.Error) {
                    Text(text = (estadoEditar as EstadoEditarLibro.Error).mensaje, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
