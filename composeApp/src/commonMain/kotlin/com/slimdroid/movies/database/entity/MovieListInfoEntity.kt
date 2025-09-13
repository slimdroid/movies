package com.slimdroid.movies.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_list_info")
data class MovieListInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("page")             val page: Int,
    @ColumnInfo("total_pages")      val totalPages: Int,
    @ColumnInfo("total_results")    val totalResults: Int
)