package com.slimdroid.movies.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.slimdroid.movies.common.FILE_DIR
import java.io.File

actual fun createDataStore(dataStoreFileName: String): DataStore<Preferences> = createDataStore(
    producePath = {
        val file = File(System.getProperty(FILE_DIR), dataStoreFileName)
        file.absolutePath
    }
)