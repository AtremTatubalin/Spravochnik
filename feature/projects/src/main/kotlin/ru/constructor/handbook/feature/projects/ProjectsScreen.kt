package ru.constructor.handbook.feature.projects
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val ProjectsRoute: String = "projects"
@Composable public fun ProjectsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Проекты", style=MaterialTheme.typography.headlineMedium) } }
