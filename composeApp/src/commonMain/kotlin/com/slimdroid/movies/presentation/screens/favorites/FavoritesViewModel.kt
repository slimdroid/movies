package com.slimdroid.movies.presentation.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.slimdroid.movies.data.repository.FavoriteMoviesRepository
import com.slimdroid.movies.dependency.Dependencies
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoriteMoviesRepository
) : ViewModel() {

    val uiState: StateFlow<FavoriteUiState> = favoritesRepository
        .getFavoriteMovies()
        .map {
            if (it.isNotEmpty()) FavoriteUiState.Success(it)
            else FavoriteUiState.Empty
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5_000),
            initialValue = FavoriteUiState.Loading
        )

    fun removeFavoriteMovie(movieId: Int) {
        viewModelScope.launch {
            favoritesRepository.removeFavoriteMovie(movieId)
        }
    }

    // TODO придумать куда запихнуть этот метод
    override fun onCleared() {
        super.onCleared()
        // TODO implement external scope for this
        viewModelScope.launch {
            favoritesRepository.removeAllUnfavorited()
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                FavoritesViewModel(
                    favoritesRepository = Dependencies.favoritesRepository
                )
            }
        }
    }

}