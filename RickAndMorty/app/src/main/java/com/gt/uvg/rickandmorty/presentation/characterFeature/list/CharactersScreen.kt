package com.gt.uvg.rickandmorty.presentation.characterFeature.list

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.gt.uvg.rickandmorty.data.CharacterDb
import com.gt.uvg.rickandmorty.presentation.characterFeature.CharacterRoutes
import com.gt.uvg.rickandmorty.presentation.components.ErrorLayout
import com.gt.uvg.rickandmorty.presentation.components.LoadingLayout
import com.gt.uvg.rickandmorty.presentation.model.CharacterUi
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

fun NavGraphBuilder.characterListRoute(
    onCharacterClick: (Int) -> Unit,
) {
    composable<CharacterRoutes.CharacterList> {
        val viewModel: CharactersViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        CharacterListScreen(
            isLoading = state.isLoading,
            isError = state.isError,
            characters = state.data,
            onRetryClick = viewModel::fetchData,
            onCharacterClick = onCharacterClick,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListScreen(
    isLoading: Boolean,
    isError: Boolean,
    characters: List<CharacterUi>,
    onCharacterClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopAppBar(
                title = {
                    Text("Characters")
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
            else -> CharacterListSuccessState(
                characters = characters,
                onCharacterClick = onCharacterClick,
                modifier = Modifier.fillMaxSize().padding(it)
            )
        }
    }
}
@Composable
private fun CharacterListSuccessState(
    characters: List<CharacterUi>,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(characters) { item ->
            CharacterItem(
                character = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCharacterClick(item.id) }
            )
        }
    }
}

@Composable
private fun CharacterItem(
    character: CharacterUi,
    modifier: Modifier = Modifier
) {
    val imageBackgroundColors = listOf(
        MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.inverseSurface
    )
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            color = imageBackgroundColors.random(),
            shape = CircleShape
        ) {
            Box {
                Icon(
                    Icons.Outlined.Person, contentDescription = "Image",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Column {
            Text(text = character.name)
            Text(
                text = "${character.species} * ${character.status}",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListScreen_LoadingState() {
    RickAndMortyTheme {
        Surface {
            CharacterListScreen(
                isLoading = true,
                isError = false,
                characters = listOf(),
                onCharacterClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListScreen_ErrorState() {
    RickAndMortyTheme {
        Surface {
            CharacterListScreen(
                isLoading = false,
                isError = true,
                characters = listOf(),
                onCharacterClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListScreen_SuccessState() {
    RickAndMortyTheme {
        Surface {
            val db = CharacterDb()
            CharacterListScreen(
                isLoading = false,
                isError = false,
                characters = db.getAllCharacters(),
                onCharacterClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}