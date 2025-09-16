package com.slimdroid.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.paging.MoviePagingSource
import com.slimdroid.movies.network.source.MovieNetworkDataSource
import kotlinx.coroutines.flow.Flow

interface SearchMovieRepository {
    fun searchMovie(movieName: String): Flow<PagingData<Movie>>
}

class SearchMovieRepositoryImpl(
    private val remoteDataSource: MovieNetworkDataSource
) : SearchMovieRepository {

    override fun searchMovie(movieName: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            MoviePagingSource(remoteDataSource, movieName)
        }
    ).flow

}