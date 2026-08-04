package ru.constructor.handbook.feature.home
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val HomeRoute: String = "home"
@Composable public fun HomeScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Главная", style=MaterialTheme.typography.headlineMedium) } }
