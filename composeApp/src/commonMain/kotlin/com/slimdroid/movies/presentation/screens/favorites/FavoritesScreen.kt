package com.slimdroid.movies.presentation.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.slimdroid.movies.presentation.composables.CustomEmptyStateScreen
import com.slimdroid.movies.presentation.composables.LoadingScreen
import movies.composeapp.generated.resources.Res
import movies.composeapp.generated.resources.background_box_empty_state
import movies.composeapp.generated.resources.screen_empty_description_favorites
import movies.composeapp.generated.resources.screen_empty_title_favorites
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FavoritesRoute(
    onNavigateToDetails: (Int) -> Unit,
    viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        favoriteUiState = uiState,
        onNavigateToDetails = onNavigateToDetails,
        removeFromFavorites = { movieId ->
            viewModel.removeFavoriteMovie(movieId)
        }
    )
}

@Composable
fun FavoritesScreen(
    favoriteUiState: FavoriteUiState,
    onNavigateToDetails: (Int) -> Unit,
    removeFromFavorites: (Int) -> Unit,
) {
    when (favoriteUiState) {
        is FavoriteUiState.Loading -> LoadingScreen()

        is FavoriteUiState.Success -> {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 0.dp,
                contentPadding = WindowInsets.safeContent.asPaddingValues(),
                horizontalArrangement = Arrangement.Center,
                content = {
                    items(favoriteUiState.movies) {
                        VerticalMovieItem(
                            title = it.title,
                            release = it.overview,
                            imageUrl = it.posterPath,
                            removeFromFavorite = { removeFromFavorites(it.id) },
                            onClick = { onNavigateToDetails(it.id) }
                        )

                        if (it == favoriteUiState.movies.last()) {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            )
        }

        is FavoriteUiState.Empty -> {
            CustomEmptyStateScreen(
                modifier = Modifier.padding(bottom = 180.dp),
                image = Res.drawable.background_box_empty_state,
                title = stringResource(Res.string.screen_empty_title_favorites),
                description = stringResource(Res.string.screen_empty_description_favorites)
            )
        }
    }

}

@Preview
@Composable
fun PreviewFavoritesScreen() {
//    FavoritesScreen(
//        onClickNavigateToDetails = {},
//        favoriteMovies = listOf(
//            FavoriteMoviesEntity(
//                id = 1,
//                title = "Title",
//                overview = "Release",
//                poster_path = "https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
//                vote_average = 7.5f,
//                release_date = "2021-08-11",
//            ),
//            FavoriteMoviesEntity(
//                id = 2,
//                title = "Title",
//                overview = "Release",
//                poster_path = "https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
//                vote_average = 7.5f,
//                release_date = "2021-08-11",
//            ),
//        )
//    )
}