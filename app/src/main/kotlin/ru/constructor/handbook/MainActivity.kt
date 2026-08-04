package ru.constructor.handbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.constructor.handbook.feature.bearings.BearingsRoute
import ru.constructor.handbook.feature.bearings.BearingsScreen
import ru.constructor.handbook.feature.calculators.CalculatorsRoute
import ru.constructor.handbook.feature.calculators.CalculatorsScreen
import ru.constructor.handbook.feature.fasteners.FastenersRoute
import ru.constructor.handbook.feature.fasteners.FastenersScreen
import ru.constructor.handbook.feature.fits.FitsRoute
import ru.constructor.handbook.feature.fits.FitsScreen
import ru.constructor.handbook.feature.home.HomeRoute
import ru.constructor.handbook.feature.home.HomeScreen
import ru.constructor.handbook.feature.materials.MaterialsRoute
import ru.constructor.handbook.feature.materials.MaterialsScreen
import ru.constructor.handbook.feature.profiles.ProfilesRoute
import ru.constructor.handbook.feature.profiles.ProfilesScreen
import ru.constructor.handbook.feature.projects.ProjectsRoute
import ru.constructor.handbook.feature.projects.ProjectsScreen
import ru.constructor.handbook.feature.reports.ReportsRoute
import ru.constructor.handbook.feature.reports.ReportsScreen
import ru.constructor.handbook.feature.reverse.ReverseRoute
import ru.constructor.handbook.feature.reverse.ReverseScreen
import ru.constructor.handbook.feature.search.SearchRoute
import ru.constructor.handbook.feature.search.SearchScreen
import ru.constructor.handbook.feature.settings.SettingsRoute
import ru.constructor.handbook.feature.settings.SettingsScreen
import ru.constructor.handbook.feature.threads.ThreadsRoute
import ru.constructor.handbook.feature.threads.ThreadsScreen
import ru.constructor.handbook.ui.theme.DarkCadTheme

public class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DarkCadTheme { AppShell() } }
    }
}

private data class TopLevelDestination(
    val route: String,
    val label: String,
    val marker: String,
)

private val topLevelDestinations = listOf(
    TopLevelDestination(HomeRoute, "Главная", "⌂"),
    TopLevelDestination(SearchRoute, "Поиск", "⌕"),
    TopLevelDestination(ProjectsRoute, "Проекты", "P"),
    TopLevelDestination(CalculatorsRoute, "Расчёты", "∑"),
    TopLevelDestination(SettingsRoute, "Ещё", "⋯"),
)

@Composable
private fun AppShell() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                topLevelDestinations.forEach { destination ->
                    NavigationBarItem(
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Text(destination.marker) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(HomeRoute) { HomeScreen() }
            composable(SearchRoute) { SearchScreen() }
            composable(ProjectsRoute) { ProjectsScreen() }
            composable(MaterialsRoute) { MaterialsScreen() }
            composable(BearingsRoute) { BearingsScreen() }
            composable(FitsRoute) { FitsScreen() }
            composable(ThreadsRoute) { ThreadsScreen() }
            composable(FastenersRoute) { FastenersScreen() }
            composable(ProfilesRoute) { ProfilesScreen() }
            composable(CalculatorsRoute) { CalculatorsScreen() }
            composable(ReverseRoute) { ReverseScreen() }
            composable(ReportsRoute) { ReportsScreen() }
            composable(SettingsRoute) { SettingsScreen() }
        }
    }
}
