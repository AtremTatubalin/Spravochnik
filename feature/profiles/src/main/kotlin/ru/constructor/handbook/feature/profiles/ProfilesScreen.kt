package ru.constructor.handbook.feature.profiles
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val ProfilesRoute: String = "profiles"
@Composable public fun ProfilesScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Сортамент", style=MaterialTheme.typography.headlineMedium) } }
