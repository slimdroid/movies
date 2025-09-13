package com.slimdroid.movies.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductionCountryDto(
    @SerialName("iso_3166_1")   val iso31661: String,
    @SerialName("name")         val name: String
)