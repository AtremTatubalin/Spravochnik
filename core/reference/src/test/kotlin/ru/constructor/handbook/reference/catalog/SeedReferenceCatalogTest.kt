package ru.constructor.handbook.reference.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SeedReferenceCatalogTest {
    @Test fun standardsExposeLifecycleStatuses() {
        val statuses = SeedReferenceCatalog.standards.map { it.status }.toSet()
        assertTrue(ReferenceCardStatus.Verified in statuses)
        assertTrue(ReferenceCardStatus.FutureReplacement in statuses)
        assertTrue(ReferenceCardStatus.Expired in statuses)
    }

    @Test fun materialPropertiesAreOnlyOnConditions() {
        assertTrue(SeedReferenceCatalog.materialConditions.all { it.properties.isNotEmpty() })
        assertFalse(SeedReferenceCatalog.materials.any { detail -> detail.details.any { it.contains("МПа") } })
    }
}
