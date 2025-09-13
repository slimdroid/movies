package com.slimdroid.movies.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slimdroid.movies.common.Result
import com.slimdroid.movies.common.asResult
import com.slimdroid.movies.data.repository.MovieRepository
import com.slimdroid.movies.dependency.Dependencies.moviesRepository
import com.slimdroid.movies.dependency.Dependencies.toggleFavoriteMovieUseCase
import com.slimdroid.movies.domain.ToggleFavoriteMovieUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val movieRepository: MovieRepository,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {

    val uiState: StateFlow<SearchUiState> = movieRepository.getMovieList()
        .asResult()
        .map {
            when (it) {
                is Result.Loading -> SearchUiState.Loading
                is Result.Error -> SearchUiState.ErrorGeneral(
                    it.exception.message ?: "Unknown error"
                )

                is Result.Success -> SearchUiState.Success(it.data.results)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000, 1),
            initialValue = SearchUiState.Loading
        )

    fun markAsFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase.invoke(movieId, isFavorite)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    movieRepository = moviesRepository,
                    toggleFavoriteMovieUseCase = toggleFavoriteMovieUseCase
                )
            }
        }
    }

}