package com.slimdroid.movies.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movies.composeapp.generated.resources.Res
import movies.composeapp.generated.resources.background_box_empty_state
import movies.composeapp.generated.resources.background_empty_state
import movies.composeapp.generated.resources.background_no_internet_connection
import movies.composeapp.generated.resources.empty_screen_description_no_results
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomEmptyStateScreen(
    modifier: Modifier = Modifier,
    image: DrawableResource,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(image), contentDescription = null)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    text = title,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(400),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    text = description,
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    }
}


@Preview
@Composable
fun EmptyMoviesScreenPrev() {
    CustomEmptyStateScreen(

        image = Res.drawable.background_empty_state,
        title = "No se encontraron resultados",
        description = stringResource(
            Res.string.empty_screen_description_no_results,
            "crepusculo"
        )
    )
}

@Preview
@Composable
fun NoInternetConnectionPrev() {
    CustomEmptyStateScreen(
        image = Res.drawable.background_no_internet_connection,
        title = "Sin conexión a internet",
        description = "Revisa tu conexión a internet e intenta nuevamente"
    )
}

@Preview
@Composable
fun FavoriteEmptyPrev() {
    CustomEmptyStateScreen(
        image = Res.drawable.background_box_empty_state,
        title = "No tienes favoritos",
        description = "Agrega películas a tus favoritos para verlas aquí"
    )
}