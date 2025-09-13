package com.slimdroid.movies.presentation.screens.favorites

import androidx.compose.runtime.Immutable
import com.slimdroid.movies.data.model.Movie

@Immutable
sealed interface FavoriteUiState {

    @Immutable
    data object Loading : FavoriteUiState

    @Immutable
    data class Success(val movies: List<Movie>) : FavoriteUiState

    @Immutable
    data object Empty : FavoriteUiState
}