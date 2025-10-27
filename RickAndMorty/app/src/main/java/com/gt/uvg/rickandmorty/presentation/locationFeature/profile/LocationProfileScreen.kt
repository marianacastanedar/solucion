package com.gt.uvg.rickandmorty.presentation.locationFeature.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.gt.uvg.rickandmorty.data.LocationDb
import com.gt.uvg.rickandmorty.presentation.components.ErrorLayout
import com.gt.uvg.rickandmorty.presentation.components.LoadingLayout
import com.gt.uvg.rickandmorty.presentation.locationFeature.LocationRoutes
import com.gt.uvg.rickandmorty.presentation.model.Location
import com.gt.uvg.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationProfileScreen(
    isLoading: Boolean,
    isError: Boolean,
    location: Location?,
    onNavigateBack: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            TopAppBar(
                title = {
                    Text("Locations")
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
            location != null -> LocationProfileSuccessState(
                location = location,
                modifier = Modifier.fillMaxSize().padding(it)
            )
        }
    }
}

@Composable
private fun LocationProfileSuccessState(
    location: Location,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = location.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        LocationProfilePropItem(
            title = "ID:",
            value = location.id.toString(),
            modifier = Modifier.fillMaxWidth()
        )
        LocationProfilePropItem(
            title = "Type:",
            value = location.type,
            modifier = Modifier.fillMaxWidth()
        )
        LocationProfilePropItem(
            title = "Dimensions:",
            value = location.dimension,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LocationProfilePropItem(
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

fun NavGraphBuilder.locationProfileRoute(
    onNavigateBack: () -> Unit
) {
    composable<LocationRoutes.LocationDetail> { backStackEntry ->
        val route: LocationRoutes.LocationDetail = backStackEntry.toRoute()
        val viewModel: LocationProfileViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.fetchData(route.id)
        }

        LocationProfileScreen(
            isLoading = state.isLoading,
            isError = state.isError,
            location = state.data,
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
private fun PreviewLocationProfileScreen_LoadingState() {
    RickAndMortyTheme {
        Surface {
            LocationProfileScreen(
                isLoading = true,
                isError = false,
                location = null,
                onNavigateBack = { },
                onRetryClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLocationProfileScreen_ErrorState() {
    RickAndMortyTheme {
        Surface {
            LocationProfileScreen(
                isLoading = false,
                isError = true,
                location = null,
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
private fun PreviewLocationProfileScreen_SuccessState() {
    RickAndMortyTheme {
        Surface {
            LocationProfileScreen(
                isLoading = false,
                isError = false,
                location = Location(1, "Earth (C-137)", "Planet", "Dimension C-137"),
                onNavigateBack = { },
                onRetryClick = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}