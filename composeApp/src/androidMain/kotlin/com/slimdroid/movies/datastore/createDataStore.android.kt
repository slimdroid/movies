package com.slimdroid.movies.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.slimdroid.movies.App

actual fun createDataStore(dataStoreFileName: String): DataStore<Preferences> = createDataStore(
    producePath = { App.instance.filesDir.resolve(dataStoreFileName).absolutePath }
)