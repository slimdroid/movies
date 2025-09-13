package com.slimdroid.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNavHost() {
    val navController: NavHostController = rememberNavController()
    val navigationBarController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavGraphRoutes.NavGraph
    ) {
        navigationBarHost(
            navController = navigationBarController,
            onDetailsClick = {
                navController.navigate(route = Details(it))
            }
        )
        detailsRoute(
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}
