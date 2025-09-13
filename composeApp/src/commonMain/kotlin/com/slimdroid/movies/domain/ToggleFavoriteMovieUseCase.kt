package com.slimdroid.movies.domain

import androidx.annotation.CheckResult
import com.slimdroid.movies.data.repository.FavoriteMoviesRepository

class ToggleFavoriteMovieUseCase(
    private val favoriteMoviesRepository: FavoriteMoviesRepository
) {
    @CheckResult
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean): Result<Unit> =
        if (isFavorite) {
            favoriteMoviesRepository.saveFavoriteMovie(movieId)
        } else {
            favoriteMoviesRepository.removeFavoriteMovie(movieId)
        }
}