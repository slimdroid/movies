package com.slimdroid.movies

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.slimdroid.movies.presentation.navigation.RootNavHost
import com.slimdroid.movies.theme.AppTheme

@Composable
internal fun MovieApp() = AppTheme {
    Surface {
        RootNavHost()
    }
}