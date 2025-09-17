package com.slimdroid.movies.dependency

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.slimdroid.movies.cache.MovieDetailsCacheLocalDataSource
import com.slimdroid.movies.cache.MovieDetailsCacheLocalDataSourceImpl
import com.slimdroid.movies.data.repository.FavoriteMoviesRepository
import com.slimdroid.movies.data.repository.FavoriteMoviesRepositoryImpl
import com.slimdroid.movies.data.repository.MovieDetailsRepository
import com.slimdroid.movies.data.repository.MovieDetailsRepositoryImpl
import com.slimdroid.movies.data.repository.MovieRepository
import com.slimdroid.movies.data.repository.MovieRepositoryImpl
import com.slimdroid.movies.data.repository.SearchHistoryRepository
import com.slimdroid.movies.data.repository.SearchHistoryRepositoryImpl
import com.slimdroid.movies.data.repository.SearchMovieRepository
import com.slimdroid.movies.data.repository.SearchMovieRepositoryImpl
import com.slimdroid.movies.database.AppDatabase
import com.slimdroid.movies.database.getDatabaseBuilder
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSource
import com.slimdroid.movies.database.source.FavoriteMoviesLocalDataSourceImpl
import com.slimdroid.movies.database.source.MovieLocalDataSource
import com.slimdroid.movies.database.source.MovieLocalDataSourceImpl
import com.slimdroid.movies.datastore.provideDataStore
import com.slimdroid.movies.domain.ToggleFavoriteMovieUseCase
import com.slimdroid.movies.network.NetworkClient
import com.slimdroid.movies.network.source.MovieNetworkDataSource
import com.slimdroid.movies.network.source.MovieNetworkDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object Dependencies {

    val moviesRepository: MovieRepository by lazy {
        createMoviesRepository()
    }

    val favoritesRepository: FavoriteMoviesRepository by lazy {
        createFavoriteMoviesRepository()
    }

    val detailsRepository: MovieDetailsRepository by lazy {
        createMovieDetailsRepository()
    }

    val searchRepository: SearchMovieRepository by lazy {
        createSearchMovieRepository()
    }

    val searchHistoryRepository: SearchHistoryRepository by lazy {
        createSearchHistoryRepository()
    }

    val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase by lazy {
        ToggleFavoriteMovieUseCase(favoritesRepository)
    }

    val externalScope: CoroutineScope by lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    private val db: AppDatabase by lazy {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    private val dataStore: DataStore<Preferences> by lazy {
        provideDataStore()
    }

    private fun createMoviesRepository(): MovieRepository =
        MovieRepositoryImpl(
            movieLocalDataSource = createMovieLocalDataSource,
            movieRemoteDataSource = createMovieNetworkDataSource,
            favoriteMoviesLocalDataSource = createFavoriteMoviesLocalDataSource
        )

    @OptIn(ExperimentalTime::class)
    private fun createFavoriteMoviesRepository(): FavoriteMoviesRepository =
        FavoriteMoviesRepositoryImpl(
            movieLocalDataSource = createMovieLocalDataSource,
            movieDetailsCacheLocalDataSource = movieDetailsCacheLocalDataSource,
            favoriteMoviesLocalDataSource = createFavoriteMoviesLocalDataSource,
            clock = Clock.System
        )

    private fun createMovieDetailsRepository(): MovieDetailsRepository =
        MovieDetailsRepositoryImpl(
            movieLocalDataSource = createMovieLocalDataSource,
            movieRemoteDataSource = createMovieNetworkDataSource,
            movieDetailsCacheLocalDataSource = movieDetailsCacheLocalDataSource,
            favoriteMoviesLocalDataSource = createFavoriteMoviesLocalDataSource
        )

    private fun createSearchMovieRepository(): SearchMovieRepository =
        SearchMovieRepositoryImpl(
            remoteDataSource = createMovieNetworkDataSource
        )


    private fun createSearchHistoryRepository(): SearchHistoryRepository =
        SearchHistoryRepositoryImpl(
            dataStore = dataStore,
            ioDispatcher = Dispatchers.IO
        )

    private val createMovieLocalDataSource: MovieLocalDataSource by lazy {
        MovieLocalDataSourceImpl(
            movieDao = db.movieDao(),
            ioDispatcher = Dispatchers.IO
        )
    }

    private val createFavoriteMoviesLocalDataSource: FavoriteMoviesLocalDataSource by lazy {
        FavoriteMoviesLocalDataSourceImpl(
            favoriteMovieDao = db.favoriteMovieDao(),
            ioDispatcher = Dispatchers.IO
        )
    }

    private val createMovieNetworkDataSource: MovieNetworkDataSource by lazy {
        MovieNetworkDataSourceImpl(
            httpClient = NetworkClient.httpClient,
            ioDispatcher = Dispatchers.IO
        )
    }

    // Should be a singleton to avoid multiple instances
    private val movieDetailsCacheLocalDataSource: MovieDetailsCacheLocalDataSource by lazy {
        MovieDetailsCacheLocalDataSourceImpl(ioDispatcher = Dispatchers.IO)
    }

}