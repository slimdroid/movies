package com.slimdroid.movies.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.slimdroid.movies.database.dao.FavoriteMovieDao
import com.slimdroid.movies.database.dao.MovieDao
import com.slimdroid.movies.database.entity.FavoriteMovieEntity
import com.slimdroid.movies.database.entity.MovieEntity
import com.slimdroid.movies.database.entity.MovieListInfoEntity
import com.slimdroid.movies.database.util.GenreIdsConverter

private const val DB_NAME = "movies.db"

@Database(
    entities = [
        MovieEntity::class,
        FavoriteMovieEntity::class,
        MovieListInfoEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    GenreIdsConverter::class,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect fun getDatabaseBuilder(dbName: String = DB_NAME): RoomDatabase.Builder<AppDatabase>