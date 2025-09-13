package com.slimdroid.movies.database.source

import com.slimdroid.movies.database.dao.FavoriteMovieDao
import com.slimdroid.movies.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface FavoriteMoviesLocalDataSource {
    fun getAll(): Flow<List<FavoriteMovieEntity>>
    suspend fun getById(id: Int): FavoriteMovieEntity?
    suspend fun clearUnfavorited()
    suspend fun removeById(id: Int)
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
    suspend fun insert(favoriteMoviesEntity: FavoriteMovieEntity)
    fun getAllIds(): Flow<List<Int>>
    suspend fun isFavorite(id: Int): Boolean
}

class FavoriteMoviesLocalDataSourceImpl(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : FavoriteMoviesLocalDataSource {

    override fun getAll(): Flow<List<FavoriteMovieEntity>> = favoriteMovieDao.getAll()
        .flowOn(ioDispatcher)

    override suspend fun getById(id: Int): FavoriteMovieEntity? = withContext(ioDispatcher) {
        favoriteMovieDao.getById(id)
    }

    override suspend fun clearUnfavorited() = withContext(ioDispatcher) {
        favoriteMovieDao.clearUnfavorited()
    }

    override suspend fun removeById(id: Int) = withContext(ioDispatcher) {
        favoriteMovieDao.deleteById(id)
    }

    override suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) =
        withContext(ioDispatcher) {
            favoriteMovieDao.updateFavoriteStatus(id, isFavorite)
        }

    override suspend fun insert(favoriteMoviesEntity: FavoriteMovieEntity) =
        withContext(ioDispatcher) {
            favoriteMovieDao.insert(favoriteMoviesEntity)
        }

    override fun getAllIds(): Flow<List<Int>> = favoriteMovieDao.getAllIds()
        .flowOn(ioDispatcher)

    override suspend fun isFavorite(id: Int): Boolean = withContext(ioDispatcher) {
        favoriteMovieDao.isFavorite(id)
    }

}