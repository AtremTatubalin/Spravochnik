package ru.constructor.handbook.feature.fasteners

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val FastenersRoute: String = "fasteners"

@Composable
public fun FastenersScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Крепёж",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
