package ru.constructor.handbook.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

public const val HomeRoute: String = "home"

public data class DashboardCard(val title: String, val subtitle: String, val route: String)
public data class HomeUiState(val cards: List<DashboardCard> = defaultDashboardCards, val recent: List<String> = emptyList(), val favorites: List<String> = emptyList())
public class HomeViewModel { public fun initialState(): HomeUiState = HomeUiState() }
public val defaultDashboardCards: List<DashboardCard> = listOf(
    DashboardCard("Глобальный поиск", "Стандарты, материалы, подшипники и расчёты", "search"),
    DashboardCard("Проекты", "Сборки, детали, измерения, заметки и фото", "projects"),
    DashboardCard("Расчёты", "Заготовки калькуляторов без нормативных подстановок", "calculators"),
)

@Composable
public fun HomeScreen(state: HomeUiState = HomeViewModel().initialState()) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Справочник конструктора", style = MaterialTheme.typography.headlineMedium)
        Text("Офлайн CAD-панель", color = MaterialTheme.colorScheme.onSurfaceVariant)
        state.cards.forEach { card ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(card.title, style = MaterialTheme.typography.titleMedium)
                    Text(card.subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        AssistChip(onClick = {}, label = { Text("Избранное: ${state.favorites.size} · Недавние: ${state.recent.size}") })
    }
}
