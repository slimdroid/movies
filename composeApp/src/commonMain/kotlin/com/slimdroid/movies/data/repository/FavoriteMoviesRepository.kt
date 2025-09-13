package com.slimdroid.movies.data.repository

import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.database.entity.FavoriteMovieEntity
import com.slimdroid.movies.database.entity.asExternalModel
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.common.runCatchingCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavoriteMoviesRepository {
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun removeAllUnfavorited(): Result<Unit>
    suspend fun removeFavoriteMovie(movieId: Int): Result<Unit>

}

class FavoriteMoviesRepositoryImpl(
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource
) : FavoriteMoviesRepository {

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        favoriteMoviesLocalDataSource
            .getAll()
            .map { movies -> movies.map(FavoriteMovieEntity::asExternalModel) }

    override suspend fun removeAllUnfavorited(): Result<Unit> = runCatchingCancellation {
        favoriteMoviesLocalDataSource.clearUnfavorited()
    }

    override suspend fun removeFavoriteMovie(movieId: Int): Result<Unit> =
        runCatchingCancellation {
            favoriteMoviesLocalDataSource.removeById(movieId)
        }

}