package com.slimdroid.movies.presentation.screens.search

import androidx.compose.runtime.Immutable

@Immutable
data class SearchUiState(
    val query: String,
    val expanded: Boolean
) {
    companion object {
        fun default() = SearchUiState(
            query = "",
            expanded = false
        )
    }
}