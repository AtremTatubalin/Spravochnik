package ru.constructor.handbook.feature.reverse

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReverseWizardCardsTest {
    @Test fun cardsExposeTableStopsForSplineWizards() {
        assertEquals(5, ReverseWizardCards.size)
        assertEquals("requires_standard_table", ReverseWizardCards.first { it.id == "straight_spline" }.statusRu)
        assertEquals("requires_standard_table", ReverseWizardCards.first { it.id == "involute_spline" }.statusRu)
        assertTrue(ReverseWizardCards.all { it.warningRu.isNotBlank() })
    }
}
