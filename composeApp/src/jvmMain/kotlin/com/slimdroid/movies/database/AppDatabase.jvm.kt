package com.slimdroid.movies.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), dbName)
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath,
    )
}