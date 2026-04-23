package com.example.practico_2.ui.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practico_2.ui.estado.EstadoCrearGenero
import com.example.practico_2.ui.viewmodel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearGenero(
    viewModel: LibroViewModel,
    alTerminar: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    val estado by viewModel.estadoCrearGenero.collectAsState()

    LaunchedEffect(estado) {
        if (estado is EstadoCrearGenero.Exito) {
            viewModel.resetearEstadoCrearGenero()
            alTerminar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Género") },
                navigationIcon = {
                    IconButton(onClick = alTerminar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Género") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado is EstadoCrearGenero.Error,
                supportingText = {
                    if (estado is EstadoCrearGenero.Error) {
                        Text(text = (estado as EstadoCrearGenero.Error).mensaje)
                    }
                }
            )

            if (estado is EstadoCrearGenero.Cargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.crearGenero(nombre) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Género")
                }
            }
        }
    }
}
