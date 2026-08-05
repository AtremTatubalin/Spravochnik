package ru.constructor.handbook.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val SettingsRoute: String = "settings"

public data class DatasetVersionUiState(
    val datasetId: String,
    val version: String,
    val verificationStatus: String,
    val updateStatus: String,
)

public val DatasetVersionPageState: List<DatasetVersionUiState> = listOf(
    DatasetVersionUiState("standards", "0.1.0", "verified", "dry-run before import"),
    DatasetVersionUiState("sources", "0.1.0", "verified", "dry-run before import"),
    DatasetVersionUiState("formulas", "0.1.0", "verified", "dry-run before import"),
    DatasetVersionUiState("reverse_wizards", "0.1.0", "verified", "dry-run before import"),
)

@Composable
public fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.medium),
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Версии наборов данных и обновления",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Перед импортом пакет обновления проходит dry-run: проверяются версия, SHA-256 и verified-статус.",
            style = MaterialTheme.typography.bodyMedium,
        )
        DatasetVersionPageState.forEach { state -> DatasetVersionCard(state) }
    }
}

@Composable
private fun DatasetVersionCard(state: DatasetVersionUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(DarkCadSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.small),
        ) {
            Text(text = state.datasetId, style = MaterialTheme.typography.titleMedium)
            Text(text = "Версия: ${state.version}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Проверка: ${state.verificationStatus}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Обновление: ${state.updateStatus}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
