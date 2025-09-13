package com.slimdroid.movies.domain

import androidx.annotation.CheckResult
import com.slimdroid.movies.data.repository.FavoriteToggleMovieRepository

class ToggleFavoriteMovieUseCase(
    private val movieDetailsRepository: FavoriteToggleMovieRepository
) {
    @CheckResult
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean): Result<Unit> =
        if (isFavorite) {
            movieDetailsRepository.markAsFavorite(movieId)
        } else {
            movieDetailsRepository.markAsUnfavorite(movieId)
        }
}