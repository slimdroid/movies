package com.slimdroid.movies.presentation.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.slimdroid.movies.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    totalStars: Int = 10,
) {
    Row(
        modifier = modifier
    ) {
        for (i in 1..totalStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = Color.Yellow

            )
        }
    }
}

@Preview
@Composable
fun PreviewRatingBar() {
    AppTheme {
        Surface {
            RatingBar(rating = 6.5f)
        }
    }
}
