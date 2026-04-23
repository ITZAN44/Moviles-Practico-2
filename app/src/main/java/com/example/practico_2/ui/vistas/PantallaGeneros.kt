package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.practico_2.ui.estado.EstadoGeneros
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGeneros(
    viewModel: LibroViewModel,
    alVolver: () -> Unit,
    alCrearGenero: () -> Unit,
    alEliminar: (Int) -> Unit
) {
    val estado by viewModel.estadoGeneros.collectAsState()

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
                                        IconButton(onClick = { alEliminar(genero.id ?: 0) }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = MaterialTheme.colorScheme.error
                                            )
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
