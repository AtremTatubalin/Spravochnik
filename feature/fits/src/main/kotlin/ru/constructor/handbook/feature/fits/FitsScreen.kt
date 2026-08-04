package ru.constructor.handbook.feature.fits
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val FitsRoute: String = "fits"
@Composable public fun FitsScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Допуски и посадки", style=MaterialTheme.typography.headlineMedium) } }
