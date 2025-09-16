package com.slimdroid.movies.presentation.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.slimdroid.movies.common.convertDateToFormattedString
import com.slimdroid.movies.presentation.composables.RatingBar
import com.slimdroid.movies.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HorizontalMovieItem(
    title: String,
    description: String,
    imageUrl: String?,
    rating: Float,
    releaseDate: String,
    markAsFavorite: () -> Unit,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.3f),
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = markAsFavorite
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = description,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = convertDateToFormattedString(releaseDate),
                    style = MaterialTheme.typography.bodySmall,
                )

                RatingBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(12.dp),
                    rating = rating
                )
            }
        }
    }
}

@Preview
@Composable
fun HorizontalMovieItemPrev() {
    AppTheme {
        Surface {
            HorizontalMovieItem(
                title = "Fast & Furious X",
                description = "Durante numerosas misiones más que imposibles, Dom Toretto y su familia han sido capaces de ser más listos, de tener más valor y de ir más rápido que cualquier enemigo que se cruzara con ellos. Pero ahora tendrán que enfrentarse al oponente más letal que jamás hayan conocido: un terrible peligro que resurge del pasado, que se mueve por una sangrienta sed de venganza y que está dispuesto a destrozar a la familia y destruir para siempre todo lo que a Dom le importa.",
                imageUrl = "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
                rating = 3.5f,
                isFavorite = false,
                onClick = {},
                markAsFavorite = {},
                releaseDate = "2021-05-19"
            )
        }
    }
}