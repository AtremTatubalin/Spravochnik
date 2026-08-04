package ru.constructor.handbook.feature.calculators
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val CalculatorsRoute: String = "calculators"
@Composable public fun CalculatorsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Расчёты", style=MaterialTheme.typography.headlineMedium) } }
