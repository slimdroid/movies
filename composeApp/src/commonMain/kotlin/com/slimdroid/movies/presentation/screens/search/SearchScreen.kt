package com.slimdroid.movies.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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

    SearchScreen(
        paddingValues = paddingValues,
        moviesUiState = moviesState,
        onMarkAsFavorite = { movieId, isFavorite ->
            viewModel.markAsFavorite(movieId, isFavorite)
        },
        onNavigateToDetails = onNavigateToDetails
    )
}

@Composable
fun SearchScreen(
    paddingValues: PaddingValues,
    moviesUiState: SearchUiState,
    onMarkAsFavorite: (Int, Boolean) -> Unit,
    onNavigateToDetails: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (moviesUiState) {
            is SearchUiState.Loading -> LoadingScreen()

            is SearchUiState.Success -> LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
//                contentPadding = paddingValues,
//                contentPadding = PaddingValues(
//                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
//                    top = paddingValues.calculateTopPadding() + 16.dp,
//                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
//                    bottom = paddingValues.calculateBottomPadding() + 16.dp
//                ),
                contentPadding = WindowInsets.safeContent.asPaddingValues(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = moviesUiState.movies, key = { it.id }) {
                    HorizontalMovieItem(
                        title = it.title,
                        description = it.overview,
                        imageUrl = it.posterPath,
                        rating = it.voteAverage,
                        releaseDate = it.releaseDate,
                        isFavorite = it.isFavorite,
                        markAsFavorite = {
                            onMarkAsFavorite(it.id, it.isFavorite.not())
                        },
                        onClick = { onNavigateToDetails(it.id) })
                }
            }

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


@Preview
@Composable
fun MoviesScreenPreview() {

}