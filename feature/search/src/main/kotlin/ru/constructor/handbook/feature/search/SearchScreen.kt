package ru.constructor.handbook.feature.search
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val SearchRoute: String = "search"
@Composable public fun SearchScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Поиск", style=MaterialTheme.typography.headlineMedium) } }
