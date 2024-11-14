package com.example.poqueapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import androidx.navigation.navArgument
import com.example.poqueapi.presentation.custom.TopBar
import com.example.poqueapi.presentation.login.LoginViewModel
import com.example.poqueapi.presentation.pokemon.detail.PokemonDetailScreen
import com.example.poqueapi.presentation.pokemon.list.PokemonListScreen
import com.example.poqueapi.presentation.theme.PoqueApiTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.poqueapi.presentation.login.AuthState
import com.example.poqueapi.presentation.login.LoginScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoqueApiTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                // Agregamos el AuthViewModel
                val authViewModel: LoginViewModel = hiltViewModel()
                val authState by authViewModel.authState.collectAsState()

                // Observar la ruta actual
                val currentRoute by navController.currentBackStackEntryAsState()
                Timber.d("Current route: ${currentRoute?.destination?.route}")

                // Efecto para manejar la navegación basada en el estado de autenticación
                LaunchedEffect(authState) {
                    when(authState) {
                        AuthState.Authenticated -> {
                            navController.navigate("pokemon-list") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        AuthState.Unauthenticated -> {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        AuthState.Loading -> {
                            Timber.i("Loding...")
                        }
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = {
                        // Modificamos la condición para mostrar la TopBar
                        when (currentRoute?.destination?.route) {
                            "login" -> { /* No mostrar TopBar */ }
                            null -> { /* No mostrar TopBar */ }
                            else -> {
                                TopBar(
                                    navController = navController,
                                    onLogoutClick = {
                                        Timber.d("Logout triggered")
                                        authViewModel.logout()
                                    }
                                )
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable(
                            route = "login",
                            label = "Iniciar sesión"
                        ) {
                            LoginScreen(
                                viewModel = authViewModel,
                                snackbarHostState = snackbarHostState
                            )
                        }
                        composable(
                            "pokemon-list",
                            label = "¡Atrápalos todos!",
                        ) {
                            PokemonListScreen(
                                navigateToDetail = { id ->
                                    navController.navigate("pokemon/$id")
                                },
                                snackbarHostState = snackbarHostState,
                            )
                        }
                        composable(
                            "pokemon/{id}",
                            label = "Pokemon",
                            arguments = listOf(navArgument("id") { type = NavType.IntType }),
                        ) {
                            PokemonDetailScreen(snackbarHostState = snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.composable(
    route: String,
    label: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(
            provider[ComposeNavigator::class],
            content
        ).apply {
            this.route = route
            this.label = label // SET LABEL
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}