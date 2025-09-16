package com.slimdroid.movies.network.dto

import com.slimdroid.movies.database.entity.MovieEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    @SerialName("id")                       val id: Int,
    @SerialName("adult")                    val adult: Boolean,
    @SerialName("backdrop_path")            val backdropPath: String? = null,
    @SerialName("belongs_to_collection")    val belongsToCollection: BelongsToCollectionDto? = null,
    @SerialName("budget")                   val budget: Int,
    @SerialName("genres")                   val genres: List<GenreDto>,
    @SerialName("homepage")                 val homepage: String,
    @SerialName("imdb_id")                  val imdbId: String? = null,
    @SerialName("original_language")        val originalLanguage: String,
    @SerialName("original_title")           val originalTitle: String,
    @SerialName("overview")                 val overview: String,
    @SerialName("popularity")               val popularity: Float,
    @SerialName("poster_path")              val posterPath: String? = null,
    @SerialName("production_companies")     val productionCompanies: List<ProductionCompanyDto>,
    @SerialName("production_countries")     val productionCountries: List<ProductionCountryDto>,
    @SerialName("release_date")             val releaseDate: String,
    @SerialName("revenue")                  val revenue: Int,
    @SerialName("runtime")                  val runtime: Int,
    @SerialName("spoken_languages")         val spokenLanguages: List<SpokenLanguageDto>,
    @SerialName("status")                   val status: String,
    @SerialName("tagline")                  val tagline: String,
    @SerialName("title")                    val title: String,
    @SerialName("video")                    val video: Boolean,
    @SerialName("vote_average")             val voteAverage: Float,
    @SerialName("vote_count")               val voteCount: Int
)

// Strange mapper
fun MovieDetailsDto.asEntity(): MovieEntity = MovieEntity(
    id = id,
    listInfoPage = 0, // Assuming a default value for listInfoPage, adjust as necessary
    adult = adult,
    backdropPath = backdropPath,
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
    genreIds = genres.map { it.id }
)