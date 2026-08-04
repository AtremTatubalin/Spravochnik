package ru.constructor.handbook.feature.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

public const val SearchRoute: String = "search"
public enum class SearchDomain(public val label: String) { Standards("Стандарты"), Materials("Материалы"), Bearings("Подшипники"), Calculators("Расчёты") }
public data class SearchResultItem(val id: String, val domain: SearchDomain, val title: String, val subtitle: String, val verified: Boolean)
public data class GlobalSearchUiState(val query: String = "", val results: List<SearchResultItem> = emptyList(), val favorites: Set<String> = emptySet(), val recent: List<SearchResultItem> = emptyList())
public class GlobalSearchViewModel { public fun search(query: String): GlobalSearchUiState = GlobalSearchUiState(query = query, results = if (query.isBlank()) emptyList() else listOf(SearchResultItem("placeholder-standard", SearchDomain.Standards, "Нет импортированных совпадений", "Нормативные данные не подставляются без источника", false))) }

@Composable
public fun SearchScreen(viewModel: GlobalSearchViewModel = GlobalSearchViewModel()) {
    var query by remember { mutableStateOf("") }
    val state = viewModel.search(query)
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Глобальный поиск", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Обозначение, материал, подшипник, расчёт") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { SearchDomain.entries.forEach { AssistChip(onClick = {}, label = { Text(it.label) }) } }
        if (state.results.isEmpty()) Text("Введите запрос. Поиск охватывает стандарты, материалы, подшипники и калькуляторы.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        state.results.forEach { item -> ListItem(headlineContent = { Text(item.title) }, supportingContent = { Text("${item.domain.label} · ${item.subtitle}") }) }
    }
}
