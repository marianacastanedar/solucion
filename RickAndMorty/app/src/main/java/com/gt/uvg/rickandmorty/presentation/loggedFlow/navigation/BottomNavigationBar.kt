package com.gt.uvg.rickandmorty.presentation.loggedFlow.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gt.uvg.rickandmorty.presentation.loggedFlow.LoggedFlowRoutes

@Composable
fun BottomNavBar(
    checkItemSelected: (LoggedFlowRoutes) -> Boolean,
    onNavItemClick: (LoggedFlowRoutes) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        navigationItems.forEach { navItem ->
            val isItemSelected = checkItemSelected(navItem.destination)
            NavigationBarItem(
                selected = isItemSelected,
                label = { Text(navItem.title) },
                onClick = {
                    onNavItemClick(navItem.destination)
                },
                icon = {
                    Icon(
                        imageVector = if (isItemSelected) {
                            navItem.selectedIcon
                        } else navItem.unselectedIcon,
                        contentDescription = navItem.title
                    )
                })
        }
    }
}