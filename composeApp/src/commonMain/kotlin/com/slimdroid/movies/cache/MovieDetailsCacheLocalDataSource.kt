package com.slimdroid.movies.cache

import com.slimdroid.movies.database.entity.MovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface MovieDetailsCacheLocalDataSource {
    suspend fun getCachedMovieDetails(movieId: Int): MovieEntity?

    suspend fun putCachedMovieDetails(movieId: Int, movieEntity: MovieEntity)
}

class MovieDetailsCacheLocalDataSourceImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MovieDetailsCacheLocalDataSource {

    private val mutex = Mutex()
    private val cache = mutableMapOf<Int, MovieEntity>()

    override suspend fun getCachedMovieDetails(movieId: Int): MovieEntity? =
        withContext(ioDispatcher) {
            mutex.withLock { cache[movieId] }
        }

    override suspend fun putCachedMovieDetails(movieId: Int, movieEntity: MovieEntity) =
        withContext(ioDispatcher) {
            mutex.withLock { cache[movieId] = movieEntity }
        }

}