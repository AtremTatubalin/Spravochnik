package ru.constructor.handbook.feature.reverse
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
public const val ReverseRoute: String = "reverse"
@Composable public fun ReverseScreen() { Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment=Alignment.Center) { Text("Реверс-инжиниринг", style=MaterialTheme.typography.headlineMedium) } }
