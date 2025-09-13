package com.slimdroid.movies.presentation.screens.movies

import androidx.compose.runtime.Immutable
import com.slimdroid.movies.data.model.Movie

@Immutable
sealed interface MoviesUiState {

    @Immutable
    data object Loading : MoviesUiState

    @Immutable
    data class Success(val movies: List<Movie>) : MoviesUiState

    @Immutable
    data object Empty : MoviesUiState

    @Immutable
    data object InternetError : MoviesUiState

    @Immutable
    data class ErrorGeneral(val error: String) : MoviesUiState
}