package com.slimdroid.movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.slimdroid.movies.data.model.BACKDROP_BASE_URL
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.model.POSTER_BASE_URL
import com.slimdroid.movies.data.model.genresMap

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("list_info_page") val listInfoPage: Int,
    @ColumnInfo("adult") val adult: Boolean,
    @ColumnInfo("backdrop_path") val backdropPath: String? = null,
    @ColumnInfo("genre_ids") val genreIds: List<Int>,
    @ColumnInfo("original_language") val originalLanguage: String,
    @ColumnInfo("original_title") val originalTitle: String,
    @ColumnInfo("overview") val overview: String,
    @ColumnInfo("popularity") val popularity: Float,
    @ColumnInfo("poster_path") val posterPath: String? = null,
    @ColumnInfo("release_date") val releaseDate: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("video") val video: Boolean,
    @ColumnInfo("vote_average") val voteAverage: Float,
    @ColumnInfo("vote_count") val voteCount: Int
)

fun MovieEntity.asExternalModel() = Movie(
    id = id,
    adult = adult,
    backdropPath = backdropPath?.let { BACKDROP_BASE_URL + it },
    genres = genreIds.mapNotNull { genresMap[it] },
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath?.let { POSTER_BASE_URL + it },
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun MovieEntity.asFavoriteMovieEntity(isFavorite: Boolean, savedTime: Long) = FavoriteMovieEntity(
    id = id,
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
    isFavorite = isFavorite,
    savedTime = savedTime
)