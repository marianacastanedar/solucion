package com.gt.uvg.rickandmorty.presentation.characterFeature

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.gt.uvg.rickandmorty.presentation.characterFeature.list.characterListRoute
import com.gt.uvg.rickandmorty.presentation.characterFeature.profile.characterProfileRoute
import com.gt.uvg.rickandmorty.presentation.loggedFlow.LoggedFlowRoutes

fun NavGraphBuilder.characterFeatureGraph(
    navController: NavController
) {
    navigation<LoggedFlowRoutes.CharactersGraph>(
        startDestination = CharacterRoutes.CharacterList
    ) {
        characterListRoute(
            onCharacterClick = {
                navController.navigate(CharacterRoutes.CharacterProfile(it))
            }
        )
        characterProfileRoute(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}