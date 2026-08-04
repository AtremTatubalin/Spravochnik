package ru.constructor.handbook.feature.settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val SettingsRoute: String = "settings"
@Composable public fun SettingsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Настройки", style=MaterialTheme.typography.headlineMedium) } }
