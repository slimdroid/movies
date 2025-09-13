package com.slimdroid.movies.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.slimdroid.movies.database.entity.MovieEntity
import com.slimdroid.movies.database.entity.MovieListEntity
import com.slimdroid.movies.database.entity.MovieListInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Transaction
    suspend fun insertMovieList(movieList: MovieListEntity) {
        deleteMoviesByPage(movieList.movieList.page)
        // Assuming you have an insert method for MovieListInfoEntity and MovieEntity
        insertMovieListInfo(movieList.movieList)
        insertMovies(movieList.movies)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieListInfo(movieListInfo: MovieListInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Transaction
    @Query("DELETE FROM movie WHERE list_info_page = :page")
    suspend fun deleteMoviesByPage(page: Int)

    @Transaction
    @Query("SELECT * FROM movie_list_info ORDER BY page")
    fun getAllMovieLists(): Flow<List<MovieListEntity>>

    @Query("SELECT * FROM movie WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?

    @Transaction
    @Query("SELECT * FROM movie_list_info WHERE page = :page")
    fun getMovieList(page: Int): Flow<MovieListEntity?>

    @Transaction
    @Query("DELETE FROM movie_list_info")
    suspend fun deleteAll()
}