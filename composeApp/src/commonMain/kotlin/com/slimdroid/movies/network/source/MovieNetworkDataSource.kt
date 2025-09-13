package com.slimdroid.movies.network.source

import com.slimdroid.movies.network.dto.MovieDetailsDto
import com.slimdroid.movies.network.dto.MovieListDto
import com.slimdroid.movies.network.dto.MovieVideoListDto

interface MovieNetworkDataSource {

    suspend fun getPopularMovies(page: Int = 1, language: String? = null): MovieListDto

    suspend fun getMovieVideos(movieId: Int, language: String? = null): MovieVideoListDto

    suspend fun getMovieDetail(movieId: Int, language: String? = null): MovieDetailsDto

    suspend fun searchMovie(query: String, page: Int = 1, language: String? = null): MovieListDto
}