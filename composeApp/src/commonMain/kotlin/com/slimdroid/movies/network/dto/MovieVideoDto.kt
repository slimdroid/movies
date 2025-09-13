package com.slimdroid.movies.network.dto

import com.slimdroid.movies.data.model.YOUTUBE_BASE_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoDto(
    @SerialName("iso_639_1")        val iso6391: String,
    @SerialName("iso_3166_1")       val iso31661: String,
    @SerialName("name")             val name: String,
    @SerialName("key")              val key: String,
    @SerialName("site")             val site: String,
    @SerialName("size")             val size: Int,
    @SerialName("type")             val type: String,
    @SerialName("official")         val official: Boolean,
    @SerialName("published_at")     val publishedAt: String,
    @SerialName("id")               val id: String
)

fun MovieVideoDto.asVideoUrl(): String = YOUTUBE_BASE_URL + key