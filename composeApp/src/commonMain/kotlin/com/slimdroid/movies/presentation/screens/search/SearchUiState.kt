package com.slimdroid.movies.presentation.screens.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import com.slimdroid.movies.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Immutable
sealed interface SearchUiState {

    @Immutable
    data object Loading : SearchUiState

    @Immutable
    data class Success(val movies: Flow<PagingData<Movie>>) : SearchUiState

    @Immutable
    data object Empty : SearchUiState

    @Immutable
    data object InternetError : SearchUiState

    @Immutable
    data class ErrorGeneral(val error: String) : SearchUiState
}