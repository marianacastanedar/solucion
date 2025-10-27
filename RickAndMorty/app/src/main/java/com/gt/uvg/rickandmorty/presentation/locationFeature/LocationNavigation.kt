package com.gt.uvg.rickandmorty.presentation.locationFeature

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.gt.uvg.rickandmorty.presentation.locationFeature.list.locationsListRoute
import com.gt.uvg.rickandmorty.presentation.locationFeature.profile.locationProfileRoute
import com.gt.uvg.rickandmorty.presentation.loggedFlow.LoggedFlowRoutes

fun NavGraphBuilder.locationFeatureGraph(
    navController: NavController
) {
    navigation<LoggedFlowRoutes.LocationsGraph>(
        startDestination = LocationRoutes.LocationList
    ) {
        locationsListRoute(
            onLocationClick = {
                navController.navigate(LocationRoutes.LocationDetail(it))
            }
        )
        locationProfileRoute(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}