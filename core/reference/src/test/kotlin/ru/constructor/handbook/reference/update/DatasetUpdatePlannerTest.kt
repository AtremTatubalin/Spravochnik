package ru.constructor.handbook.reference.update

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DatasetUpdatePlannerTest {
    @Test fun dryRunBlocksUnverifiedOrBadChecksumAndPlansVerifiedImport() {
        val goodChecksum = "a".repeat(64)
        val dry = DatasetUpdatePlanner.dryRun(
            current = listOf(DatasetVersion("formulas", "0.1.0", "now", "verified")),
            update = listOf(
                UpdatePackageEntry("formulas", "0.2.0", goodChecksum, true),
                UpdatePackageEntry("materials", "0.2.0", "bad", true),
                UpdatePackageEntry("bearings", "0.2.0", goodChecksum, false),
            ),
        )

        assertFalse(dry.canImport)
        assertEquals(1, dry.entriesToImport.size)
        assertEquals(2, dry.blockedEntries.size)
        assertTrue(dry.warnings.isNotEmpty())
        assertEquals("formulas", DatasetUpdatePlanner.importPlan(dry.copy(blockedEntries = emptyList())).single().datasetId)
    }
}
