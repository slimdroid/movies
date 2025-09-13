package com.slimdroid.movies.database.util

import androidx.room.TypeConverter

class GenreIdsConverter {

    @TypeConverter
    fun fromList(genreIds: List<Int>): String = genreIds.joinToString(DELIMITER)

    @TypeConverter
    fun toList(genreIdsString: String): List<Int> = if (genreIdsString.isEmpty()) {
        emptyList()
    } else {
        genreIdsString.split(DELIMITER).map { it.toInt() }
    }

    private companion object {
        const val DELIMITER = ";"
    }

}