package ru.constructor.handbook.ui.reference

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.constructor.handbook.reference.catalog.MaterialConditionCardModel
import ru.constructor.handbook.reference.catalog.ReferenceCardModel
import ru.constructor.handbook.reference.catalog.ReferenceCardStatus

@Composable
public fun ReferenceCard(model: ReferenceCardModel, modifier: Modifier = Modifier) {
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            AssistChip(onClick = {}, label = { Text(model.status.labelRu()) }, colors = AssistChipDefaults.assistChipColors(labelColor = model.status.color()))
            Text(model.title, style = MaterialTheme.typography.titleMedium)
            Text(model.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            model.details.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
            Text("Источник: ${model.sourceId} · dataset ${model.datasetVersion}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
public fun MaterialConditionCard(model: MaterialConditionCardModel, modifier: Modifier = Modifier) {
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            AssistChip(onClick = {}, label = { Text(model.verificationStatus.labelRu()) })
            Text("${model.materialDesignation} · ${model.conditionLabel}", style = MaterialTheme.typography.titleMedium)
            model.properties.forEach { Text("• $it") }
            Text("Источник: ${model.sourceId} · dataset ${model.datasetVersion}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

private fun ReferenceCardStatus.labelRu(): String = when (this) {
    ReferenceCardStatus.Verified -> "Проверено"
    ReferenceCardStatus.Draft -> "Черновик"
    ReferenceCardStatus.FutureReplacement -> "Будущая замена"
    ReferenceCardStatus.Expired -> "Истёк / заменён"
    ReferenceCardStatus.Placeholder -> "Требуется таблица"
}

@Composable
private fun ReferenceCardStatus.color() = when (this) {
    ReferenceCardStatus.Verified -> MaterialTheme.colorScheme.primary
    ReferenceCardStatus.Draft -> MaterialTheme.colorScheme.tertiary
    ReferenceCardStatus.FutureReplacement -> MaterialTheme.colorScheme.secondary
    ReferenceCardStatus.Expired -> MaterialTheme.colorScheme.error
    ReferenceCardStatus.Placeholder -> MaterialTheme.colorScheme.onSurfaceVariant
}
