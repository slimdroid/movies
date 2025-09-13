package com.slimdroid.movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.slimdroid.movies.data.model.BACKDROP_BASE_URL
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.model.POSTER_BASE_URL
import com.slimdroid.movies.data.model.genresMap

@Entity(tableName = "favorite_movie")
data class FavoriteMovieEntity(
    @PrimaryKey                                 val id: Int,
    @ColumnInfo("adult")                val adult: Boolean,
    @ColumnInfo("backdrop_path")        val backdropPath: String? = null,
    @ColumnInfo("genre_ids")            val genreIds: List<Int>,
    @ColumnInfo("original_language")    val originalLanguage: String,
    @ColumnInfo("original_title")       val originalTitle: String,
    @ColumnInfo("overview")             val overview: String,
    @ColumnInfo("popularity")           val popularity: Float,
    @ColumnInfo("poster_path")          val posterPath: String,
    @ColumnInfo("release_date")         val releaseDate: String,
    @ColumnInfo("title")                val title: String,
    @ColumnInfo("video")                val video: Boolean,
    @ColumnInfo("vote_average")         val voteAverage: Float,
    @ColumnInfo("vote_count")           val voteCount: Int,
    @ColumnInfo("favorite")             val isFavorite: Boolean,
    @ColumnInfo("saved_time")           val savedTime: Long

)

fun FavoriteMovieEntity.asExternalModel() = Movie(
    id = id,
    adult = adult,
    backdropPath = BACKDROP_BASE_URL + backdropPath,
    genres = genreIds.mapNotNull { genresMap[it] },
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterPath = POSTER_BASE_URL + posterPath,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
    isFavorite = isFavorite
)

fun FavoriteMovieEntity.asEntity() = MovieEntity(
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
    listInfoPage = 0,
)