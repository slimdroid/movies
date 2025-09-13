package com.slimdroid.movies.data.repository

import androidx.annotation.CheckResult
import com.slimdroid.movies.data.model.MovieList
import com.slimdroid.movies.data.model.asEntity
import com.slimdroid.movies.database.entity.asExternalModel
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.database.source.MovieLocalDataSource
import com.slimdroid.movies.logError
import com.slimdroid.movies.common.runCatchingCancellation
import com.slimdroid.movies.network.source.MovieNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull

interface MovieRepository {

    fun getMovieList(page: Int = 1): Flow<MovieList>

    @CheckResult
    suspend fun fetchMovieList(page: Int = 1): Result<Unit>
}

class MovieRepositoryImpl(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieNetworkDataSource,
    private val favoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource
) : MovieRepository {

    override fun getMovieList(page: Int): Flow<MovieList> = flow {
        fetchMovieList(page)
            .onFailure {
                logError("MovieRepository", "Failed to fetch movies: ${it.message}", it)
                emit(
                    MovieList(
                        page = page,
                        results = emptyList(),
                        totalPages = 0,
                        totalResults = 0
                    )
                )
            }
        movieLocalDataSource.getMovieList(page)
            .mapNotNull {
                it?.asExternalModel()
            }.combine(favoriteMoviesLocalDataSource.getAllIds()) { movieList, favoriteIds ->
                movieList.copy(
                    results = movieList.results.map { movie ->
                        movie.copy(
                            isFavorite = favoriteIds.contains(movie.id)
                        )
                    }
                )
            }.catch {

            }.collect {
                emit(it)
            }
    }

    @CheckResult
    override suspend fun fetchMovieList(page: Int) = runCatchingCancellation {
        val movies = movieRemoteDataSource.getPopularMovies(page, null)
        movieLocalDataSource.insertMovieList(movies.asEntity())
    }

}