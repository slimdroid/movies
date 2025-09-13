package com.slimdroid.movies.network.source

import com.slimdroid.movies.network.dto.MovieDetailsDto
import com.slimdroid.movies.network.dto.MovieListDto
import com.slimdroid.movies.network.dto.MovieVideoListDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieNetworkDataSourceImpl(
    private val httpClient: HttpClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : MovieNetworkDataSource {

    override suspend fun getPopularMovies(
        page: Int,
        language: String?
    ): MovieListDto = withContext(ioDispatcher) {
        httpClient.get("/3/movie/popular") {
            parameter("page", page)
            parameter("language", language)
        }.body()
    }

    override suspend fun getMovieVideos(
        movieId: Int,
        language: String?
    ): MovieVideoListDto = withContext(ioDispatcher) {
        httpClient.get("/3/movie/$movieId/videos") {
            parameter("language", language)
        }.body()
    }

    override suspend fun getMovieDetail(
        movieId: Int,
        language: String?
    ): MovieDetailsDto = withContext(ioDispatcher) {
        httpClient.get("/3/movie/$movieId") {
            parameter("language", language)
        }.body()
    }

    override suspend fun searchMovie(
        query: String,
        page: Int,
        language: String?
    ): MovieListDto = withContext(ioDispatcher) {
        httpClient.get("/3/search/movie") {
            parameter("query", query)
            parameter("include_adult", true)
            parameter("page", page)
            parameter("language", language)
        }.body()
    }

}