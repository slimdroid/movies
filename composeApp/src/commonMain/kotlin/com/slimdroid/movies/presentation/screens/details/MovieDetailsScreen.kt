package com.slimdroid.movies.presentation.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.slimdroid.movies.data.model.Movie
import com.slimdroid.movies.presentation.composables.CustomErrorScreenSomethingHappens
import com.slimdroid.movies.presentation.composables.LoadingScreen
import com.slimdroid.movies.theme.AppTheme
import com.slimdroid.movies.theme.ScrimDark
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter

@Composable
fun MovieDetailsRoute(
    movieId: Int,
    onBackPressed: () -> Unit,
    viewModel: MovieDetailsViewModel = viewModel(
        factory = MovieDetailsViewModel.Factory,
        extras = MutableCreationExtras().apply {
            set(MovieDetailsViewModel.MOVIE_ID_KEY, movieId)
        }
    )
) {
    val uiState: MovieDetailsUiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailsScreen(
        onClickBack = onBackPressed,
        onClickFavorite = {
            viewModel.toggleFavoriteMovie(isFavorite = it)
        },
        uiState = uiState
    )
}

@Composable
fun MovieDetailsScreen(
    onClickBack: () -> Unit,
    onClickFavorite: (Boolean) -> Unit,
    uiState: MovieDetailsUiState
) {
    when (uiState) {
        is MovieDetailsUiState.Loading -> LoadingScreen()

        is MovieDetailsUiState.Success -> {
            MovieDetailsContent(
                onClickBack = onClickBack,
                onClickFavorite = onClickFavorite,
                movie = uiState.movie
            )
        }

        is MovieDetailsUiState.Failure -> CustomErrorScreenSomethingHappens(
            modifier = Modifier.padding(bottom = 180.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsContent(
    onClickBack: () -> Unit,
    onClickFavorite: (Boolean) -> Unit,
    movie: Movie
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = { onClickBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onClickFavorite(movie.isFavorite.not()) }) {
                        Icon(
                            imageVector = if (movie.isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
//                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 20.dp
                    ),
                ) {
                    Box() {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp),
                            model = movie.backdropPath,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                        )

                        Card(
                            modifier = Modifier
                                .offset(x = 310.dp, y = 178.dp)
                                .background(
                                    color = Color(0x52252836),
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .padding(vertical = 4.dp)
                                .padding(horizontal = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                        ) {
                            Row(
                                modifier = Modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    imageVector = Icons.Outlined.StarBorder,
                                    contentDescription = null,
                                    tint = ScrimDark,
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = movie.voteAverage.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(600),
                                    color = ScrimDark
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .offset(x = 29.dp, y = 150.dp)
                        .width(95.dp)
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        model = movie.posterPath,
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                    )
                }

                //Title
                Text(
                    modifier = Modifier
                        .width(210.dp)
                        .height(48.dp)
                        .offset(x = 140.dp, y = 220.dp),
                    text = movie.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                )
            }

            Spacer(modifier = Modifier.height(75.dp))

            HorizontalThreeOptions(
                yearRelease = movie.releaseDate,
                duration = 0,
                genre = movie.genres.joinToString(separator = " * ")
            )

            Spacer(modifier = Modifier.height(24.dp))

            //Description Title
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = "Description",
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
            )

            Spacer(modifier = Modifier.height(12.dp))

            //Description
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = movie.overview,
                textAlign = TextAlign.Justify,
                fontSize = 14.sp,
                fontWeight = FontWeight(400),
            )

            Spacer(modifier = Modifier.height(24.dp))

            val listGenres = movie.genres.joinToString(separator = " * ")

            //Genres Action * Horror * Comedy
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = listGenres,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight(600),
            )
        }
    }
}

@Composable
fun HorizontalThreeOptions(
    yearRelease: String,
    duration: Int,
    genre: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .padding(horizontal = 24.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = yearRelease,
            style = MaterialTheme.typography.labelMedium
        )

        VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp))

        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = duration.toTime(),
            style = MaterialTheme.typography.labelMedium
        )

        VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp))

        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Outlined.Movie,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = genre,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun Int.toTime(): String {
    val hours = this / 60
    val minutes = this % 60
    return buildString {
        if (hours > 0) append("$hours hours ")
        if (minutes > 0) append("$minutes minutes")
    }.trim()
}

@Preview
@Composable
fun DetailsMovieContentPreview(
    @PreviewParameter(
        provider = MovieDetailsUiStateProvider::class,
        limit = 1
    ) mockedUiState: MovieDetailsUiState
) {
    AppTheme {
        Surface {
            MovieDetailsScreen(
                onClickBack = {},
                onClickFavorite = {},
                uiState = mockedUiState
            )
        }
    }
}

@Preview
@Composable
fun HorizontalThreeOptionsPreview() {
    AppTheme {
        Surface {
            HorizontalThreeOptions(
                yearRelease = "2021-12-15",
                duration = 118,
                genre = "Action",
            )
        }
    }
}
