package com.slimdroid.movies.presentation.screens.details

import com.slimdroid.movies.data.model.Movie
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

class MovieDetailsUiStateProvider : PreviewParameterProvider<MovieDetailsUiState> {
    override val values = sequenceOf(
        MovieDetailsUiState.Success(
            movie = Movie(
                id = 1,
                adult = false,
                backdropPath = "/path/to/backdrop.jpg",
                genres = listOf("Action", "Adventure"),
                originalLanguage = "en",
                originalTitle = "Sample Movie",
                overview = "This is a sample movie overview.",
                popularity = 123.45f,
                posterPath = "/path/to/poster.jpg",
                releaseDate = "2023-10-01",
                title = "Sample Movie Title",
                video = false,
                voteAverage = 8.5f,
                voteCount = 1000,
                isFavorite = false
            )
        ),
        MovieDetailsUiState.Loading,
        MovieDetailsUiState.Failure
    )
}