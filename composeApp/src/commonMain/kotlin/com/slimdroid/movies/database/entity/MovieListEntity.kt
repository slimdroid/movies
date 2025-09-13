package com.slimdroid.movies.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.slimdroid.movies.data.model.MovieList

data class MovieListEntity(
    @Embedded val movieList: MovieListInfoEntity,
    @Relation(
        parentColumn = "page",
        entityColumn = "list_info_page"
    )
    val movies: List<MovieEntity>
)

fun MovieListEntity.asExternalModel() = MovieList(
    page = movieList.page,
    results = movies.map { it.asExternalModel() },
    totalPages = movieList.totalPages,
    totalResults = movieList.totalResults
)