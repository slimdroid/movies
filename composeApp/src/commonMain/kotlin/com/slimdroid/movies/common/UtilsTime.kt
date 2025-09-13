package com.slimdroid.movies.common

import kotlinx.datetime.LocalDate

fun convertDateToFormattedString(releaseDate: String): String {
    return try {
        val date = LocalDate.parse(releaseDate) // expects "yyyy-MM-dd"
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val formatted = "${date.day} $month ${date.year}"
        formatted
    } catch (e: Exception) {
        releaseDate
    }
}