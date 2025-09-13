package com.slimdroid.movies.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BelongsToCollectionDto(
    @SerialName("id")               val id: Int,
    @SerialName("backdrop_path")    val backdropPath: String,
    @SerialName("name")             val name: String,
    @SerialName("poster_path")      val posterPath: String
)