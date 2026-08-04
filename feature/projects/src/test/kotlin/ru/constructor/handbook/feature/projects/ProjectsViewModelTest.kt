package ru.constructor.handbook.feature.projects

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectsViewModelTest {
    @Test fun nameControlsCreateState() {
        val state = ProjectsViewModel().reduce(ProjectsUiState(), ProjectsEvent.NameChanged("Редуктор"))
        assertTrue(state.canCreate)
        assertEquals("Редуктор", state.nameDraft)
    }

    @Test fun createProjectAppendsDraftAndClearsInput() {
        val viewModel = ProjectsViewModel()
        val named = viewModel.reduce(ProjectsUiState(), ProjectsEvent.NameChanged(" Вал "))
        val created = viewModel.reduce(named, ProjectsEvent.CreateClicked)
        assertEquals("Вал", created.projects.single().name)
        assertFalse(created.canCreate)
        assertEquals("", created.nameDraft)
    }
}
