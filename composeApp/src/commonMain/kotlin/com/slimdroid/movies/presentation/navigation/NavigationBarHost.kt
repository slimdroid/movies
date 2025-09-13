package com.slimdroid.movies.presentation.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.slimdroid.movies.presentation.composables.NavigationBar
import com.slimdroid.movies.presentation.screens.favorites.FavoritesRoute
import com.slimdroid.movies.presentation.screens.movies.MoviesRoute
import com.slimdroid.movies.presentation.screens.search.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
internal fun NavGraphBuilder.navigationBarHost(
    navController: NavHostController,
    onDetailsClick: (movieID: Int) -> Unit
) {
    composable<NavGraphRoutes.NavGraph> {
        val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            bottomBar = {
                BottomAppBar(
                    scrollBehavior = scrollBehavior
                ) {
                    NavigationBar(navController)
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavGraphRoutes.Movies,
            ) {
                composable<NavGraphRoutes.Movies> {
                    MoviesRoute(
                        paddingValues = innerPadding,
                        onNavigateToDetails = { movieId ->
                            onDetailsClick(movieId)
                        }
                    )
                }
                composable<NavGraphRoutes.Search> {
                    ScreenRoute(
                        paddingValues = innerPadding,
                        onNavigateToDetails = { movieId ->
                            onDetailsClick(movieId)
                        }
                    )
                }
                composable<NavGraphRoutes.Favorites> {
                    FavoritesRoute(
                        onNavigateToDetails = { movieID ->
                            onDetailsClick(movieID)
                        }
                    )
                }
            }
        }
    }
}
