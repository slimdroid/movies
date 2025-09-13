package com.slimdroid.movies.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoListDto(
    @SerialName("id")       val id: Int,
    @SerialName("results")  val results: List<MovieVideoDto>
)