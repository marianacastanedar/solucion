package com.gt.uvg.rickandmorty.presentation.characterFeature.profile

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.gt.uvg.rickandmorty.data.CharacterDb
import com.gt.uvg.rickandmorty.presentation.AppRoutes
import com.gt.uvg.rickandmorty.presentation.characterFeature.CharacterRoutes
import com.gt.uvg.rickandmorty.presentation.components.ErrorLayout
import com.gt.uvg.rickandmorty.presentation.components.LoadingLayout
import com.gt.uvg.rickandmorty.presentation.model.CharacterUi
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterProfileScreen(
    isLoading: Boolean,
    isError: Boolean,
    character: CharacterUi?,
    onNavigateBack: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Character Detail")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                )
            )
        }
    ) {
        when {
            isLoading -> LoadingLayout(
                modifier = Modifier.fillMaxSize().padding(it)
            )
            isError -> ErrorLayout(
                modifier = Modifier.fillMaxSize().padding(it),
                onRetryClick = onRetryClick
            )
            character != null -> CharacterProfileSuccessState(
                character = character,
                modifier = Modifier.fillMaxSize().padding(it)
            )
        }
    }
}

@Composable
private fun CharacterProfileSuccessState(
    character: CharacterUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
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
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            CharacterProfilePropItem(
                title = "Species:",
                value = character.species,
                modifier = Modifier.fillMaxWidth()
            )
            CharacterProfilePropItem(
                title = "Status:",
                value = character.status,
                modifier = Modifier.fillMaxWidth()
            )
            CharacterProfilePropItem(
                title = "Gender:",
                value = character.gender,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun CharacterProfilePropItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title)
        Text(text = value)
    }
}

fun NavGraphBuilder.characterProfileRoute(
    onNavigateBack: () -> Unit
) {
    composable<CharacterRoutes.CharacterProfile> { backstackEntry ->
        val route: CharacterRoutes.CharacterProfile = backstackEntry.toRoute()
        val viewModel: CharacterProfileViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.fetchData(route.id)
        }

        CharacterProfileScreen(
            isLoading = state.isLoading,
            isError = state.isError,
            character = state.data,
            onNavigateBack = onNavigateBack,
            onRetryClick = {
                viewModel.fetchData(route.id)
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun PreviewCharacterProfileScreen_LoadingState() {
    RickAndMortyTheme {
        Surface {
            CharacterProfileScreen(
                isLoading = true,
                isError = false,
                character = null,
                onNavigateBack = { },
                onRetryClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterProfileScreen_ErrorState() {
    RickAndMortyTheme {
        Surface {
            CharacterProfileScreen(
                isLoading = false,
                isError = true,
                character = null,
                onNavigateBack = { },
                onRetryClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCharacterProfileScreen_SuccessState() {
    RickAndMortyTheme {
        Surface {
            CharacterProfileScreen(
                isLoading = false,
                isError = false,
                character = CharacterUi(
                    id = 2565,
                    name = "Rick",
                    status = "Alive",
                    species = "Human",
                    gender = "Male",
                    image = ""
                ),
                onNavigateBack = { },
                onRetryClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}