package ru.constructor.handbook.feature.calculators

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PreliminaryCalculatorCardsTest {
    @Test fun allPreliminaryCardsRequireEngineerReviewAndShowLimitations() {
        assertEquals(6, PreliminaryCalculatorCards.size)
        PreliminaryCalculatorCards.forEach { card ->
            assertEquals("requires_engineer_review", card.reviewStatus)
            assertTrue(card.isEngineerReviewRequired)
            assertTrue(card.limitationRu.isNotBlank())
        }
    }
}
