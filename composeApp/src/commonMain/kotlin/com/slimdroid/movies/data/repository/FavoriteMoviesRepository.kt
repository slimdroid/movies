package com.slimdroid.movies.data.repository

import com.slimdroid.movies.cache.MovieDetailsCacheLocalDataSource
import com.slimdroid.movies.common.runCatchingCancellation
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.database.entity.FavoriteMovieEntity
import com.slimdroid.movies.database.entity.asEntity
import com.slimdroid.movies.database.entity.asExternalModel
import com.slimdroid.movies.database.entity.asFavoriteMovieEntity
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.database.source.MovieLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface FavoriteMoviesRepository {
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun saveFavoriteMovie(movieId: Int): Result<Unit>
    suspend fun removeFavoriteMovie(movieId: Int): Result<Unit>
    suspend fun markAsUnfavorite(movieId: Int): Result<Unit>
    suspend fun removeAllUnfavorited(): Result<Unit>
}

@OptIn(ExperimentalTime::class)
class FavoriteMoviesRepositoryImpl(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource,
    private val movieDetailsCacheLocalDataSource: MovieDetailsCacheLocalDataSource,
    private val clock: Clock
) : FavoriteMoviesRepository {

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        favoriteMoviesLocalDataSource
            .getAll()
            .map { movies -> movies.map(FavoriteMovieEntity::asExternalModel) }

    override suspend fun saveFavoriteMovie(movieId: Int): Result<Unit> = runCatchingCancellation {
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

    override suspend fun removeFavoriteMovie(movieId: Int): Result<Unit> =
        runCatchingCancellation {
            favoriteMoviesLocalDataSource.removeById(movieId)
        }

    override suspend fun markAsUnfavorite(movieId: Int): Result<Unit> = runCatchingCancellation {
        favoriteMoviesLocalDataSource.markAsUnfavorite(id = movieId)
    }

    override suspend fun removeAllUnfavorited(): Result<Unit> = runCatchingCancellation {
        favoriteMoviesLocalDataSource.clearUnfavorited()
    }

}