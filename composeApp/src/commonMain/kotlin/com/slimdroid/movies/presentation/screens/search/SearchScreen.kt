package com.slimdroid.movies.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.presentation.composables.CustomEmptySearchScreen
import com.slimdroid.movies.presentation.composables.CustomErrorScreenSomethingHappens
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
    val uiState by viewModel.iuState.collectAsStateWithLifecycle()
    val pagingState: LazyPagingItems<Movie> = viewModel.pagingDataFlow.collectAsLazyPagingItems()
    val searchHistoryState = viewModel.searchHistory.collectAsStateWithLifecycle()

    // https://developer.android.com/develop/ui/compose/components/search-bar
    SearchScreen(
        paddingValues = paddingValues,
        searchHistory = searchHistoryState,
        pagingItems = pagingState,
        uiState = uiState,
        onNavigateToDetails = onNavigateToDetails,
        onNewQuery = { viewModel.onQueryChanged(it) },
        onClearQuery = { viewModel.onClearChanged() },
        onExpandedChange = { viewModel.onExpandedChange(it) },
        savePrompt = { viewModel.savePrompt(it) },
        deletePrompt = { viewModel.deletePrompt(it) }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    uiState: SearchUiState,
    searchHistory: State<List<String>>,
    pagingItems: LazyPagingItems<Movie>,
    onNavigateToDetails: (Int) -> Unit,
    onNewQuery: (String) -> Unit,
    onClearQuery: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    savePrompt: (String) -> Unit,
    deletePrompt: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val history: List<String> = searchHistory.value
        val colors1 = SearchBarDefaults.colors()

        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.query,
                    onQueryChange = {
                        onNewQuery(it)
                    },
                    onSearch = {},
                    expanded = uiState.expanded,
                    onExpandedChange = onExpandedChange,
                    leadingIcon = {
                        if (uiState.expanded) {
                            IconButton(onClick = {
                                onExpandedChange(false)
                                onClearQuery()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                            return@InputField
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    },
                    trailingIcon = {
                        if (uiState.query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
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
            expanded = uiState.expanded,
            onExpandedChange = { onExpandedChange(it) },
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
                lazyPagingItems = pagingItems,
                onNavigateToDetails = onNavigateToDetails,
                savePrompt = savePrompt
            )
        }
        if (uiState.expanded.not()) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                itemsIndexed(items = history) { index, historyItem ->
                    SearchHistoryItem(
                        itemValue = historyItem,
                        first = index == 0,
                        last = index == history.lastIndex,
                        onItemClick = { savedQuery ->
                            onExpandedChange(true)
                            onNewQuery(savedQuery)
                        },
                        onDeleteClick = {
                            deletePrompt(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultContent(
    paddingValues: PaddingValues,
    lazyPagingItems: LazyPagingItems<Movie>,
    onNavigateToDetails: (Int) -> Unit,
    savePrompt: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
//        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
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
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = "Search movie"
                            )
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(text = "Pagination Loading")

                            CircularProgressIndicator(color = Color.Black)
                        }
                    }
                }

                loadState.append is LoadState.Error -> {
                    item {
                        CustomErrorScreenSomethingHappens(
                            modifier = Modifier.padding(bottom = 180.dp)
                        )
                    }
                }

                loadState.append is LoadState.NotLoading -> {
                    if (lazyPagingItems.itemCount == 0) {
                        item {
                            CustomEmptySearchScreen(
                                Modifier.padding(bottom = 180.dp),
                                description = stringResource(
                                    Res.string.empty_screen_description_no_results,
                                    "searchQuery"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MoviesScreenPreview() {

}