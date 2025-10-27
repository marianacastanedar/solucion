package com.gt.uvg.rickandmorty.presentation.loggedFlow

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gt.uvg.rickandmorty.presentation.AppRoutes
import com.gt.uvg.rickandmorty.presentation.characterFeature.characterFeatureGraph
import com.gt.uvg.rickandmorty.presentation.locationFeature.locationFeatureGraph
import com.gt.uvg.rickandmorty.presentation.loggedFlow.navigation.BottomNavBar
import com.gt.uvg.rickandmorty.presentation.profileFeature.profileRoute

@Composable
fun MainFlowScreen(
    onLogOutClick: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            BottomNavBar(
                /*
                 * Determina si el item del bottom bar debe estar resaltado verificando
                 * si su destino coincide con alguna pantalla en la jerarquía de navegación actual.
                 *
                 * Ejemplo: En CharacterProfile, la jerarquía es:
                 * CharacterGraph -> CharacterList -> CharacterProfile
                 * Retorna true si el destino del bottom bar coincide con cualquiera de estas pantallas.
                 */
                checkItemSelected = { destination ->
                    currentDestination?.hierarchy?.any { it.hasRoute(destination::class) } ?: false
                },
                onNavItemClick = { destination ->
                    navController.navigate(destination) {
                        /*
                         * Configuración de navegación para bottom bar:
                         * - Evita pantallas duplicadas en el backstack
                         * - Previene crear nueva instancia al hacer click repetido
                         * - Restaura el estado previo al navegar entre tabs
                         */
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoggedFlowRoutes.CharactersGraph,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            characterFeatureGraph(navController)
            locationFeatureGraph(navController)
            profileRoute(
                onLogOutClick = onLogOutClick
            )
        }
    }
}

fun NavGraphBuilder.loggedFlowRoute(
    onLogOutClick: () -> Unit
) {
    composable<AppRoutes.LoggedFlow> {
        MainFlowScreen(
            onLogOutClick = onLogOutClick
        )
    }
}