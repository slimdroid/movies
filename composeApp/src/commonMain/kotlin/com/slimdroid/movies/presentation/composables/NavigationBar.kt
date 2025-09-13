package com.slimdroid.movies.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.slimdroid.movies.presentation.navigation.NavGraphRoutes
import com.slimdroid.movies.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        containerColor = NavigationBarDefaults.containerColor.copy(
            alpha = 0.9f,
        )
    ) {
        topLevelRoutes.forEach { topLevelRoute ->

            val isSelected = currentDestination
                ?.hierarchy
                ?.any { it.hasRoute(topLevelRoute.route::class) } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) topLevelRoute.selectedIcon else topLevelRoute.icon,
                        contentDescription = topLevelRoute.label
                    )
                },
                selected = isSelected,
                label = { Text(text = topLevelRoute.label) },
                onClick = {
                    navController.navigate(topLevelRoute.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
@Preview
fun NavigationBarPreview() {
    AppTheme {
        Surface {
            NavigationBar(
                navController = rememberNavController()
            )
        }
    }
}

private data class TopLevelRoute<T : Any>(
    val label: String,
    val route: T,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

private val topLevelRoutes = listOf(
    TopLevelRoute(
        label = "Movies",
        route = NavGraphRoutes.Movies,
        icon = Icons.Outlined.Movie,
        selectedIcon = Icons.Default.Movie
    ),
    TopLevelRoute(
        label = "Search",
        route = NavGraphRoutes.Search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Default.Search
    ),
    TopLevelRoute(
        label = "Favorite",
        route = NavGraphRoutes.Favorites,
        icon = Icons.Outlined.FavoriteBorder,
        selectedIcon = Icons.Default.Favorite
    )
)