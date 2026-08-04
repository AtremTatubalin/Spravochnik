package ru.constructor.handbook.feature.profiles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val ProfilesRoute: String = "profiles"

@Composable
public fun ProfilesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Сортамент",
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
