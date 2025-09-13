package com.slimdroid.movies.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.touchlab.kermit.Logger
import com.slimdroid.movies.common.Result
import com.slimdroid.movies.common.asResult
import com.slimdroid.movies.data.repository.MovieDetailsRepository
import com.slimdroid.movies.dependency.Dependencies.detailsRepository
import com.slimdroid.movies.dependency.Dependencies.toggleFavoriteMovieUseCase
import com.slimdroid.movies.domain.ToggleFavoriteMovieUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieDetailsRepository: MovieDetailsRepository,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val movieId: Int
) : ViewModel() {

    val uiState: StateFlow<MovieDetailsUiState> = movieDetailsRepository.getMovieDetails(movieId)
        .asResult()
        .map { result ->
            when (result) {
                is Result.Loading -> MovieDetailsUiState.Loading
                is Result.Success -> MovieDetailsUiState.Success(result.data)
                is Result.Error -> {
                    Logger.e { "${result.exception}" }
                    MovieDetailsUiState.Failure
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000, 1),
            initialValue = MovieDetailsUiState.Loading
        )

    fun toggleFavoriteMovie(isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movieId, isFavorite)
        }
    }

    companion object {
        val MOVIE_ID_KEY = object : CreationExtras.Key<Int> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val movieId = this[MOVIE_ID_KEY] as Int
                MovieDetailsViewModel(
                    movieDetailsRepository = detailsRepository,
                    toggleFavoriteMovieUseCase = toggleFavoriteMovieUseCase,
                    movieId = movieId
                )
            }
        }
    }

}