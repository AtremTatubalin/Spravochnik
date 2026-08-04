package ru.constructor.handbook.feature.materials
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val MaterialsRoute: String = "materials"
@Composable public fun MaterialsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Материалы", style=MaterialTheme.typography.headlineMedium) } }
