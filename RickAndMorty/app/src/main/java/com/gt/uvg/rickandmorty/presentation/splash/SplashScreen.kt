package com.gt.uvg.rickandmorty.presentation.splash

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gt.uvg.rickandmorty.data.model.AuthStatus
import com.gt.uvg.rickandmorty.presentation.AppRoutes
import com.gt.uvg.rickandmorty.presentation.AppViewModel

fun NavGraphBuilder.splashRoute(
    onNavigateToLogin: () -> Unit,
    onNavigateToLoggedFlow: () -> Unit
) {
    composable<AppRoutes.Splash> {
        val appViewModel: AppViewModel = viewModel()
        val authStatus by appViewModel.authStatus.collectAsStateWithLifecycle()

        LaunchedEffect(authStatus) {
            when (authStatus) {
                AuthStatus.Loading -> null
                AuthStatus.LoggedIn -> onNavigateToLoggedFlow()
                AuthStatus.LoggedOut -> onNavigateToLogin()
                AuthStatus.Error -> onNavigateToLogin()
            }
        }
    }
}
