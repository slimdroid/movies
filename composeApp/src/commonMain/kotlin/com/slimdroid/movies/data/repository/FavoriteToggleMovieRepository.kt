package com.slimdroid.movies.data.repository

import com.slimdroid.movies.cache.MovieDetailsCacheLocalDataSource
import com.slimdroid.movies.database.entity.asEntity
import com.slimdroid.movies.database.entity.asFavoriteMovieEntity
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.database.source.MovieLocalDataSource
import com.slimdroid.movies.common.runCatchingCancellation
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface FavoriteToggleMovieRepository {
    suspend fun markAsFavorite(movieId: Int): Result<Unit>
    suspend fun markAsUnfavorite(movieId: Int): Result<Unit>
}

@OptIn(ExperimentalTime::class)
class FavoriteToggleMovieRepositoryImpl(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieDetailsCacheLocalDataSource: MovieDetailsCacheLocalDataSource,
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource,
    private val clock: Clock
) : FavoriteToggleMovieRepository {

    @OptIn(ExperimentalTime::class)
    override suspend fun markAsFavorite(movieId: Int): Result<Unit> = runCatchingCancellation {
        val movie = movieLocalDataSource.getMovieById(movieId)
            ?: movieDetailsCacheLocalDataSource.getCachedMovieDetails(movieId)
            ?: favoriteMoviesLocalDataSource.getById(movieId)?.asEntity()
            ?: throw NoSuchElementException("Movie not found")

        favoriteMoviesLocalDataSource.insert(
            movie.asFavoriteMovieEntity(
                isFavorite = true,
                savedTime = clock.now().toEpochMilliseconds()
            )
        )
    }

    override suspend fun markAsUnfavorite(movieId: Int): Result<Unit> = runCatchingCancellation {
        if (isFavorite(movieId)) {
            favoriteMoviesLocalDataSource.updateFavoriteStatus(movieId, false)
        }
    }

    private suspend fun isFavorite(id: Int): Boolean = favoriteMoviesLocalDataSource.isFavorite(id)

}