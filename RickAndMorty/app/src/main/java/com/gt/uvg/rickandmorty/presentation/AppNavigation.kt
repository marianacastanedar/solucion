package com.gt.uvg.rickandmorty.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.gt.uvg.rickandmorty.presentation.loggedFlow.loggedFlowRoute
import com.gt.uvg.rickandmorty.presentation.loginFeature.loginRoute
import com.gt.uvg.rickandmorty.presentation.splash.splashRoute

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Splash,
        modifier = modifier
    ) {
        splashRoute(
            onNavigateToLogin = {
                navController.navigate(AppRoutes.Login) {
                    popUpTo(0)
                }
            },
            onNavigateToLoggedFlow = {
                navController.navigate(AppRoutes.LoggedFlow) {
                    popUpTo(0)
                }
            }
        )

        loginRoute(
            navigateToLoggedFlow = {
                navController.navigate(AppRoutes.LoggedFlow) {
                    popUpTo(0)
                }
            }
        )

        loggedFlowRoute(
            onLogOutClick = {
                navController.navigate(AppRoutes.Login) {
                    popUpTo(0)
                }
            }
        )
    }
}