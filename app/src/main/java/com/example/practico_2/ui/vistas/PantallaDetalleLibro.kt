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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.practico_2.ui.estado.EstadoDetalleLibro
import com.example.practico_2.ui.estado.EstadoEliminarLibro
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleLibro(idLibro: Int, viewModel: LibroViewModel, alVolver: () -> Unit, alEditar: (Int) -> Unit) {
    val estado by viewModel.estadoDetalle.collectAsState()
    val estadoEliminar by viewModel.estadoEliminar.collectAsState()
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(idLibro) {
        viewModel.obtenerLibroPorId(idLibro)
    }

    LaunchedEffect(estadoEliminar) {
        if (estadoEliminar is EstadoEliminarLibro.Exito) {
            viewModel.resetearEstadoEliminar()
            alVolver()
        }
    }

    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este libro? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarLibro(idLibro)
                        mostrarDialogoEliminar = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (estado) {
                is EstadoDetalleLibro.Cargando -> CircularProgressIndicator()
                is EstadoDetalleLibro.Error -> Text("Error: ${(estado as EstadoDetalleLibro.Error).mensaje}")
                is EstadoDetalleLibro.Exito -> {
                    val libro = (estado as EstadoDetalleLibro.Exito).libro
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = libro.imagen,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = libro.nombre, style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Autor: ${libro.autor}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Editorial: ${libro.editorial}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "ISBN: ${libro.isbn}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Calificación: ${libro.calificacion}/5", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Sinopsis", style = MaterialTheme.typography.titleSmall)
                        Text(text = libro.sinopsis, style = MaterialTheme.typography.bodyMedium)
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (estadoEliminar is EstadoEliminarLibro.Cargando) {
                            CircularProgressIndicator()
                        } else {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                Button(onClick = { alEditar(libro.id ?: 0) }) {
                                    Text("Editar")
                                }
                                Button(
                                    onClick = { mostrarDialogoEliminar = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Text("Eliminar")
                                }
                            }
                        }
                        
                        if (estadoEliminar is EstadoEliminarLibro.Error) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = (estadoEliminar as EstadoEliminarLibro.Error).mensaje,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}
