package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.practico_2.datos.modelo.Libro
import com.example.practico_2.ui.estado.EstadoLibros
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros(
    viewModel: LibroViewModel,
    alVerDetalle: (Int) -> Unit,
    alCrearLibro: () -> Unit,
    alVerGeneros: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Libros") },
                actions = {
                    IconButton(onClick = alVerGeneros) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Ver Géneros")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = alCrearLibro) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Libro")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (estado) {
                is EstadoLibros.Cargando -> CircularProgressIndicator()
                is EstadoLibros.Vacio -> Text("No hay libros disponibles")
                is EstadoLibros.Error -> Text("Error: ${(estado as EstadoLibros.Error).mensaje}")
                is EstadoLibros.Exito -> {
                    val libros = (estado as EstadoLibros.Exito).libros
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(libros) { libro ->
                            ItemLibro(libro, onClick = { libro.id?.let { alVerDetalle(it) } })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemLibro(libro: Libro, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = libro.imagen,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = libro.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = libro.autor, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
