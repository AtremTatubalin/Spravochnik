package ru.constructor.handbook.feature.home

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {
    @Test fun dashboardContainsVerticalShellEntries() {
        val state = HomeViewModel().initialState()
        assertEquals(3, state.cards.size)
        assertTrue(state.cards.any { it.route == "search" })
        assertTrue(state.cards.any { it.route == "projects" })
    }
}
