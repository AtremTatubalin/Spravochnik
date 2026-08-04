package ru.constructor.handbook.feature.projects

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.constructor.handbook.projects.model.*

public const val ProjectsRoute: String = "projects"

public data class ProjectsUiState(val projects: List<Project> = emptyList(), val selected: ProjectDetail? = null, val nameDraft: String = "", val canCreate: Boolean = false)
public sealed interface ProjectsEvent { public data class NameChanged(val value: String) : ProjectsEvent; public data object CreateClicked : ProjectsEvent; public data class PhotoPicked(val uri: String) : ProjectsEvent }
public class ProjectsViewModel { public fun reduce(state: ProjectsUiState, event: ProjectsEvent): ProjectsUiState = when (event) { is ProjectsEvent.NameChanged -> state.copy(nameDraft = event.value, canCreate = event.value.isNotBlank()); ProjectsEvent.CreateClicked -> if (state.canCreate) state.copy(projects = state.projects + Project("draft-${state.projects.size + 1}", state.nameDraft.trim(), null, "", "", false), nameDraft = "", canCreate = false) else state; is ProjectsEvent.PhotoPicked -> state } }

@Composable
public fun ProjectsScreen(viewModel: ProjectsViewModel = ProjectsViewModel()) {
    var state by remember { mutableStateOf(ProjectsUiState()) }
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? -> uri?.let { state = viewModel.reduce(state, ProjectsEvent.PhotoPicked(it.toString())) } }
    val documentPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? -> uri?.let { state = viewModel.reduce(state, ProjectsEvent.PhotoPicked(it.toString())) } }
    Row(Modifier.fillMaxSize().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(Modifier.weight(0.42f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Проекты", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(state.nameDraft, { state = viewModel.reduce(state, ProjectsEvent.NameChanged(it)) }, label = { Text("Название проекта") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Button(onClick = { state = viewModel.reduce(state, ProjectsEvent.CreateClicked) }, enabled = state.canCreate) { Text("Создать") }
            state.projects.ifEmpty { listOf(Project("empty", "Пока нет проектов", "Создайте проект для деталей, замеров и фото", "", "", false)) }.forEach { project -> ListItem(headlineContent = { Text(project.name) }, supportingContent = { Text(project.description ?: "Сборки · детали · измерения · заметки") }) }
        }
        ProjectDetailPanel(
            detail = state.selected,
            onPickPhoto = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            onOpenDocument = { documentPicker.launch(arrayOf("image/*", "application/pdf")) },
            modifier = Modifier.weight(0.58f),
        )
    }
}

@Composable
public fun ProjectDetailPanel(detail: ProjectDetail?, onPickPhoto: () -> Unit, onOpenDocument: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(modifier.fillMaxHeight()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(detail?.project?.name ?: "Карточка проекта", style = MaterialTheme.typography.titleLarge)
            Text("Dark CAD детализация: сборки, детали, измерения, заметки и вложения", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedButton(onPickPhoto) { Text("Добавить фото") }; OutlinedButton(onOpenDocument) { Text("Файл/документ") } }
            Text("Сборки: ${detail?.assemblies?.size ?: 0} · Детали: ${detail?.parts?.size ?: 0} · Замеры: ${detail?.measurements?.size ?: 0}")
            Text("Вложения: ${detail?.attachments?.size ?: 0} · Заметки: ${detail?.notes?.size ?: 0}")
            HorizontalDivider()
            Text("Нормативные значения не создаются в проекте автоматически; все расчёты и источники будут сохраняться трассируемо на следующих этапах.")
        }
    }
}
