package ru.constructor.handbook.feature.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GlobalSearchViewModelTest {
    @Test fun blankQueryHasNoResults() {
        assertTrue(GlobalSearchViewModel().search(" ").results.isEmpty())
    }

    @Test fun nonBlankQueryReturnsTraceabilityWarningPlaceholder() {
        val state = GlobalSearchViewModel().search("ГОСТ")
        assertEquals("ГОСТ", state.query)
        assertEquals(SearchDomain.Standards, state.results.single().domain)
    }
}
