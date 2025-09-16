package com.slimdroid.movies.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("id")                   val id: Int,
    @SerialName("adult")                val adult: Boolean,
    @SerialName("backdrop_path")        val backdropPath: String? = null,
    @SerialName("genre_ids")            val genreIds: List<Int>,
    @SerialName("original_language")    val originalLanguage: String,
    @SerialName("original_title")       val originalTitle: String,
    @SerialName("overview")             val overview: String,
    @SerialName("popularity")           val popularity: Float,
    @SerialName("poster_path")          val posterPath: String? = null,
    @SerialName("release_date")         val releaseDate: String,
    @SerialName("title")                val title: String,
    @SerialName("video")                val video: Boolean,
    @SerialName("vote_average")         val voteAverage: Float,
    @SerialName("vote_count")           val voteCount: Int
)