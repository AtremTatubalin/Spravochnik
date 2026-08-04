package ru.constructor.handbook.feature.fasteners
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val FastenersRoute: String = "fasteners"
@Composable public fun FastenersScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Крепёж", style=MaterialTheme.typography.headlineMedium) } }
