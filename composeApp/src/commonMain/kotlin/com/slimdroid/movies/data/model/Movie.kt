package com.slimdroid.movies.data.model

import com.slimdroid.movies.database.entity.MovieEntity
import com.slimdroid.movies.database.entity.MovieListEntity
import com.slimdroid.movies.database.entity.MovieListInfoEntity
import com.slimdroid.movies.network.dto.MovieDto
import com.slimdroid.movies.network.dto.MovieListDto

data class Movie(
    val id: Int,
    val adult: Boolean,
    val backdropPath: String? = null,
    val genres: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Float,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Float,
    val voteCount: Int,
    val isFavorite: Boolean = false,
)

fun MovieListDto.asEntity() = MovieListEntity(
    movieList = MovieListInfoEntity(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults
    ),
    movies = results.map { it.asEntity(page) }
)

fun MovieDto.asEntity(listInfoPage: Int) = MovieEntity(
    id = id,
    listInfoPage = listInfoPage,
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)