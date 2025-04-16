package com.example.composecharactersbase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun GhibliNavigation(
    viewModel: CharacterViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "characters") {
        composable("characters") {
            CharacterListScreen(
                viewModel = viewModel,
                onCharacterClick = { characterId ->
                    navController.navigate("details/$characterId")
                }
            )
        }
        composable(
            route = "details/{characterId}",
            arguments = listOf(navArgument("characterId") { type = NavType.StringType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId") ?: return@composable
            CharacterDetailScreen(
                viewModel = viewModel,
                characterId = characterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}