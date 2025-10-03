package com.slimdroid.movies.presentation.screens.movies

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MoviesScreenItem(
    title: String,
    imageUrl: String?,
    markAsFavorite: () -> Unit,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onClick
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )
    }
}

@Preview
@Composable
private fun MoviesScreenItemPreview() {
    MoviesScreenItem(
        title = "Fast & Furious X",
        imageUrl = "",
        markAsFavorite = {},
        isFavorite = false,
        onClick = {}
    )
}