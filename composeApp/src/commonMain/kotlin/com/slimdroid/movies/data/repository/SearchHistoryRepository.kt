package com.slimdroid.movies.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.slimdroid.movies.common.runCatchingCancellation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface SearchHistoryRepository {
    suspend fun savePrompt(prompt: String): Result<Unit>
    suspend fun deletePrompt(prompt: String): Result<Unit>
    fun getPrompts(): Flow<List<String>>
}

class SearchHistoryRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : SearchHistoryRepository {

    private companion object {
        val SEARCH_HISTORY_PROMPTS = stringSetPreferencesKey("search_history_prompts")
        const val SEARCH_HISTORY_LIMIT = 10
    }

    override suspend fun savePrompt(prompt: String): Result<Unit> = runCatchingCancellation {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                val currentPrompts: Set<String> = preferences[SEARCH_HISTORY_PROMPTS] ?: emptySet()
                preferences[SEARCH_HISTORY_PROMPTS] = currentPrompts
                    .minus(prompt)
                    .reversed()
                    .plus(prompt)
                    .reversed()
                    .take(SEARCH_HISTORY_LIMIT)
                    .toSet()
            }
        }
    }

    override suspend fun deletePrompt(prompt: String): Result<Unit> = runCatchingCancellation {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                val currentPrompts = preferences[SEARCH_HISTORY_PROMPTS] ?: return@edit
                preferences[SEARCH_HISTORY_PROMPTS] = currentPrompts.minus(prompt)
            }
        }
    }

    override fun getPrompts(): Flow<List<String>> = dataStore.data
        .map { preferences ->
            preferences[SEARCH_HISTORY_PROMPTS]?.toList() ?: emptyList()
        }

}
