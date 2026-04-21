package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.practico_2.ui.estado.EstadoDetalleLibro
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleLibro(idLibro: Int, viewModel: LibroViewModel, alVolver: () -> Unit, alEditar: (Int) -> Unit) {
    val estado by viewModel.estadoDetalle.collectAsState()

    LaunchedEffect(idLibro) {
        viewModel.obtenerLibroPorId(idLibro)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Libro") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
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
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Button(onClick = { alEditar(libro.id ?: 0) }) {
                                Text("Editar")
                            }
                            Button(
                                onClick = { /* TODO: Eliminar */ },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
