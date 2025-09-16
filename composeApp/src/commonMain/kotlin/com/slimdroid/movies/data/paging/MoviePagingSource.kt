package com.slimdroid.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.touchlab.kermit.Logger
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.model.asExternalModel
import com.slimdroid.movies.network.source.MovieNetworkDataSource

private const val STARTING_KEY = 1

// https://developer.android.com/topic/libraries/architecture/paging/v3-overview
class MoviePagingSource(
    private val movieRemoteDataSource: MovieNetworkDataSource,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val nextPageNumber = params.key ?: STARTING_KEY
        return runCatching {
            movieRemoteDataSource.searchMovie(query, page = nextPageNumber)
        }.fold(
            onSuccess = { response ->
                Logger.i { "response: page:${response.page}, totalPages:${response.totalPages}" }
                val data = response.results
                    .map { it.asExternalModel() }

                LoadResult.Page(
                    data = data,
                    prevKey = if (nextPageNumber == STARTING_KEY) null else nextPageNumber.minus(1),
                    nextKey = if (response.page < response.totalPages) response.page.plus(1) else null
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

}