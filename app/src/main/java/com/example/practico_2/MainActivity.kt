package com.example.practico_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practico_2.datos.remoto.RetrofitCliente
import com.example.practico_2.datos.repositorio.LibroRepositorio
import com.example.practico_2.ui.theme.Practico2Theme
import com.example.practico_2.ui.viewmodel.LibroViewModel
import com.example.practico_2.ui.viewmodel.LibroViewModelFactory
import com.example.practico_2.ui.vistas.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practico2Theme {
                val repositorio = LibroRepositorio(RetrofitCliente.apiServicio)
                val viewModel: LibroViewModel = viewModel(
                    factory = LibroViewModelFactory(repositorio)
                )
                Principal(viewModel)
            }
        }
    }
}

@Composable
fun Principal(viewModel: LibroViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaLibros(
                viewModel = viewModel,
                alVerDetalle = { id ->
                    navController.navigate("detalle/$id")
                },
                alCrearLibro = {
                    navController.navigate("crear")
                },
                alVerGeneros = {
                    navController.navigate("generos")
                }
            )
        }
        composable("generos") {
            PantallaGeneros(
                viewModel = viewModel,
                alVolver = { navController.popBackStack() },
                alCrearGenero = { navController.navigate("crear-genero") }
            )
        }
        composable("crear-genero") {
            PantallaCrearGenero(
                viewModel = viewModel,
                alTerminar = { navController.popBackStack() }
            )
        }
        composable("crear") {
            PantallaCrearLibro(
                viewModel = viewModel,
                alTerminar = { navController.popBackStack() }
            )
        }
        composable(
            route = "editar/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            PantallaEditarLibro(
                idLibro = id,
                viewModel = viewModel,
                alTerminar = { navController.popBackStack() }
            )
        }
        composable(
            route = "detalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            PantallaDetalleLibro(
                idLibro = id,
                viewModel = viewModel,
                alVolver = { navController.popBackStack() },
                alEditar = { idLibro ->
                    navController.navigate("editar/$idLibro")
                }
            )
        }
    }
}
