package ru.constructor.handbook.feature.bearings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val BearingsRoute: String = "bearings"
@Composable public fun BearingsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Подшипники", style=MaterialTheme.typography.headlineMedium) } }
