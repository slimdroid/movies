package com.slimdroid.movies.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieListDto(
    @SerialName("page")             val page: Int,
    @SerialName("results")          val results: List<MovieDto>,
    @SerialName("total_pages")      val totalPages: Int,
    @SerialName("total_results")    val totalResults: Int
)