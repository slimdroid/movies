package com.slimdroid.movies.data.repository

import com.slimdroid.movies.cache.MovieDetailsCacheLocalDataSource
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.database.entity.asExternalModel
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.database.source.MovieLocalDataSource
import com.slimdroid.movies.network.dto.asEntity
import com.slimdroid.movies.network.dto.asVideoUrl
import com.slimdroid.movies.common.runCatchingCancellation
import com.slimdroid.movies.network.source.MovieNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

interface MovieDetailsRepository {
    fun getMovieDetails(movieId: Int): Flow<Movie>
    suspend fun getMovieVideoUrl(movieId: Int): Result<String>
}

class MovieDetailsRepositoryImpl(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieNetworkDataSource,
    private val movieDetailsCacheLocalDataSource: MovieDetailsCacheLocalDataSource,
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource
) : MovieDetailsRepository {

    override fun getMovieDetails(movieId: Int): Flow<Movie> = flow {
        combine(
            flow = favoriteMoviesLocalDataSource.getAllIds(),
            flow2 = flowOf(checkLocalDataSource(movieId) ?: checkRemoteDataSource(movieId))
        ) { favoriteIds, movie ->
            movie.copy(isFavorite = favoriteIds.contains(movie.id))
        }.collect {
            emit(it)
        }
    }

    override suspend fun getMovieVideoUrl(movieId: Int): Result<String> = runCatchingCancellation {
        movieRemoteDataSource.getMovieVideos(movieId)
            .results
            .first()
            .asVideoUrl()
    }

    private suspend fun checkLocalDataSource(movieId: Int): Movie? =
        favoriteMoviesLocalDataSource.getById(movieId)?.asExternalModel()
            ?: movieLocalDataSource.getMovieById(movieId)?.asExternalModel()
            ?: movieDetailsCacheLocalDataSource.getCachedMovieDetails(movieId)?.asExternalModel()

    private suspend fun checkRemoteDataSource(movieId: Int): Movie =
        movieRemoteDataSource.getMovieDetail(movieId)
            .asEntity()
            .also { movieEntity ->
                movieDetailsCacheLocalDataSource.putCachedMovieDetails(movieId, movieEntity)
            }.asExternalModel()

}