package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practico_2.ui.estado.EstadoEliminarGenero
import com.example.practico_2.ui.estado.EstadoGeneros
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGeneros(
    viewModel: LibroViewModel,
    alVolver: () -> Unit,
    alCrearGenero: () -> Unit
) {
    val estado by viewModel.estadoGeneros.collectAsState()
    val estadoEliminar by viewModel.estadoEliminarGenero.collectAsState()
    var generoAEliminar by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(estadoEliminar) {
        if (estadoEliminar is EstadoEliminarGenero.Exito) {
            viewModel.resetearEstadoEliminarGenero()
        }
    }

    if (generoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { generoAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este género?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        generoAEliminar?.let { viewModel.eliminarGenero(it) }
                        generoAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { generoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listado de Géneros") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alCrearGenero) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Género")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (estado) {
                is EstadoGeneros.Cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EstadoGeneros.Error -> {
                    Text(
                        text = "Error: ${(estado as EstadoGeneros.Error).mensaje}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is EstadoGeneros.Exito -> {
                    val generos = (estado as EstadoGeneros.Exito).generos
                    if (generos.isEmpty()) {
                        Text(
                            text = "No hay géneros disponibles",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(generos) { genero ->
                                ListItem(
                                    headlineContent = { Text(genero.nombre) },
                                    trailingContent = {
                                        if (estadoEliminar is EstadoEliminarGenero.Cargando && generoAEliminar == genero.id) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        } else {
                                            IconButton(onClick = { generoAEliminar = genero.id }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Eliminar",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
