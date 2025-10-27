package com.gt.uvg.rickandmorty.presentation.loginFeature

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gt.uvg.rickandmorty.R
import com.gt.uvg.rickandmorty.presentation.AppRoutes
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
private fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 64.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.rickmorty_logo), contentDescription = "Logo")

            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(LoginScreenEvent.UpdateName(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.syncing
            )

            Button(
                onClick = { onEvent(LoginScreenEvent.LogInClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.syncing
            ) {
                if (state.syncing) {
                    Text(text = "Cargando...")
                } else {
                    Text(text = "Iniciar sesión")
                }
            }
        }
        Text(
            text = "Juan Carlos Durini - #1201613",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

fun NavGraphBuilder.loginRoute(
    navigateToLoggedFlow: () -> Unit
) {
    composable<AppRoutes.Login> {
        val viewModel: LoginViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(state.finishedSync) {
            if (state.finishedSync) {
                navigateToLoggedFlow()
            }
        }

        LoginScreen(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLoginScreen() {
    RickAndMortyTheme {
        Surface {
            LoginScreen(
                state = LoginScreenState(),
                onEvent = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}