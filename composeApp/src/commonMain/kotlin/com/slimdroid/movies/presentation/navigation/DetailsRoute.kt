package com.slimdroid.movies.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.slimdroid.movies.presentation.screens.details.MovieDetailsRoute

internal fun NavGraphBuilder.detailsRoute(
    onBackPressed: () -> Unit
) {
    composable<Details> { backStackEntry ->
        val details: Details = backStackEntry.toRoute()
        MovieDetailsRoute(
            movieId = details.movieId,
            onBackPressed = onBackPressed,
        )
    }
}