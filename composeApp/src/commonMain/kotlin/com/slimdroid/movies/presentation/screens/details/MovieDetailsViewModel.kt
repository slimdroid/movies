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
import com.slimdroid.movies.common.openMovieTrailer
import com.slimdroid.movies.data.repository.FavoriteMoviesRepository
import com.slimdroid.movies.data.repository.MovieDetailsRepository
import com.slimdroid.movies.dependency.Dependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val movieDetailsRepository: MovieDetailsRepository,
    private val favoritesRepository: FavoriteMoviesRepository,
    private val movieId: Int,
    private val externalScope: CoroutineScope
) : ViewModel() {

    private val _trailerErrorMessageState = MutableStateFlow(false)
    val messageState: StateFlow<Boolean> = _trailerErrorMessageState.asStateFlow()

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
            if (isFavorite) favoritesRepository.saveFavoriteMovie(movieId)
            else favoritesRepository.markAsUnfavorite(movieId)
        }
    }

    fun openMovieVideoUrl() {
        viewModelScope.launch {
            movieDetailsRepository.getMovieVideoUrl(movieId)
                .onSuccess {
                    openMovieTrailer(it)
                }.onFailure {
                    _trailerErrorMessageState.emit(true)
                }
        }
    }

    fun resetMessageState() {
        viewModelScope.launch {
            _trailerErrorMessageState.emit(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        externalScope.launch {
            favoritesRepository.removeAllUnfavorited()
        }
    }

    companion object {
        val MOVIE_ID_KEY = object : CreationExtras.Key<Int> {}

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val movieId = this[MOVIE_ID_KEY] as Int
                MovieDetailsViewModel(
                    movieDetailsRepository = Dependencies.detailsRepository,
                    favoritesRepository = Dependencies.favoritesRepository,
                    movieId = movieId,
                    externalScope = Dependencies.externalScope
                )
            }
        }
    }

}