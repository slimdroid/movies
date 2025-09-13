package com.slimdroid.movies.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.slimdroid.movies.App

actual fun getDatabaseBuilder(dbName: String): RoomDatabase.Builder<AppDatabase> =
    Room.databaseBuilder<AppDatabase>(
        context = App.instance,
        name = dbName
    )