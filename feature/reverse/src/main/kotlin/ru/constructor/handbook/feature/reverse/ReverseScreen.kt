package ru.constructor.handbook.feature.reverse

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
import ru.constructor.handbook.reverse.ReverseWizardDefinition
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val ReverseRoute: String = "reverse"

public data class ReverseWizardCardState(
    val id: String,
    val titleRu: String,
    val statusRu: String,
    val nextBestMeasurement: String?,
    val warningRu: String,
)

public fun ReverseWizardCardState(definition: ReverseWizardDefinition): ReverseWizardCardState {
    val requiresTable = definition.stopConditions.contains("requires_standard_table")
    return ReverseWizardCardState(
        id = definition.id,
        titleRu = definition.titleRu,
        statusRu = if (requiresTable) "requires_standard_table" else "data_driven_scoring",
        nextBestMeasurement = definition.measurements.maxByOrNull { it.weight }?.key,
        warningRu = definition.warnings.firstOrNull().orEmpty(),
    )
}

public val ReverseWizardCards: List<ReverseWizardCardState> = listOf(
    ReverseWizardCardState("bearing_by_dimensions", "Подбор подшипника по размерам", "data_driven_scoring", "d", "Учитываются неопределённость измерения и износ."),
    ReverseWizardCardState("metric_thread", "Определение метрической резьбы", "requires_standard_table", "majorDiameter", "Таблица резьб должна быть импортирована из проверенного набора."),
    ReverseWizardCardState("straight_spline", "Определение прямобочного шлица", "requires_standard_table", "z", "Останов до импорта лицензированной таблицы стандарта."),
    ReverseWizardCardState("involute_spline", "Определение эвольвентного шлица", "requires_standard_table", "z", "Останов до импорта лицензированной таблицы стандарта."),
    ReverseWizardCardState("cylindrical_gear", "Определение цилиндрического зубчатого колеса", "data_driven_scoring", "z", "Кандидат модуля — стартовая оценка, не нормативное подтверждение."),
)

@Composable
public fun ReverseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.medium),
    ) {
        Text(
            text = "Реверс-инжиниринг",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Мастера показывают кандидатов, причины уверенности, противоречия и следующий лучший замер. Шлицы останавливаются на requires_standard_table до импорта таблиц.",
            style = MaterialTheme.typography.bodyMedium,
        )
        ReverseWizardCards.forEach { card -> ReverseWizardCard(card) }
    }
}

@Composable
private fun ReverseWizardCard(card: ReverseWizardCardState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(DarkCadSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.small),
        ) {
            Text(text = card.titleRu, style = MaterialTheme.typography.titleMedium)
            Text(text = card.id, style = MaterialTheme.typography.labelMedium)
            Text(text = "Статус: ${card.statusRu}", style = MaterialTheme.typography.labelLarge)
            Text(text = "Следующий лучший замер: ${card.nextBestMeasurement ?: "нет"}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Ограничение: ${card.warningRu}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
