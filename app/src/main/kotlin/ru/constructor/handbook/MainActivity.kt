package ru.constructor.handbook
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ru.constructor.handbook.ui.theme.DarkCadTheme
import ru.constructor.handbook.feature.home.*
import ru.constructor.handbook.feature.search.*
import ru.constructor.handbook.feature.projects.*
import ru.constructor.handbook.feature.materials.*
import ru.constructor.handbook.feature.bearings.*
import ru.constructor.handbook.feature.fits.*
import ru.constructor.handbook.feature.threads.*
import ru.constructor.handbook.feature.fasteners.*
import ru.constructor.handbook.feature.profiles.*
import ru.constructor.handbook.feature.calculators.*
import ru.constructor.handbook.feature.reverse.*
import ru.constructor.handbook.feature.reports.*
import ru.constructor.handbook.feature.settings.*
class MainActivity : ComponentActivity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { DarkCadTheme { AppShell() } } } }
private data class Destination(val route:String,val label:String)
@Composable private fun AppShell() { val controller=rememberNavController(); val destinations=listOf(Destination(HomeRoute,"Главная"),Destination(SearchRoute,"Поиск"),Destination(ProjectsRoute,"Проекты"),Destination(CalculatorsRoute,"Расчёты"),Destination(SettingsRoute,"Ещё")); val entry=controller.currentBackStackEntryAsState().value
 Scaffold(bottomBar={ NavigationBar { destinations.forEach { d -> NavigationBarItem(selected=entry?.destination?.route==d.route,onClick={controller.navigate(d.route){launchSingleTop=true;restoreState=true}},icon={},label={Text(d.label)}) } } }) { padding -> NavHost(controller,HomeRoute,modifier=androidx.compose.ui.Modifier.padding(padding)) {
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
 } } }
