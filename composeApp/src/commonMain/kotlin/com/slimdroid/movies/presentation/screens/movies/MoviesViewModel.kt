package com.slimdroid.movies.presentation.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slimdroid.movies.common.Result
import com.slimdroid.movies.common.asResult
import com.slimdroid.movies.data.repository.MovieRepository
import com.slimdroid.movies.dependency.Dependencies
import com.slimdroid.movies.domain.ToggleFavoriteMovieUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
    movieRepository: MovieRepository,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
) : ViewModel() {

    val uiState: StateFlow<MoviesUiState> = movieRepository.getMovieList()
        .asResult()
        .map {
            when (it) {
                is Result.Loading -> MoviesUiState.Loading
                is Result.Error -> MoviesUiState.ErrorGeneral(
                    it.exception.message ?: "Unknown error"
                )

                is Result.Success -> MoviesUiState.Success(it.data.results)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000, 1),
            initialValue = MoviesUiState.Loading
        )

    fun toggleFavoriteMovie(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase.invoke(movieId, isFavorite)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MoviesViewModel(
                    movieRepository = Dependencies.moviesRepository,
                    toggleFavoriteMovieUseCase = Dependencies.toggleFavoriteMovieUseCase
                )
            }
        }
    }

}