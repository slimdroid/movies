package com.slimdroid.movies.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private const val dataStoreFileName = "search_history.preferences_pb"

fun provideDataStore() = createDataStore(dataStoreFileName)

internal expect fun createDataStore(dataStoreFileName: String): DataStore<Preferences>

internal fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
