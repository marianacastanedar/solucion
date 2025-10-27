package com.gt.uvg.rickandmorty.presentation.profileFeature

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.gt.uvg.rickandmorty.presentation.loggedFlow.LoggedFlowRoutes
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun ProfileScreen(
    state: ProfileScreenState,
    onLogOutClick: () -> Unit,
    onClearUsername: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Person",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Nombre:")
            Text(text = state.username)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Carné:")
            Text(text = "1201613")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = {
            onClearUsername()
            onLogOutClick()
        }) {
            Text("Cerrar sesión")
        }
    }
}

fun NavGraphBuilder.profileRoute(
    onLogOutClick: () -> Unit
) {
    composable<LoggedFlowRoutes.Profile>{
        val viewModel: ProfileViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        ProfileScreen(
            state = state,
            onLogOutClick = onLogOutClick,
            onClearUsername = viewModel::clearUsername,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewProfileScreen() {
    RickAndMortyTheme {
        Surface {
            ProfileScreen(
                state = ProfileScreenState(username = "Juan Carlos Durini"),
                onLogOutClick = { /*TODO*/ },
                onClearUsername = { /*TODO*/ },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}