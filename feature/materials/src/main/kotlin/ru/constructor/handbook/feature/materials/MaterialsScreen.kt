package ru.constructor.handbook.feature.materials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.constructor.handbook.reference.catalog.SeedReferenceCatalog
import ru.constructor.handbook.ui.reference.MaterialConditionCard
import ru.constructor.handbook.ui.reference.ReferenceCard
import ru.constructor.handbook.ui.theme.DarkCadSpacing

public const val MaterialsRoute: String = "materials"

@Composable
public fun MaterialsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(DarkCadSpacing.large),
        verticalArrangement = Arrangement.spacedBy(DarkCadSpacing.medium),
    ) {
        Text(
            text = "Материалы",
            style = MaterialTheme.typography.headlineMedium,
        )
        SeedReferenceCatalog.materials.forEach { ReferenceCard(it) }
        if (SeedReferenceCatalog.materialConditions.isEmpty()) {
            Text("Состояния материала пока не импортированы; механические свойства скрыты до появления условий.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        SeedReferenceCatalog.materialConditions.forEach { MaterialConditionCard(it) }
    }
}
