package com.slimdroid.movies.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import movies.composeapp.generated.resources.Res
import movies.composeapp.generated.resources.background_empty_state
import movies.composeapp.generated.resources.background_no_internet_connection
import movies.composeapp.generated.resources.background_something_wrong
import movies.composeapp.generated.resources.empty_screen_description_error_something_went_wrong
import movies.composeapp.generated.resources.empty_screen_description_no_internet
import movies.composeapp.generated.resources.empty_screen_description_no_results
import movies.composeapp.generated.resources.empty_screen_title_error_something_went_wrong
import movies.composeapp.generated.resources.empty_screen_title_no_internet
import movies.composeapp.generated.resources.empty_screen_title_not_found_results
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}

@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen()
}

@Composable
fun CustomErrorScreenSomethingHappens(
    modifier: Modifier = Modifier,
) {
    CustomEmptyStateScreen(
        modifier = modifier,
        title = stringResource(Res.string.empty_screen_title_error_something_went_wrong),
        //Algo pasó, por favor intenta de nuevo
        description = stringResource(Res.string.empty_screen_description_error_something_went_wrong),
        image = Res.drawable.background_something_wrong
    )
}

@Preview
@Composable
fun CustomErrorScreenSomethingHappensPreview() {
    CustomErrorScreenSomethingHappens()
}

//no internet
@Composable
fun CustomNoInternetConnectionScreen(
    modifier: Modifier = Modifier,
) {
    CustomEmptyStateScreen(
        modifier = modifier,
        title = stringResource(Res.string.empty_screen_title_no_internet),
        //Algo pasó, por favor intenta de nuevo
        description = stringResource(Res.string.empty_screen_description_no_internet),
        image = Res.drawable.background_no_internet_connection
    )
}

@Preview
@Composable
fun CustomNoInternetConnectionScreenPreview() {
    CustomNoInternetConnectionScreen()
}

@Composable
fun CustomEmptySearchScreen(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.empty_screen_title_not_found_results),
    description: String = stringResource(
        Res.string.empty_screen_description_no_results,
        "busqueda"
    )
) {
    CustomEmptyStateScreen(
        modifier = modifier,
        title = title,
        description = description,
        image = Res.drawable.background_empty_state
    )
}

@Preview
@Composable
fun ErrorScreen2Prev() {
    CustomErrorScreenSomethingHappens()
}


