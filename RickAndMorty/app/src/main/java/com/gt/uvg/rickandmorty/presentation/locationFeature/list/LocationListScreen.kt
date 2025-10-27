package com.gt.uvg.rickandmorty.presentation.locationFeature.list

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.gt.uvg.rickandmorty.data.LocationDb
import com.gt.uvg.rickandmorty.presentation.components.ErrorLayout
import com.gt.uvg.rickandmorty.presentation.components.LoadingLayout
import com.gt.uvg.rickandmorty.presentation.locationFeature.LocationRoutes
import com.gt.uvg.rickandmorty.presentation.model.Location
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationListScreen(
    isLoading: Boolean,
    isError: Boolean,
    locations: List<Location>,
    onLocationClick: (Int) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations")
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
            else -> LocationListSuccessState(
                locations = locations,
                onLocationClick = onLocationClick,
                modifier = Modifier.fillMaxSize().padding(it)
            )
        }
    }
}

@Composable
private fun LocationListSuccessState(
    locations: List<Location>,
    onLocationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(locations) { item ->
            LocationItem(
                location = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLocationClick(item.id) }
            )
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
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
        Column {
            Text(text = location.name)
            Text(
                text = location.type,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

fun NavGraphBuilder.locationsListRoute(
    onLocationClick: (Int) -> Unit,
) {
    composable<LocationRoutes.LocationList> {
        val viewModel: LocationListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        LocationListScreen(
            isLoading = state.isLoading,
            isError = state.isError,
            locations = state.data,
            onLocationClick = onLocationClick,
            onRetryClick = viewModel::fetchData,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun PreviewLocationListScreen_LoadingState() {
    RickAndMortyTheme {
        Surface {
            LocationListScreen(
                isLoading = true,
                isError = false,
                locations = listOf(),
                onLocationClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLocationListScreen_ErrorState() {
    RickAndMortyTheme {
        Surface {
            LocationListScreen(
                isLoading = false,
                isError = true,
                locations = listOf(),
                onLocationClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewLocationListScreen_SuccessState() {
    RickAndMortyTheme {
        Surface {
            val locationDb = LocationDb()
            LocationListScreen(
                isLoading = false,
                isError = false,
                locations = locationDb.getAllLocations(),
                onLocationClick = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}