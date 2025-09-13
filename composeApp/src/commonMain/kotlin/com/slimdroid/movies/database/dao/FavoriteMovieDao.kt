package com.slimdroid.movies.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slimdroid.movies.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movie WHERE favorite = true ORDER BY saved_time DESC")
    fun getAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    suspend fun getById(id: Int): FavoriteMovieEntity?

    @Query("DELETE FROM favorite_movie WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM favorite_movie WHERE favorite = false")
    suspend fun clearUnfavorited()

    @Query("UPDATE favorite_movie SET favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteMoviesEntity: FavoriteMovieEntity)

    @Query("SELECT id FROM favorite_movie WHERE favorite = true")
    fun getAllIds(): Flow<List<Int>>

}