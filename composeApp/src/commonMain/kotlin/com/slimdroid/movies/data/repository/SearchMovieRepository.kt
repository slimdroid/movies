package com.slimdroid.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.paging.MoviePagingSource
import com.slimdroid.movies.network.source.MovieNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface SearchMovieRepository {
    fun searchMovie(movieName: String): Flow<PagingData<Movie>>
}

class SearchMovieRepositoryImpl(
    private val remoteDataSource: MovieNetworkDataSource
) : SearchMovieRepository {

    companion object {
        private const val PAGE_SIZE = 20
    }

    override fun searchMovie(movieName: String): Flow<PagingData<Movie>> =
        if (movieName.isBlank()) flowOf(PagingData.empty())
        else Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {
                MoviePagingSource(remoteDataSource, movieName)
            }
        ).flow

}