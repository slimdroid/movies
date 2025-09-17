package com.slimdroid.movies.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.presentation.composables.CustomEmptySearchScreen
import com.slimdroid.movies.presentation.composables.CustomErrorScreenSomethingHappens
import com.slimdroid.movies.presentation.composables.CustomNoInternetConnectionScreen
import com.slimdroid.movies.presentation.composables.LoadingScreen
import movies.composeapp.generated.resources.Res
import movies.composeapp.generated.resources.empty_screen_description_no_results
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ScreenRoute(
    paddingValues: PaddingValues,
    onNavigateToDetails: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
) {
    val moviesState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchHistoryState = viewModel.searchHistory.collectAsStateWithLifecycle()

    // https://developer.android.com/develop/ui/compose/components/search-bar
    SearchScreen(
        paddingValues = paddingValues,
        moviesUiState = moviesState,
        searchHistory = searchHistoryState,
        onNavigateToDetails = onNavigateToDetails,
        onNewQuery = { viewModel.onQueryChanged(it) },
        savePrompt = { viewModel.savePrompt(it) },
        deletePrompt = { viewModel.deletePrompt(it) }
    )
}

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    moviesUiState: SearchUiState,
    searchHistory: State<List<String>>,
    onNavigateToDetails: (Int) -> Unit,
    onNewQuery: (String) -> Unit,
    savePrompt: (String) -> Unit,
    deletePrompt: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (moviesUiState) {
            is SearchUiState.Loading -> LoadingScreen()

            is SearchUiState.Success -> SearchScreenContent(
                paddingValues = paddingValues,
                moviesUiState = moviesUiState,
                onNavigateToDetails = onNavigateToDetails,
                searchHistory = searchHistory,
                onNewQuery = onNewQuery,
                savePrompt = savePrompt,
                deletePrompt = deletePrompt
            )

            is SearchUiState.Empty -> CustomEmptySearchScreen(
                Modifier.padding(bottom = 180.dp),
                description = stringResource(
                    Res.string.empty_screen_description_no_results,
                    "searchQuery"
                )
            )

            is SearchUiState.InternetError -> CustomNoInternetConnectionScreen(
                modifier = Modifier.padding(bottom = 180.dp)
            )

            is SearchUiState.ErrorGeneral -> CustomErrorScreenSomethingHappens(
                modifier = Modifier.padding(bottom = 180.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    paddingValues: PaddingValues,
    moviesUiState: SearchUiState.Success,
    onNavigateToDetails: (Int) -> Unit,
    searchHistory: State<List<String>>,
    onNewQuery: (String) -> Unit,
    savePrompt: (String) -> Unit,
    deletePrompt: (String) -> Unit
) {
    val history: List<String> = searchHistory.value
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(paddingValues),
    ) {
        // FIXME move "searchQuery" to viewmodel
        var searchQuery by remember { mutableStateOf("") }
        var expanded by rememberSaveable { mutableStateOf(false) }

        val colors1 = SearchBarDefaults.colors()
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        onNewQuery(it)
                    },
                    onSearch = {},
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(onClick = {
                                expanded = false
                                searchQuery = ""
                                onNewQuery(searchQuery)
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                            return@InputField
                        } else {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                onNewQuery(searchQuery)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    colors = colors1.inputFieldColors,
                )
                //text showed on SearchBar
                //update the value of searchText
                //the callback to be invoked when the input service triggers the ImeAction.Search action
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = SearchBarDefaults.inputFieldShape,
            colors = colors1,
            tonalElevation = SearchBarDefaults.TonalElevation,
            shadowElevation = SearchBarDefaults.ShadowElevation,
            windowInsets = SearchBarDefaults.windowInsets
        ) {
            SearchResultContent(
                paddingValues = paddingValues,
                moviesUiState = moviesUiState,
                onNavigateToDetails = onNavigateToDetails,
                savePrompt = savePrompt
            )
        }
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            itemsIndexed(items = history) { index, historyItem ->
                SearchHistoryItem(
                    itemValue = historyItem,
                    first = index == 0,
                    last = index == history.lastIndex,
                    onItemClick = {
                        expanded = true
                        searchQuery = it
                        onNewQuery(it)
                    },
                    onDeleteClick = {
                        deletePrompt(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchResultContent(
    paddingValues: PaddingValues,
    moviesUiState: SearchUiState.Success,
    onNavigateToDetails: (Int) -> Unit,
    savePrompt: (String) -> Unit,
) {
    val lazyPagingItems: LazyPagingItems<Movie> = moviesUiState.movies.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
//        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let {
                SearchMovieItem(
                    title = it.title,
                    description = it.overview,
                    imageUrl = it.posterPath,
                    rating = it.voteAverage,
                    releaseDate = it.releaseDate,
                    onClick = {
                        savePrompt(it.title)
                        onNavigateToDetails(it.id)
                    }
                )
            }
        }
        // https://proandroiddev.com/pagination-in-jetpack-compose-with-and-without-paging-3-e45473a352f4
        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    // You can add shimmer here
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    // Handle error
                }
            }
        }
    }
}

@Preview
@Composable
fun MoviesScreenPreview() {

}