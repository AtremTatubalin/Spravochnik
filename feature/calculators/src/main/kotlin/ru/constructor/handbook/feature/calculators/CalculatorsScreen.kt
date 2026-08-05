package ru.constructor.handbook.feature.calculators

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

public const val CalculatorsRoute: String = "calculators"

public data class PreliminaryCalculatorCardState(
    val formulaId: String,
    val titleRu: String,
    val limitationRu: String,
    val reviewStatus: String = "requires_engineer_review",
) {
    public val isEngineerReviewRequired: Boolean = reviewStatus == "requires_engineer_review"
}

public val PreliminaryCalculatorCards: List<PreliminaryCalculatorCardState> = listOf(
    PreliminaryCalculatorCardState("key_preliminary", "Шпонка: срез и смятие", "Без допускаемых напряжений; эффективная высота задаётся пользователем."),
    PreliminaryCalculatorCardState("spline_bearing_preliminary", "Шлиц: смятие", "zEff, рабочая высота и допускаемое давление должны быть взяты из выбранного метода."),
    PreliminaryCalculatorCardState("bolt_tension_preliminary", "Болт: растяжение", "Не учитывает затяжку, податливость стыка, усталость и нормативные коэффициенты."),
    PreliminaryCalculatorCardState("bolt_direct_shear_preliminary", "Болт: прямой срез", "Не учитывает смятие, эксцентриситет, проскальзывание и группы болтов."),
    PreliminaryCalculatorCardState("fillet_weld_direct_shear", "Угловой шов: прямой срез", "Только равномерный прямой срез; сложные группы швов требуют нормативного расчёта."),
    PreliminaryCalculatorCardState("compression_spring_basic", "Пружина сжатия", "Устойчивость, соударение витков, усталость и допускаемые напряжения проверяются отдельно."),
)

@Composable
public fun CalculatorsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.medium),
    ) {
        Text(
            text = "Расчёты",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Предварительные расчёты всегда помечаются requires_engineer_review и не содержат нормативных допускаемых напряжений.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
        PreliminaryCalculatorCards.forEach { card ->
            PreliminaryCalculatorCard(card)
        }
    }
}

@Composable
private fun PreliminaryCalculatorCard(card: PreliminaryCalculatorCardState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(DarkCadSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.small),
        ) {
            Text(text = card.titleRu, style = MaterialTheme.typography.titleMedium)
            Text(text = card.formulaId, style = MaterialTheme.typography.labelMedium)
            Text(
                text = "Ограничения: ${card.limitationRu}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "Статус результата: ${card.reviewStatus}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
