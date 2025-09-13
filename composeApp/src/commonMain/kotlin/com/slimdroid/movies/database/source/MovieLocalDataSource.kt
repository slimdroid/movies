package com.slimdroid.movies.database.source

import com.slimdroid.movies.database.dao.MovieDao
import com.slimdroid.movies.database.entity.MovieEntity
import com.slimdroid.movies.database.entity.MovieListEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface MovieLocalDataSource {

    suspend fun insertMovieList(movieList: MovieListEntity)

    fun getAllMovieLists(): Flow<List<MovieListEntity>>

    suspend fun getMovieById(id: Int): MovieEntity?

    fun getMovieList(page: Int): Flow<MovieListEntity?>

    suspend fun deleteAll()
}

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MovieLocalDataSource {
    override suspend fun insertMovieList(movieList: MovieListEntity) = withContext(ioDispatcher) {
        movieDao.insertMovieList(movieList)
    }

    override fun getAllMovieLists(): Flow<List<MovieListEntity>> = movieDao.getAllMovieLists()
        .flowOn(ioDispatcher)

    override suspend fun getMovieById(id: Int): MovieEntity? = withContext(ioDispatcher) {
        movieDao.getMovieById(id)
    }

    override fun getMovieList(page: Int): Flow<MovieListEntity?> = movieDao.getMovieList(page)
        .flowOn(ioDispatcher)

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        movieDao.deleteAll()
    }

}