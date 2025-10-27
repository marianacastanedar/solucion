package com.gt.uvg.rickandmorty.presentation.loggedFlow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.gt.uvg.rickandmorty.presentation.characterFeature.CharacterRoutes
import com.gt.uvg.rickandmorty.presentation.locationFeature.LocationRoutes
import com.gt.uvg.rickandmorty.presentation.loggedFlow.LoggedFlowRoutes

data class NavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destination: LoggedFlowRoutes,
)

val navigationItems = listOf(
    NavItem(
        title = "Characters",
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        destination = LoggedFlowRoutes.CharactersGraph
    ),
    NavItem(
        title = "Locations",
        selectedIcon = Icons.Filled.LocationOn,
        unselectedIcon = Icons.Outlined.LocationOn,
        destination = LoggedFlowRoutes.LocationsGraph
    ),
    NavItem(
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        destination = LoggedFlowRoutes.Profile
    )
)
