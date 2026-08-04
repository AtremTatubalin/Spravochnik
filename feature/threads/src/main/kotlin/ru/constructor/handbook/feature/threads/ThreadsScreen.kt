package ru.constructor.handbook.feature.threads
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val ThreadsRoute: String = "threads"
@Composable public fun ThreadsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Резьбы", style=MaterialTheme.typography.headlineMedium) } }
