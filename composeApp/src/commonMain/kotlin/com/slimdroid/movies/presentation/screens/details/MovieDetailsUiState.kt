package com.slimdroid.movies.presentation.screens.details

import androidx.compose.runtime.Immutable
import com.slimdroid.movies.data.model.Movie

@Immutable
sealed interface MovieDetailsUiState {

    @Immutable
    data object Loading : MovieDetailsUiState

    @Immutable
    data class Success(val movie: Movie) : MovieDetailsUiState

    @Immutable
    data object Failure : MovieDetailsUiState
}