package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practico_2.datos.modelo.LibroRequest
import com.example.practico_2.ui.estado.EstadoCrearLibro
import com.example.practico_2.ui.estado.EstadoGeneros
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearLibro(viewModel: LibroViewModel, alTerminar: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var editorial by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var sinopsis by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }
    var calificacion by remember { mutableStateOf("0") }
    
    val generosSeleccionados = remember { mutableStateListOf<Int>() }
    
    val estadoCrear by viewModel.estadoCrear.collectAsState()
    val estadoGeneros by viewModel.estadoGeneros.collectAsState()

    LaunchedEffect(estadoCrear) {
        if (estadoCrear is EstadoCrearLibro.Exito) {
            viewModel.resetearEstadoCrear()
            alTerminar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Libro") },
                navigationIcon = {
                    IconButton(onClick = alTerminar) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
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
            
            Text("Géneros", style = MaterialTheme.typography.titleSmall)
            when (estadoGeneros) {
                is EstadoGeneros.Cargando -> CircularProgressIndicator()
                is EstadoGeneros.Exito -> {
                    val generos = (estadoGeneros as EstadoGeneros.Exito).generos
                    generos.forEach { genero ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = generosSeleccionados.contains(genero.id),
                                onCheckedChange = { check ->
                                    if (check) genero.id?.let { generosSeleccionados.add(it) }
                                    else generosSeleccionados.remove(genero.id)
                                }
                            )
                            Text(genero.nombre)
                        }
                    }
                }
                is EstadoGeneros.Error -> Text("Error cargando géneros")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (estadoCrear is EstadoCrearLibro.Cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                Button(
                    onClick = {
                        if (nombre.isNotBlank() && autor.isNotBlank() && isbn.isNotBlank()) {
                            val nuevoLibro = LibroRequest(
                                nombre = nombre,
                                autor = autor,
                                editorial = editorial,
                                imagen = imagen,
                                sinopsis = sinopsis,
                                isbn = isbn,
                                calificacion = calificacion.toIntOrNull() ?: 0
                            )
                            viewModel.crearLibro(nuevoLibro, generosSeleccionados.toList())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Libro")
                }
            }

            if (estadoCrear is EstadoCrearLibro.Error) {
                Text(text = (estadoCrear as EstadoCrearLibro.Error).mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
