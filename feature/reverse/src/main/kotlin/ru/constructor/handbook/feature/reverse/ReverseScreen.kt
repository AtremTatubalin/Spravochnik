package ru.constructor.handbook.feature.reverse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val ReverseRoute: String = "reverse"

@Composable
public fun ReverseScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Реверс-инжиниринг",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
