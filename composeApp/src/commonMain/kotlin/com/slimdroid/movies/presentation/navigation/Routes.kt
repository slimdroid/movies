package com.slimdroid.movies.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavGraphRoutes {
    @Serializable
    data object NavGraph

    @Serializable
    data object Movies

    @Serializable
    data object Search

    @Serializable
    data object Favorites
}

@Serializable
data class Details(val movieId: Int)