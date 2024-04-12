package com.example.poqueapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.get
import androidx.navigation.navArgument
import com.example.poqueapi.presentation.custom.TopBar
import com.example.poqueapi.presentation.pokemon.detail.PokemonDetailScreen
import com.example.poqueapi.presentation.pokemon.list.PokemonListScreen
import com.example.poqueapi.presentation.theme.PoqueApiTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Init Activity")
        Timber.d("Base URL: ${BuildConfig.BASE_URL}")
        setContent {
            PoqueApiTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    topBar = { TopBar(navController = navController) },
                ) { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = "pokemon-list",
                    ) {
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
               /* Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonListScreen(snackbarHostState = snackbarHostState)
//                    PokemonListScreen(navigateToDetail = , snackbarHostState = )
//                    Greeting("Android")
                }*/
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