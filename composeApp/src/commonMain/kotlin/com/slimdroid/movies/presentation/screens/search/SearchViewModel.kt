package com.slimdroid.movies.presentation.screens.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.data.repository.SearchHistoryRepository
import com.slimdroid.movies.data.repository.SearchMovieRepository
import com.slimdroid.movies.dependency.Dependencies
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchMovieRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _iuState = MutableStateFlow(SearchUiState.default())
    val iuState: StateFlow<SearchUiState> = _iuState.asStateFlow()

    init {
        savedStateHandle.get<String>(LAST_SEARCH_QUERY)?.let { query -> onQueryChanged(query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingDataFlow: Flow<PagingData<Movie>> = _iuState.flatMapLatest { state ->
        searchRepository
            .searchMovie(state.query)
            .cachedIn(viewModelScope)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000, 1),
        initialValue = PagingData.empty()
    )

    val searchHistory: StateFlow<List<String>> = searchHistoryRepository.getPrompts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000, 1),
            initialValue = emptyList()
        )

    fun onQueryChanged(query: String) {
        _iuState.update {
            it.copy(query = query)
        }
    }

    fun onClearChanged() {
        onQueryChanged("")
    }

    fun onExpandedChange(expanded: Boolean) {
        _iuState.update {
            it.copy(expanded = expanded)
        }
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

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = _iuState.value.query
        super.onCleared()
    }

    companion object {

        private const val LAST_SEARCH_QUERY: String = "last_search_query"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    searchRepository = Dependencies.searchRepository,
                    searchHistoryRepository = Dependencies.searchHistoryRepository,
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }

}