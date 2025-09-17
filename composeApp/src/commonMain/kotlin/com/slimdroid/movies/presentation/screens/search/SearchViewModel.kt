package com.slimdroid.movies.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.cachedIn
import com.slimdroid.movies.common.Result
import com.slimdroid.movies.common.asResult
import com.slimdroid.movies.data.repository.SearchHistoryRepository
import com.slimdroid.movies.data.repository.SearchMovieRepository
import com.slimdroid.movies.dependency.Dependencies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchMovieRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SearchUiState> = queryFlow.flatMapLatest { query ->
        searchRepository.searchMovie(query)
            .cachedIn(viewModelScope)
            .asResult()
            .map {
                when (it) {
                    is Result.Loading -> SearchUiState.Loading
                    is Result.Error -> SearchUiState.ErrorGeneral(
                        it.exception.message ?: "Unknown error"
                    )

                    is Result.Success -> SearchUiState.Success(flowOf(it.data))
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5000, 1),
        initialValue = SearchUiState.Loading
    )

    val searchHistory: StateFlow<List<String>> = searchHistoryRepository.getPrompts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000, 1),
            initialValue = emptyList()
        )

    fun onQueryChanged(query: String) {
        queryFlow.value = query
    }

    fun savePrompt(prompt: String) {
        viewModelScope.launch {
            searchHistoryRepository.savePrompt(prompt)
        }
    }

    fun deletePrompt(prompt: String) {
        viewModelScope.launch {
            searchHistoryRepository.deletePrompt(prompt)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchRepository = Dependencies.searchRepository,
                    searchHistoryRepository = Dependencies.searchHistoryRepository
                )
            }
        }
    }

}